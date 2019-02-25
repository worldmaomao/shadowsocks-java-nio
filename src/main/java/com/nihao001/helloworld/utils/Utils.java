package com.nihao001.helloworld.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	private static final Pattern IP_ADDRESS_PATTERN = Pattern
			.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

	/**
	 * parse package header
	 * 
	 * 
	 * @param data
	 * @return
	 */
	public static PackageHeader parserHeader(byte[] data) {
		if (data == null || data.length <= 1) {
			return null;
		}
		PackageHeader header = new PackageHeader();
		byte addressType = data[0];
		int headerLength = 0;
		byte[] hostBytes = null;
		byte[] portBytes = null;

		// ipv4
		if (addressType == 1) {
			if (data.length >= 7) {
				hostBytes = Arrays.copyOfRange(data, 1, 5);
				portBytes = Arrays.copyOfRange(data, 5, 7);
				headerLength = 7;

				header.setAddressType(addressType);
				header.setAddress(Utils.byteToIp(hostBytes));
				header.setPort(portBytes);
				header.setHeaderLength(headerLength);
				return header;
			}
		}
		// domain
		else if (addressType == 3) {
			if (data.length > 2) {
				int addressLen = data[1];
				hostBytes = Arrays.copyOfRange(data, 2, 2 + addressLen);
				portBytes = Arrays.copyOfRange(data, 2 + addressLen,
						2 + addressLen + 2);
				headerLength = 2 + addressLen + 2;

				header.setAddressType(addressType);
				header.setAddress(new String(hostBytes));
				header.setPort(portBytes);
				header.setHeaderLength(headerLength);
				return header;
			}
		}
		// ipv6
		else if (addressType == 4) {
			if (data.length >= 19) {
				hostBytes = Arrays.copyOfRange(data, 1, 17);
				portBytes = Arrays.copyOfRange(data, 17, 19);
				headerLength = 19;
				header.setAddressType(addressType);
				header.setAddress(new String(hostBytes));
				header.setPort(portBytes);
				header.setHeaderLength(headerLength);
			}

		}

		return null;

	}

	/**
	 * 判断是否是ip地址
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isIpAddress(String ip) {
		Matcher m = IP_ADDRESS_PATTERN.matcher(ip);
		return m.find();
	}

	/**
	 * 把byte的数组转换为字符串的ip地址
	 * 
	 * @param host
	 * @return
	 */
	public static String byteToIp(byte[] host) {
		if (host == null || host.length != 4) {
			throw new IllegalArgumentException("the ip byte array is iilegal.");
		}
		StringBuilder ip = new StringBuilder();
		for (int i = 0; i < host.length; i++) {
			ip.append(host[i] & 0xFF);
			if (i != (host.length - 1)) {
				ip.append(".");
			}
		}
		return ip.toString();
	}

	/**
	 * 把byte数组形式的端口转换为int
	 * 
	 * @param port
	 * @return
	 */
	public static int byteToPort(byte[] port) {
		if (port == null || port.length != 2) {
			throw new IllegalArgumentException("the ip byte array is iilegal.");
		}
		String part0 = Integer.toHexString(port[0] & 0xFF);
		String part1 = Integer.toHexString(port[1] & 0xFF);
		return Integer.parseInt(part0 + part1, 16);
	}

	public static void close(InputStream clientIn) {
		if (clientIn != null) {
			try {
				clientIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(OutputStream clientOut) {
		if (clientOut != null) {
			try {
				clientOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void writeToSocketChannel(byte[] data,
			SocketChannel socketChannel) throws IOException {
		ByteBuffer dataBuf = ByteBuffer.wrap(data);
		while (dataBuf.remaining() > 0) {
			socketChannel.write(dataBuf);
		}
	}

	public static String array2String(byte[] ary) {
		StringBuilder strBld = new StringBuilder();
		for (byte b : ary) {
			strBld.append(b);
		}
		return strBld.toString();
	}

	/**
	 * 返回格式化JSON字符串。
	 * 
	 * @param json
	 *            未格式化的JSON字符串。
	 * @return 格式化的JSON字符串。
	 */
	public static String formatJson(String json) {
		StringBuffer result = new StringBuffer();
		int length = json.length();
		int number = 0;
		char key = 0;

		// 遍历输入字符串。
		for (int i = 0; i < length; i++) {
			// 1、获取当前字符。
			key = json.charAt(i);

			// 2、如果当前字符是前方括号、前花括号做如下处理：
			if ((key == '[') || (key == '{')) {
				// （1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
				if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
					result.append('\n');
					result.append(indent(number));
				}

				// （2）打印：当前字符。
				result.append(key);

				// （3）前方括号、前花括号，的后面必须换行。打印：换行。
				result.append('\n');

				// （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
				number++;
				result.append(indent(number));

				// （5）进行下一次循环。
				continue;
			}

			// 3、如果当前字符是后方括号、后花括号做如下处理：
			if ((key == ']') || (key == '}')) {
				// （1）后方括号、后花括号，的前面必须换行。打印：换行。
				result.append('\n');

				// （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
				number--;
				result.append(indent(number));

				// （3）打印：当前字符。
				result.append(key);

				// （4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
				if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
					result.append('\n');
				}

				// （5）继续下一次循环。
				continue;
			}

			// 4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
			if ((key == ',')) {
				result.append(key);
				result.append('\n');
				result.append(indent(number));
				continue;
			}

			// 5、打印：当前字符。
			result.append(key);
		}

		return result.toString();
	}

	/**
	 * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
	 * 
	 * @param number
	 *            缩进次数。
	 * @return 指定缩进次数的字符串。
	 */
	private static String indent(int number) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < number; i++) {
			result.append(" ");
		}
		return result.toString();
	}
	
	public static String defaultIfEmpty(String str, String defaultStr){
		if(str == null || str.length() == 0){
			return defaultStr;
		}
		return str;
	}
	
    public static boolean saveFile(String fn, String content) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(fn);
            writer.println(content);
            writer.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public static String getFileContent(String fn) {
        Path path = Paths.get(fn);
        String content = "";
        try {
            content = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            // do nothing
        }
        return content;
    }

	public static void main(String[] args) {
		isIpAddress("www.baidu.com");
		isIpAddress("192.168.1.1");

		byte[] ip = { 61, -121, -87, 105 };

		System.out.println(byteToIp(ip));

	}
}
