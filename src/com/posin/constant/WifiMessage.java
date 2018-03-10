package com.posin.constant;

import java.io.Serializable;

public class WifiMessage implements Comparable<WifiMessage>, Serializable {

	private String macAddress; // MAC��ַ
	private String frequency; // wifiƵ��
	private int signalLevel; // wifiǿ��
	private String flags; // ���ܷ�ʽ
	private String ssid; // wifi����
	private String status; // wifi״̬
	private String ipAddresss; // wifi��ַ

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
