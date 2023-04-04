package com.qy.ntf.util;

/**
 * 字符串处理工具类
 *
 * @author guo
 * @date 2019年4月16日14:10:18
 */
public class StringUtils {

  public static boolean isNullOrEmpty(String string) {
    return string == null || string.length() == 0;
  }

  public static boolean isEmpty(String str) {
    return str == null || "".equals(str) || str.trim().length() == 0;
  }

  public static boolean isEmpty(Integer str) {
    return str == null;
  }

  /**
   * 将字符串转为int数组
   *
   * @param str 要转换的字符串
   * @param separator 分割符号
   * @return
   * @author lxy
   */
  public static Integer[] strToIntArray(String str, String separator) {
    String[] strArr = str.split(separator); // 然后使用split方法将字符串拆解到字符串数组中
    Integer[] intArr = new Integer[strArr.length]; // 定义一个长度与上述的字符串数组长度相通的整
    return intArr;
  }
}
