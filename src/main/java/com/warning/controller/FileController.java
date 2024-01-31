package com.warning.controller;

import com.warning.common.ResponseResult;
import com.warning.utils.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * 上传文件
     *
     * @param file 文件流
     * @return 文件存储路径
     */
    @PostMapping("/upload")
    public ResponseResult fileUpload(@RequestParam(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.failed("上传文件为空");
        }
        String fileName = FileUtils.uploadFile(file);
        Map<String, String> map = new HashMap<>();
        map.put("fileName", fileName);
        return ResponseResult.success("上传成功", map);
    }

    /**
     * 图片删除
     */
    @RequestMapping("deleteImageByPath")
    public void deleteImageByPath(@RequestParam("filePath") String filePath) {
        FileUtils.removeFileByPath(filePath);
    }
}
