package com.neu.trash2treasure.beans;

import java.util.List;

/**
 * Admin Bean that stors the list of items that the Admin can view.
 * @author Vijet Badigannavar
 */
public class Admin {

	private List<Item> listOfItems;

	public List<Item> getListOfItems() {
		return listOfItems;
	}

	public void setListOfItems(List<Item> listOfItems) {
		this.listOfItems = listOfItems;
	}
	
}
