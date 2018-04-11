package com.posin.Jlist;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.posin.constant.WifiMessage;
import com.posin.utils.StringUtils;

public class FriListCellRenderer extends JLabel implements ListCellRenderer {

	private Icon[] mIcons = null;

	public FriListCellRenderer(Icon[] icons) {
		this.mIcons = icons;
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		WifiMessage wifiData = (WifiMessage) value;
		// WifiData wifiData = listWifiDatas.get(index);

		// ����JLable������
		String text = "<html>" + StringUtils.parseWifiName(wifiData.getSsid()) + "<br/>"
				+ wifiData.getStatus() + " <html/>";
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
		// ImageIcon imgicon = wifiData.getImageIcon();
		// wifiǿ��
		int wifiSignalLevel = wifiData.getSignalLevel();
		if (wifiSignalLevel >= 85) {
			setIcon(mIcons[4]);
		} else if ((wifiSignalLevel < 85) && (wifiSignalLevel >= 65)) {
			setIcon(mIcons[3]);
		} else if ((wifiSignalLevel < 65) && (wifiSignalLevel >= 55)) {
			setIcon(mIcons[2]);
		} else if ((wifiSignalLevel < 55) && (wifiSignalLevel >= 45)) {
			setIcon(mIcons[1]);
		}else{
			setIcon(mIcons[0]);
		}

		// ����JLable��ͼƬ������֮��ľ���
		setIconTextGap(30);

		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(true);

		return this;
	}
}
