package com.nihao001.helloworld.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * config info
 * 
 * @author worldmaomao
 *
 */
public class Config {
	// buffer
	private int transferBufferSize;

	// proxy server info
	private String proxyServerIp;
	private int proxyServerPort;

	// client info
	private String clientListenIp;
	private int clientListenPort;

	// cipher info
	private String encryptMethod;
	private String encryptPassword;

	public int getTransferBufferSize() {
		return transferBufferSize;
	}

	public void setTransferBufferSize(int transferBufferSize) {
		this.transferBufferSize = transferBufferSize;
	}

	public String getProxyServerIp() {
		return proxyServerIp;
	}

	public void setProxyServerIp(String proxyServerIp) {
		this.proxyServerIp = proxyServerIp;
	}

	public int getProxyServerPort() {
		return proxyServerPort;
	}

	public void setProxyServerPort(int proxyServerPort) {
		this.proxyServerPort = proxyServerPort;
	}

	public String getClientListenIp() {
		return clientListenIp;
	}

	public void setClientListenIp(String clientListenIp) {
		this.clientListenIp = clientListenIp;
	}

	public int getClientListenPort() {
		return clientListenPort;
	}

	public void setClientListenPort(int clientListenPort) {
		this.clientListenPort = clientListenPort;
	}

	public String getEncryptMethod() {
		return encryptMethod;
	}

	public void setEncryptMethod(String encryptMethod) {
		this.encryptMethod = encryptMethod;
	}

	public String getEncryptPassword() {
		return encryptPassword;
	}

	public void setEncryptPassword(String encryptPassword) {
		this.encryptPassword = encryptPassword;
	}
	
	/**
	 * read json into config
	 * 
	 * 
	 * @param text
	 */
	public void loadFromString(String text){
		if(text == null || text.length() == 0){
			text = "{}";
		}
		JSONObject json = JSON.parseObject(text);
		this.transferBufferSize = Integer.valueOf(Utils.defaultIfEmpty(json.getString("transfer_buffer_size"), "8192"));
		this.proxyServerIp = Utils.defaultIfEmpty(json.getString("proxy_server_ip"), "");
		this.proxyServerPort = Integer.valueOf(Utils.defaultIfEmpty(json.getString("proxy_server_port"), "50011"));
		this.clientListenIp = Utils.defaultIfEmpty(json.getString("client_listen_ip"), "127.0.0.1");
		this.clientListenPort = Integer.valueOf(Utils.defaultIfEmpty(json.getString("client_listen_port"), "1080"));
		this.encryptMethod = Utils.defaultIfEmpty(json.getString("encrypt_method"), "aes-256-ofb");
		this.encryptPassword = Utils.defaultIfEmpty(json.getString("encrypt_password"), "123456");
	}
	
	public String generateJsonString(){
		JSONObject json = new JSONObject();
		json.put("transfer_buffer_size", String.valueOf(this.transferBufferSize));
		json.put("proxy_server_ip", this.proxyServerIp);
		json.put("proxy_server_port", String.valueOf(this.proxyServerPort));
		json.put("client_listen_ip", this.clientListenIp);
		json.put("client_listen_port", String.valueOf(this.clientListenPort));
		json.put("encrypt_method", this.encryptMethod);
		json.put("encrypt_password", this.encryptPassword);
		return Utils.formatJson(json.toJSONString());
	}
	
	public static void main(String[] args) {
		Config config = new Config();
		config.setClientListenIp("127.0.0.1");
		config.setClientListenPort(7011);
		config.setProxyServerIp("www.baidu.com");
		config.setProxyServerPort(501122);
		config.setEncryptMethod("table");
		config.setEncryptPassword("123456");
		System.out.println(config.generateJsonString());
	}

}
