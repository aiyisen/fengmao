package com.qy.ntf.service;

import com.qy.ntf.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Eric
 * @Date: 2021/5/28 14:24
 */
@Service
@Slf4j
public class FileLocalService {

    @Value("${file.upload-path}")
    private String basePath;

    public String uploadFile(MultipartFile file) {
        String filename = file.getOriginalFilename();

        assert filename != null;
        String suffixName = filename.substring(filename.lastIndexOf("."));

        StringBuilder buffer = new StringBuilder();

        String newFileName = IdUtil.getSnowflakeId() + suffixName;
        buffer.append(formatDate(new Date(), "yyyy-MM-dd")).append("/").append(newFileName);

        String savePath = buffer.toString();
        String fullPath = basePath + File.separator + savePath;

        File dest = new File(fullPath);
        if (!dest.getParentFile().exists()) {
            boolean mkdirs = dest.getParentFile().mkdirs();
            log.info("创建目录状态：{}，路径：{}",mkdirs,buffer);
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return savePath;
    }

    private static String formatDate(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }
}