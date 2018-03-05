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
		// 把数据转换为user对象， 在AbstractListModel
		WifiData wifiData = (WifiData) value;
		// 中传过来的是一个user对象；
		/******* 设置JLable的文字 ******/
		String text = "<html>" + wifiData.getWifiName() + "<br/>"
				+ wifiData.getWifiStatus() + " <html/>";
		setText(text);// 设置JLable的文字
		/******* 设置JLable的图片 *****/
		// 得到此图标的 Image,然后创建此图像的缩放版本。
		// Image img = wifiData.getImage().getScaledInstance(50, 50,
		// Image.SCALE_DEFAULT);
		ImageIcon imgicon = wifiData.getImageIcon();
		setIcon(imgicon);// 设置JLable的图片
		setIconTextGap(30);// 设置JLable的图片与文字之间的距离

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
