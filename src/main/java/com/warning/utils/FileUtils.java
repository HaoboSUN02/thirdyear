package com.warning.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class FileUtils {

    /**
     * 文件上传的保存路径  默认值
     */
    public static String UPLOAD_PATH = getProjectRootPath() + "/src/main/resources/upload";

    /**
     * 证书保存路径
     */
    public static String CERT_PATH = getProjectRootPath() + "/src/main/resources/";


    /**
     * 文件路径
     */
    public static String FILE_PATH = getProjectRootPath().replaceAll("\\\\", "/") + "/src/main/resources/upload";


    /**
     * 获取项目路径：兼容jar包和war服务模式
     *
     * @return
     */
    public static String getProjectRootPath() {
        // 获取项目路径（兼容war服务和jar服务）
        String rootPath = System.getProperty("user.dir");
        String[] splits = rootPath.split("/");
        int breakI = 0;
        boolean isJarService = false;// 项目是否是打成jar包发布
        for (int i = 0; i < splits.length; i++) {
            if (splits[i].contains(".jar")) {
                breakI = i;
                isJarService = true;
                break;
            }
        }
        StringBuilder finalRootPath = new StringBuilder();
        if (isJarService) {
            // 打成jar包后，获取的路径中会以file:XXX/XXX开头，所以去除切割后的第一个路径，i从1开始遍历
            for (int i = 1; i < breakI; i++) {
                finalRootPath.append(splits[i]).append("/");
            }
        } else {
            finalRootPath = new StringBuilder(rootPath);
        }
        return finalRootPath.toString();
    }

    /**
     * 根据文件老名字得到新名字
     *
     * @param oldName 文件老名字
     * @return 新名字由32位随机数加图片后缀组成
     */
    public static String createNewFileName(String oldName) {
        //获取文件名后缀
        String stuff = oldName.substring(oldName.lastIndexOf("."), oldName.length());
        //将UUID与文件名后缀进行拼接，生成新的文件名  生成的UUID为32位
        return IdUtil.simpleUUID().toUpperCase() + stuff;
    }

    /**
     * 文件下载
     *
     * @param path
     * @return
     */
    public static ResponseEntity<Object> createResponseEntity(String path) {
        //1,构造文件对象
        File file = new File(UPLOAD_PATH, path);
        if (file.exists()) {
            //将下载的文件，封装byte[]
            byte[] bytes = null;
            try {
                bytes = FileUtil.readBytes(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //创建封装响应头信息的对象
            HttpHeaders header = new HttpHeaders();
            //封装响应内容类型(APPLICATION_OCTET_STREAM 响应的内容不限定)
            header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            //创建ResponseEntity对象
            return new ResponseEntity<>(bytes, header, HttpStatus.CREATED);
        }
        return null;
    }

    /**
     * 更该图片的名字 去掉_temp
     *
     * @param goodsimg
     * @return
     */
    public static String renameFile(String goodsimg) {
        File file = new File(UPLOAD_PATH, goodsimg);
        String replace = goodsimg.replace("_temp", "");
        if (file.exists()) {
            file.renameTo(new File(UPLOAD_PATH, replace));
        }
        return replace;
    }

    /**
     * 根据老路径删除图片
     *
     * @param oldPath
     */
    public static void removeFileByPath(String oldPath) {
        File file = new File(UPLOAD_PATH, oldPath.substring(8));
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 上传文件
     * <p>
     * 文件夹根据日期生成
     *
     * @param file 文件流
     * @return
     */
    public static String uploadFile(MultipartFile file) {
        //1.得到文件名
        String oldName = file.getOriginalFilename();
        //2.根据旧的文件名生成新的文件名
        String newName = FileUtils.createNewFileName(oldName);
        //3.得到当前日期的字符串
        String dirName = DateUtil.format(new Date(), "yyyy-MM-dd");
        //4.构造文件夹
        File dirFile = new File(FileUtils.UPLOAD_PATH, dirName);
        //5.判断当前文件夹是否存在
        if (!dirFile.exists()) {
            //如果不存在则创建新文件夹
            dirFile.mkdirs();
        }
        //6.构造文件对象
        File createFile = new File(dirFile.getAbsolutePath(), newName);
        //7.把file里面的图片信息写入createFile
        try {
            file.transferTo(createFile);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return "/files/" + dirName + "/" + newName;
    }

    /**
     * 上传文件
     *
     * @param file        文件流
     * @param imageFolder 文件存储folder
     * @return
     */
    public static String uploadFile(MultipartFile file, String imageFolder) {
        //1.得到文件名
        String oldName = file.getOriginalFilename();
        //2.根据旧的文件名生成新的文件名
        String newName = FileUtils.createNewFileName(oldName);
        //3.若用户输入的文件夹名称不为空根据用户输入的子文件夹名称构造文件夹
        //  否则通过日期名命名文件夹
        File dirFile;
        if (!StringUtils.isEmpty(imageFolder)) {
            dirFile = new File(FileUtils.UPLOAD_PATH, imageFolder);
        } else {
            dirFile = new File(FileUtils.UPLOAD_PATH, DateUtil.format(new Date(), "yyyy-MM-dd"));
        }
        //4.判断当前文件夹是否存在
        if (!dirFile.exists()) {
            //如果不存在则创建新文件夹
            dirFile.mkdirs();
        }
        //5.构造文件对象
        File createFile = new File(dirFile.getAbsolutePath(), newName);
        //6.把file里面的图片信息写入createFile
        try {
            file.transferTo(createFile);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return "/files/" + imageFolder + "/" + newName;
    }

    /**
     * 删除本地图片
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public String delFile(String filePath) {
        //获取根目录
        String path = getProjectRootPath().replaceAll("\\\\", "/");
        //拼接完整的文件路径
        File delFile = new File(path + filePath);
        if (delFile.isFile() && delFile.exists()) {
            delFile.delete();
            return "成功删除图片";
        }
        return "没有该文件，删除失败";
    }

}
