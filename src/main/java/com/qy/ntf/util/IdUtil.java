package com.qy.ntf.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/** Eric DESC : id工具类 */
public class IdUtil {

  private static final SnowflakeIdGen snowflakeIdGen = new SnowflakeIdGen(0, 0);

  // 获取雪花id
  public static Long getSnowflakeId() {
    return snowflakeIdGen.nextId();
  }

  public static String getRamdomIndex() {
    char[] sources =
        new char[] {
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
          's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
          'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1',
          '2', '3', '4', '5', '6', '7', '8', '9'
        };
    int length = sources.length;
    ThreadLocalRandom random = ThreadLocalRandom.current();
    StringBuilder sb = new StringBuilder();
    for (int j = 0; j < 6; j++) {
      sb.append(sources[random.nextInt(length)]);
    }
    return sb.toString();
  }

  /**
   * 获取UUID
   *
   * @return
   */
  public static String getUUID() {
    String str = UUID.randomUUID().toString();
    return str.replaceAll("-", "").toLowerCase();
  }

  public static void main(String[] args) {
    for (int i = 0; i < 30; i++) {
      System.out.println(getSnowflakeId());
    }
  }
}
