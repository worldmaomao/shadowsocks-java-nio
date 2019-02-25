package com.nihao001.helloworld.utils;


public class PackageHeader {
	private int addressType;
	private String address;
	private byte[] port;
	private int headerLength;
	
	
	public int getAddressType() {
		return addressType;
	}
	public void setAddressType(int addressType) {
		this.addressType = addressType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public byte[] getPort() {
		return port;
	}
	public void setPort(byte[] port) {
		this.port = port;
	}
	public int getHeaderLength() {
		return headerLength;
	}
	public void setHeaderLength(int headerLength) {
		this.headerLength = headerLength;
	}
	
	@Override
	public String toString() {
		return "PackageHeader [addressType=" + addressType + ", address="
				+ address + ", port=" + Utils.byteToPort(port) + ", headerLength=" + headerLength
				+ "]";
	}
}
