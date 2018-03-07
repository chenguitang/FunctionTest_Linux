package com.posin.constant;

public class WifiMessage {

	private String macAddress; // MAC��ַ
	private String frequency; // wifiƵ��
	private String signalLevel; // wifiǿ��
	private String flags; // ���ܷ�ʽ
	private String ssid; // wifi����
	private String status; // wifi״̬

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

	public String getSignalLevel() {
		return signalLevel;
	}

	public void setSignalLevel(String signalLevel) {
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

	@Override
	public String toString() {
		return "WifiMessage [macAddress=" + macAddress + ", frequency="
				+ frequency + ", signalLevel=" + signalLevel + ", flags="
				+ flags + ", ssid=" + ssid + ", status=" + status + "]";
	}
	
	

}
