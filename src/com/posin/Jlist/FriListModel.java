package com.posin.Jlist;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

public class FriListModel extends AbstractListModel {

	private ArrayList<WifiData> listWifi;// wifi¡–±Ì£ª

	public FriListModel(ArrayList<WifiData> listWfi) {
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
