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

		// ����JLable������
		String text = "<html>" + wifiData.getWifiName() + "<br/>"
				+ wifiData.getWifiStatus() + " <html/>";
		setText(text);
		// ������Ϊ5�Ŀհױ߿�
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// ������ɫ
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		// ����JLable��ͼƬ
		ImageIcon imgicon = wifiData.getImageIcon();
		setIcon(imgicon);
		// ����JLable��ͼƬ������֮��ľ���
		setIconTextGap(30);

		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(true);

		return this;
	}
}
