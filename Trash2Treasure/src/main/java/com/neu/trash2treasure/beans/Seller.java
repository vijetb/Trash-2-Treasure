package com.neu.trash2treasure.beans;

import java.util.List;

public class Seller extends User{
	/**
	 * List of registered Items
	 */
	private List<Item> itemsRegistered;
	/**
	 * Amount Owed by the seller.
	 */
	private Double amountOwed;

	public List<Item> getItemsRegistered() {
		return itemsRegistered;
	}

	public void setItemsRegistered(List<Item> itemsRegistered) {
		this.itemsRegistered = itemsRegistered;
	}

	public Double getAmountOwed() {
		return amountOwed;
	}

	public void setAmountOwed(Double amountOwed) {
		this.amountOwed = amountOwed;
	}

}
