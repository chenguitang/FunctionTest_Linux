package com.posin.Jlist;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import com.posin.constant.WifiMessage;


public class MyDefaultListModel extends DefaultListModel {
	
	
	private ArrayList<WifiMessage> listWifi;// wifi¡–±Ì£ª

	public MyDefaultListModel(ArrayList<WifiMessage> listWfi) {
		this.listWifi = listWfi;
	}
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return listWifi.size();
	}

	@Override
	public Object getElementAt(int index) {
		// TODO Auto-generated method stub
		return listWifi.get(index);
	}
	
}
