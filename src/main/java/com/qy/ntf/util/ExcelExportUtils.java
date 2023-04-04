package com.qy.ntf.util;

import com.qy.ntf.bean.entity.SysUser;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ExcelExportUtils {

  public static List<List<String>> myReadExcel(File file) {
    List<List<String>> resultList = new ArrayList<>();
    String path = file.getName();
    Workbook wb = null;
    Short rowLen = 0;
    Integer columnLen = 0;
    try {
      if (path.endsWith("xls")) {
        FileInputStream fis = new FileInputStream(file);
        wb = new HSSFWorkbook(fis);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    Sheet firstSheet = wb.getSheetAt(0);
    if (firstSheet != null) columnLen = firstSheet.getLastRowNum();
    for (int i = 0; i < columnLen; i++) {
      Row row = firstSheet.getRow(i);
      if (row != null) {
        List<String> rowData = new ArrayList<>();
        resultList.add(rowData);
        if (rowLen == 0) rowLen = row.getLastCellNum();
        for (int i2 = 0; i2 < rowLen; i2++) {
          Cell cell = row.getCell(i2);
          if (cell != null) rowData.add(cell.toString());
          else rowData.add("");
        }
      }
    }
    return resultList;
  }

  /**
   * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
   *
   * @param title 表格标题名
   * @param headers 表格属性列名数组
   * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
   *     javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
   * @param out 与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
   * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
   */
  public static void exportExcelList(
      String title,
      String[] headers,
      Collection<SysUser> dataset,
      OutputStream out,
      String pattern,
      String[]... args) {
    // 声明一个工作薄
    HSSFWorkbook workbook = new HSSFWorkbook();
    // 生成一个表格
    HSSFSheet sheet = workbook.createSheet(title);
    // 设置表格默认列宽度为15个字节
    sheet.setDefaultColumnWidth((short) 15);
    // 生成一个样式
    HSSFCellStyle style = workbook.createCellStyle();
    // 设置这些样式
    // 设置表头的背景颜色(excel的第一行)
    // 生成一个字体
    HSSFFont font = workbook.createFont();
    // 设置表头的字体颜色
    font.setFontHeightInPoints((short) 12);
    // 把字体应用到当前的样式
    style.setFont(font);
    // 生成并设置另一个样式
    HSSFCellStyle style2 = workbook.createCellStyle();
    // 设置表体的背景颜色(除了表头)
    // 生成另一个字体
    HSSFFont font2 = workbook.createFont();
    // 把字体应用到当前的样式
    style2.setFont(font2);

    // 声明一个画图的顶级管理器
    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
    // 定义注释的大小和位置,详见文档
    HSSFComment comment =
        patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
    // 设置注释内容
    comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
    comment.setAuthor("leno");
    for (int i = 0; i < args.length - 1; i = i + 2) {
      // 下拉框的位置
      CellRangeAddressList cellRangeAddressList =
          new CellRangeAddressList(
              1, 1, Integer.valueOf(args[i + 1][0]), Integer.valueOf(args[i + 1][0]));
      // 下拉框准备的数据
      DVConstraint constraint = DVConstraint.createExplicitListConstraint(args[i]);
      HSSFDataValidation dataValidation = new HSSFDataValidation(cellRangeAddressList, constraint);
      sheet.addValidationData(dataValidation);
    }

    // 产生表格标题行
    HSSFRow row = sheet.createRow(0);
    for (short i = 0; i < headers.length; i++) {
      HSSFCell cell = row.createCell(i);
      cell.setCellStyle(style);
      HSSFRichTextString text = new HSSFRichTextString(headers[i]);
      cell.setCellValue(text);
    }

    // 遍历集合数据，产生数据行
    Iterator<SysUser> it = dataset.iterator();
    int index = 0;
    while (it.hasNext()) {
      index++;
      row = sheet.createRow(index);
      SysUser t = it.next();
      // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
      HSSFCell cell = row.createCell(0);
      cell.setCellStyle(style2);
      cell.setCellValue(t.getPhone());
      HSSFCell cell1 = row.createCell(1);
      cell1.setCellStyle(style2);
      cell1.setCellValue(t.getHasCount());
    }
    try {
      workbook.write(out);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
