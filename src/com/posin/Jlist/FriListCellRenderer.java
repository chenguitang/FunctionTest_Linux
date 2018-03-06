package com.posin.Jlist;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class FriListCellRenderer extends JLabel implements ListCellRenderer {

//	private ArrayList<WifiData> listWifiDatas = null;
//
//	public FriListCellRenderer(ArrayList<WifiData> listWifiDatas) {
//		this.listWifiDatas = listWifiDatas;
//	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		 WifiData wifiData = (WifiData) value;
//		WifiData wifiData = listWifiDatas.get(index);

		// 设置JLable的文字
		String text = "<html>" + wifiData.getWifiName() + "<br/>"
				+ wifiData.getWifiStatus() + " <html/>";
		setText(text);
		// 加入宽度为5的空白边框
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// 背景颜色
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		// 设置JLable的图片
		ImageIcon imgicon = wifiData.getImageIcon();
		setIcon(imgicon);
		// 设置JLable的图片与文字之间的距离
		setIconTextGap(30);

		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(true);

		return this;
	}
}
