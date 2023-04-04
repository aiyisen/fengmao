package com.qy.ntf.controller.common;

import com.qy.ntf.service.FileLocalService;
import com.qy.ntf.util.IdUtil;
import com.qy.ntf.util.OSSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: firstSet @Package: com.lingo.firstSet.controller @ClassName:
 * SystemController @Author: 王振读 @Description: ${description} @Date: 2021/11/26 9:31 @Version: 1.0
 */
@RestController
@Api(tags = {"通用 - 文件管理"})
public class FileController {

  @Value("${file.upload-path}")
  private String basePath;

  @Autowired private FileLocalService fileLocalService;

  @PostMapping("/local/upload")
  @ResponseBody
  @ApiOperation(value = "GENERAL - " + "文件服务 - 上传文件", notes = "上传文件到本地服务器")
  public Map<String, Object> uploadFileLocal(
      @RequestParam(value = "file", required = true) MultipartFile file) {
    Map<String, Object> result = new HashMap<>();
    try {
      //      String path = fileLocalService.uploadFile(file);
      String path = OSSUtil.uploadFile(file);
      result.put("code", 0);
      result.put("msg", "上传成功");
      result.put("data", path);
    } catch (Exception e) {
      result.put("code", -1);
      result.put("msg", "上传错误, 原因：" + e.getMessage());
      result.put("data", null);
    }
    return result;
  }

  @GetMapping("/local/download")
  @ApiOperation(value = "GENERAL - " + "文件服务 - 下载文件", notes = "从本地服务器下载文件")
  public void downloadFile(HttpServletResponse response, @RequestParam("path") String path)
      throws Exception {
    String fullPath = basePath + File.separator + path;
    File file = new File(fullPath);
    if (!file.exists()) {
      throw new IOException("文件不存在");
    }
    String suffixName = path.substring(path.lastIndexOf("."));
    String fileName = IdUtil.getSnowflakeId() + suffixName;
    BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
    try {
      BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
      // 通知浏览器以附件形式下载
      //      response.setHeader(
      //          "Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,
      // "utf-8"));
      //      response.setContentType("application/force-download");
      byte[] car = new byte[1024];
      int l;
      while ((l = in.read(car)) != -1) {
        out.write(car, 0, l);
      }
      out.flush();
      out.close();
      in.close();
    } catch (Exception e) {
      throw new IOException("文件下载失败");
    }
  }
}
