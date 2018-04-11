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

		// 设置JLable的文字
		String text = "<html>" + StringUtils.parseWifiName(wifiData.getSsid()) + "<br/>"
				+ wifiData.getStatus() + " <html/>";
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
		// ImageIcon imgicon = wifiData.getImageIcon();
		// wifi强度
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

		// 设置JLable的图片与文字之间的距离
		setIconTextGap(30);

		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(true);

		return this;
	}
}
