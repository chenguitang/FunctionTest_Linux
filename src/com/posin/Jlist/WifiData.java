package com.posin.Jlist;

import java.awt.Image;

import javax.swing.ImageIcon;

public class WifiData {

	private Image image; // Í¼Æ¬
	private ImageIcon imageIcon;
	private String wifiName; // wifiÃû×Ö
	private String wifiStatus; // wifi×´Ì¬

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}

	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}

	public String getWifiName() {
		return wifiName;
	}

	public void setWifiName(String wifiName) {
		this.wifiName = wifiName;
	}

	public String getWifiStatus() {
		return wifiStatus;
	}

	public void setWifiStatus(String wifiStatus) {
		this.wifiStatus = wifiStatus;
	}

}
