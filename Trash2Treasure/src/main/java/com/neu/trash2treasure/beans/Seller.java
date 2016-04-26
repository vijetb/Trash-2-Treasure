package com.neu.trash2treasure.beans;

import java.util.ArrayList;

public class Seller extends User{

	private ArrayList<Item> itemsRegistered;
	private Double amountOwed;

	public ArrayList<Item> getItemsRegistered() {
		return itemsRegistered;
	}

	public void setItemsRegistered(ArrayList<Item> itemsRegistered) {
		this.itemsRegistered = itemsRegistered;
	}

	public Double getAmountOwed() {
		return amountOwed;
	}

	public void setAmountOwed(Double amountOwed) {
		this.amountOwed = amountOwed;
	}

}
