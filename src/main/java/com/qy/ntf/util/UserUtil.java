package com.qy.ntf.util;

import com.qy.ntf.bean.dto.UserData;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

public class UserUtil {
	
	static Base64.Decoder decoder = Base64.getDecoder();
	
	public static UserData parseUserData(String data) {
		
		// data format:  user id, username, realname, organization code
		UserData userData = new UserData();
		if(data != null) {
			String userInfo = new String(decoder.decode(data));
			System.out.println("= common => " + userInfo);
			
			String[] str = userInfo.split(",");
			
			for(String aa : str) {
				System.out.println("    = common => " + aa);
			}
			
			userData.setUserId(Integer.parseInt(str[0]));
			userData.setUsername(str[1]);
			userData.setRealname(str[2]);
			userData.setOrganizationCode(str[3]);
		}
		
		System.out.println("= commons => " + userData.toString());
		return userData;
	}

	/**
	 * 从token String中获取用户信息
	 *
	 * @param token
	 * @return
	 */
	public static AuthUserInfo parseAuthUser(String token) {
		String userInfo = new String(decoder.decode(token));
		String[] str = userInfo.split(",");
		AuthUserInfo authUser = new AuthUserInfo(Integer.parseInt(str[0]), str[1], str[2], str[3]);
		return authUser;
	}

	/**
	 * 从request获取用户信息
	 *
	 * @param request
	 * @return
	 */
	public static AuthUserInfo getUserData(HttpServletRequest request) throws Exception {
		return parseAuthUser(getToken(request));
	}

	/**
	 * 获取token
	 *
	 * @param request
	 * @return
	 */
	public static String getToken(HttpServletRequest request) throws Exception {
		String token = request.getHeader("User-Info");
		if (null == token || "".equals(token)) {
			throw new Exception( "请先登陆或申请凭证");
		}
		return token;
	}
}
