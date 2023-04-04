package com.qy.ntf.util;

import com.aliyun.oss.OSSClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @
 *
 * @author MengJinyue
 * @date 2018年3月19日 下午9:56:48
 */
public class OSSUtil {

  public static String uploadFile(MultipartFile file) {
    try {
      OSSClient ossClient =
          new OSSClient(
              ResourcesUtil.getProperty("end_point"),
              ResourcesUtil.getProperty("ali_access_key_id"),
              ResourcesUtil.getProperty("ali_access_key_secret"));
      String name = file.getName();
      String backgroundImg = "";
      if (!name.contains(".")) {
        backgroundImg =
            UUID.randomUUID().toString().replaceAll("-", "")
                + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
      } else {
        backgroundImg =
            UUID.randomUUID().toString().replaceAll("-", "")
                + file.getName().substring(file.getOriginalFilename().lastIndexOf("."));
      }
      System.out.println(System.currentTimeMillis());
      ossClient.putObject(
          ResourcesUtil.getProperty("picture_bucket_name"), backgroundImg, file.getInputStream());
      System.out.println(System.currentTimeMillis());
      ossClient.shutdown();
      return "https://"
          + ResourcesUtil.getProperty("picture_bucket_name")
          + "."
          + ResourcesUtil.getProperty("end_point")
          + "/"
          + backgroundImg;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String uploadFile(MultipartFile file, String indexFileName) {
    try {
      OSSClient ossClient =
          new OSSClient(
              ResourcesUtil.getProperty("end_point"),
              ResourcesUtil.getProperty("ali_access_key_id"),
              ResourcesUtil.getProperty("ali_access_key_secret"));
      String backgroundImg =
          UUID.randomUUID().toString().replaceAll("-", "")
              + indexFileName.substring(indexFileName.lastIndexOf("."));
      ossClient.putObject(
          ResourcesUtil.getProperty("picture_bucket_name"), backgroundImg, file.getInputStream());
      ossClient.shutdown();
      return "https://"
          + ResourcesUtil.getProperty("picture_bucket_name")
          + "."
          + ResourcesUtil.getProperty("end_point")
          + "/"
          + backgroundImg;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void main(String[] a) {
    String s =
        uploadFile(
            new File("C:\\Users\\23198\\Pictures\\v2-7be269d198a362610c8eca6dfa196c98_r.jpg"),
            "v2-7be269d198a362610c8eca6dfa196c98_r.jpg");
    System.out.println(s);
  }

  public static String uploadFile(File file, String fileName) {
    try {
      OSSClient ossClient =
          new OSSClient(
              ResourcesUtil.getProperty("end_point"),
              ResourcesUtil.getProperty("ali_access_key_id"),
              ResourcesUtil.getProperty("ali_access_key_secret"));
      ossClient.putObject(
          ResourcesUtil.getProperty("picture_bucket_name"), fileName, new FileInputStream(file));
      ossClient.shutdown();
      return "https://"
          + ResourcesUtil.getProperty("picture_bucket_name")
          + "."
          + ResourcesUtil.getProperty("end_point")
          + "/"
          + fileName;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
