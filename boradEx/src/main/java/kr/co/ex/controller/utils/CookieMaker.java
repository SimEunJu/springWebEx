package kr.co.ex.controller.utils;

import javax.servlet.http.Cookie;

public class CookieMaker {
	public static Cookie newCookie(Cookie cookie){
		int DAY =  60*60*24;
		String cookieVal = cookie.getValue();
		int firstZIdx = cookieVal.indexOf("z");
		int curCnt = Integer.valueOf(cookieVal.substring(0, firstZIdx));
		int newCnt = curCnt - 1;
		String newCookieVal = newCnt+cookieVal.substring(firstZIdx);
		Cookie newCookie = new Cookie(cookie.getName(), newCookieVal);
		newCookie.setPath("/");
		newCookie.setMaxAge(DAY);
		return newCookie;
	}
}
