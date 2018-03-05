package com.posin.Jlist;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class FriListCellRenderer extends JLabel implements ListCellRenderer {

	private Graphics mGraphics = null;

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
		// Image img = wifiData.getImage().getScaledInstance(50, 50,
		// Image.SCALE_DEFAULT);
		ImageIcon imgicon = wifiData.getImageIcon();
		setIcon(imgicon);// ����JLable��ͼƬ
		setIconTextGap(30);// ����JLable��ͼƬ������֮��ľ���

		// list.setCellRenderer(new DefaultListCellRenderer() {
		// public void paintComponent(Graphics g) {
		// super.paintComponent(g);
		// g.setColor(Color.red);
		// g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
		// }
		// });

//		if (mGraphics != null) {
//			System.out.println("111111111111111111");
//			mGraphics.setColor(Color.red);
//			mGraphics.drawLine(0, 100, 10, 0);
//		} else {
//			System.out.println("22222222222222222");
//		}
		return this;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		this.mGraphics = g;
	}
	
	@Override
	public Dimension getSize() {
//		return super.getSize();
		return new Dimension(1920,200);
	}

}
