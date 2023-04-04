package com.qy.ntf.util;

import java.util.UUID;

/**
 * 获得uuid
 * @author liz
 *
 */
public class IdUtils {

	/**
	 * 
	 * @return 32位UUID
	 */
	public static String getUUID32(){
		String uuid = UUID.randomUUID().toString(); 
		uuid = uuid.replace("-", ""); 
		return uuid;
	}
	

}
