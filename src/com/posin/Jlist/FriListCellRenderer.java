package com.posin.Jlist;

import java.awt.Component;
import java.awt.Image;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class FriListCellRenderer extends JLabel implements ListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		// ������ת��Ϊuser���� ��AbstractListModel
		WifiData wifiData = (WifiData) value;
		// �д���������һ��user����
		/******* ����JLable������ ******/
		String text = "<html>" + wifiData.getWifiName() + "<br/>"
				+ wifiData.getWifiStatus() + " <html/>";
		setText(text);// ����JLable������
		/******* ����JLable��ͼƬ *****/
		// �õ���ͼ��� Image,Ȼ�󴴽���ͼ������Ű汾��
//		Image img = wifiData.getImage().getScaledInstance(50, 50,
//				Image.SCALE_DEFAULT);
		ImageIcon imgicon = wifiData.getImageIcon();
		setIcon(imgicon);// ����JLable��ͼƬ
		setIconTextGap(30);// ����JLable��ͼƬ������֮��ľ���
		return this;
	}

}
