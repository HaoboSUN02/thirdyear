package com.warning.utils;

import sun.security.x509.*;

import javax.security.auth.x500.X500Principal;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Date;


public class GenerateSelfSignedCertificate {
    public static void main(String[] args) throws Exception {
        // 生成RSA密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 构建证书信息
        X509CertInfo certInfo = new X509CertInfo();
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 365 * 24 * 60 * 60 * 1000L); // 一年有效期

        X500Principal subject = new X500Principal("CN=example.com,O=Example Organization,L=Beijing,C=CN");
        X500Name x500Name = new X500Name(subject.getName());
        certInfo.set(X509CertInfo.SUBJECT, x500Name);
        certInfo.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3));
        certInfo.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(new BigInteger(64, new SecureRandom())));
        certInfo.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(AlgorithmId.get("SHA256withRSA")));
        certInfo.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic()));
        certInfo.set(X509CertInfo.VALIDITY, new CertificateValidity(startDate, endDate));
        X500Principal subject2 = new X500Principal("CN=example.com,O=Example Organization,L=Beijing,C=CN");
        X500Name x500Name2 = new X500Name(subject2.getName());
        certInfo.set(X509CertInfo.ISSUER, x500Name2);

        // 添加BasicConstraints扩展
        CertificateExtensions extensions = new CertificateExtensions();
        BasicConstraintsExtension bce = new BasicConstraintsExtension(true, -1);
        extensions.set(BasicConstraintsExtension.NAME, new BasicConstraintsExtension(false, bce.getExtensionValue()));
        certInfo.set(X509CertInfo.EXTENSIONS, extensions);

        // 添加SubjectKeyIdentifier扩展
        SubjectKeyIdentifierExtension ski = new SubjectKeyIdentifierExtension(new KeyIdentifier(keyPair.getPublic()).getIdentifier());
        extensions.set(SubjectKeyIdentifierExtension.NAME, new SubjectKeyIdentifierExtension(false, ski.getExtensionValue()));

        // 添加AuthorityKeyIdentifier扩展
        AuthorityKeyIdentifierExtension aki = new AuthorityKeyIdentifierExtension(new KeyIdentifier(keyPair.getPublic()), null, null);
        extensions.set(AuthorityKeyIdentifierExtension.NAME, new AuthorityKeyIdentifierExtension(false, aki.getExtensionValue()));

        // 使用私钥签名证书
        X509CertImpl certificate = new X509CertImpl(certInfo);
        certificate.sign(keyPair.getPrivate(), "SHA256withRSA");


        // 将证书保存到文件
        Certificate[] certificateChain = {certificate};
        FileOutputStream fos = new FileOutputStream(FileUtils.CERT_PATH + "certificate.crt");
        fos.write("-----BEGIN CERTIFICATE-----\n".getBytes());
        fos.write(Base64.getEncoder().encode(certificate.getEncoded()));
        fos.write("\n-----END CERTIFICATE-----\n".getBytes());
        fos.close();

        //将私钥保存到文件
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        FileOutputStream keyFos = new FileOutputStream(FileUtils.CERT_PATH + "public.key");
        keyFos.write(publicKeyBytes);
        keyFos.close();

        byte[] publicKeyBytes2 = keyPair.getPublic().getEncoded();
        FileOutputStream key2Fos = new FileOutputStream(FileUtils.CERT_PATH + "private.key");
        key2Fos.write(publicKeyBytes2);
        key2Fos.close();

        // 输出私钥和公钥
        System.out.println("Private Key:\n" + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        System.out.println("Public Key:\n" + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));


        // 保存证书链到文件
        FileOutputStream chainFos = new FileOutputStream(FileUtils.CERT_PATH + "certificate-chain.crt");
        for (Certificate cert : certificateChain) {
            chainFos.write("-----BEGIN CERTIFICATE-----\n".getBytes());
            chainFos.write(Base64.getEncoder().encode(cert.getEncoded()));
            chainFos.write("\n-----END CERTIFICATE-----\n".getBytes());
        }
        chainFos.close();
    }

    private static class CertificateVersion extends sun.security.x509.CertificateVersion {
        public CertificateVersion(int version) throws IOException {
            super(version);
        }
    }

    private static class CertificateX509Key extends sun.security.x509.CertificateX509Key {
        public CertificateX509Key(PublicKey key) {
            super(key);
        }
    }
}
