package com.warning.utils.ocr;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class IdentifyImageContentUtils {

    private static final String POST_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?access_token=" + AuthService.getAuth();

    private static final CloseableHttpClient httpClient;

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    /**
     * 识别本地图片的文字
     *
     * @param path 本地图片地址
     * @return 识别结果，为json格式
     * @throws URISyntaxException URI打开异常
     * @throws IOException        io流异常
     */
    public static String checkFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new NullPointerException("图片不存在");
        }
        String imgBase64 = BaseImg64.getImageStrFromPath(path);
        try {
            String param = "image=" + URLEncoder.encode(imgBase64, "UTF-8");
            return post(param);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param url 图片url
     * @return 识别结果，为json格式
     */
    public static String checkUrl(String url) throws IOException, URISyntaxException {
        String param = "url=" + url;
        return post(param);
    }


    /**
     * 通过传递参数：url和image进行文字识别
     *
     * @param param 区分是url还是image识别
     * @return 识别结果
     * @throws URISyntaxException URI打开异常
     * @throws IOException        IO流异常
     */
    private static String post(String param) throws URISyntaxException, IOException {
        //开始搭建post请求
        HttpPost post = new HttpPost();
        URI url = new URI(POST_URL);
        post.setURI(url);
        //设置请求头，请求头必须为application/x-www-form-urlencoded，因为是传递一个很长的字符串，不能分段发送
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        StringEntity entity = new StringEntity(param);
        post.setEntity(entity);
        HttpResponse response = httpClient.execute(post);
        System.out.println(response.toString());
        if (response.getStatusLine().getStatusCode() == 200) {
            String str;
            try {
                /*读取服务器返回过来的json字符串数据*/
                str = EntityUtils.toString(response.getEntity());
                System.out.println(str);
                return str;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static void main(String[] args) {
//        String path = FileUtils.FILE_PATH + "/images/2023-05-03/test.jpg";
//        long now = System.currentTimeMillis();
//        checkFile(path);
//        String imageContent = IdentifyImageContentUtil.checkFile(path);
//        JSONArray results = JSON.parseObject(imageContent).getJSONArray("words_result");
//        if (!CollectionUtils.isEmpty(results)) {
//            List<String> words = results.stream().map(e -> JSON.parseObject(e.toString()).getString("words")).collect(Collectors.toList());
//            StringBuilder sb = new StringBuilder();
//            for (String str : words) {
//                sb.append(str);
//            }
//            String totalString = sb.toString();
//            System.out.println(totalString);
//        }
//        System.out.println("耗时：" + (System.currentTimeMillis() - now) / 1000 + "s");
    }
}
