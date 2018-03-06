package com.posin.Jlist;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

import com.posin.constant.WifiMessage;

public class FriListModel extends AbstractListModel {

	private ArrayList<WifiMessage> listWifi;// wifi¡–±Ì£ª

	public FriListModel(ArrayList<WifiMessage> listWfi) {
		this.listWifi = listWfi;
	}

	@Override
	public Object getElementAt(int index) {
		return listWifi.get(index);
	}

	@Override
	public int getSize() {
		return listWifi.size();
	}

}
