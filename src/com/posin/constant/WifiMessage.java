package com.posin.constant;

import java.io.Serializable;

public class WifiMessage implements Comparable<WifiMessage>, Serializable {

	private String macAddress; // MAC地址
	private String frequency; // wifi频率
	private int signalLevel; // wifi强度
	private String flags; // 加密方式
	private String ssid; // wifi名字
	private String status; // wifi状态
	private String ipAddresss; // wifi地址
	
	

	public WifiMessage(String macAddress, String frequency, int signalLevel,
			String flags, String ssid, String status, String ipAddresss) {
		super();
		this.macAddress = macAddress;
		this.frequency = frequency;
		this.signalLevel = signalLevel;
		this.flags = flags;
		this.ssid = ssid;
		this.status = status;
		this.ipAddresss = ipAddresss;
	}

	public WifiMessage() {
		
	}
	
	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public int getSignalLevel() {
		return signalLevel;
	}

	public void setSignalLevel(int signalLevel) {
		this.signalLevel = signalLevel;
	}

	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIpAddresss() {
		return ipAddresss;
	}

	public void setIpAddresss(String ipAddresss) {
		this.ipAddresss = ipAddresss;
	}

	@Override
	public String toString() {
		return "WifiMessage [macAddress=" + macAddress + ", frequency="
				+ frequency + ", signalLevel=" + signalLevel + ", flags="
				+ flags + ", ssid=" + ssid + ", status=" + status + "]";
	}

	@Override
	public int compareTo(WifiMessage wifiMessage) {
		return (this.signalLevel < wifiMessage.signalLevel ? -1
				: (this.signalLevel == wifiMessage.signalLevel ? 0 : 1));
	}

}
