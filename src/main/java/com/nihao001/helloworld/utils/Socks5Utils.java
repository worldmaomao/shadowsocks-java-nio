package com.nihao001.helloworld.utils;

public class Socks5Utils {

	
	public static byte[] versionOk(){
		return new byte[] {5, 0};
	}
	
	public static byte[] versionError(){
		return new byte[] {5, (byte)255};
	}
	
	public static byte[] connectServerError(){
		return new byte[]{5, 3, 0, 1, 0, 0, 0, 0, 1, 1};
	}
	
	public static byte[] connectServerSuccess(){
		return new byte[]{5, 0, 0, 1, 0, 0, 0, 0, 1, 1};
	}
	
	public static void main(String[] args) {
		System.out.println(Integer.toHexString(255));
	}
	
}
