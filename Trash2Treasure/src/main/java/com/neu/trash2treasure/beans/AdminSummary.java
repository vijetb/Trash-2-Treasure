package com.neu.trash2treasure.beans;

/**
 * Bean class for Admin Summary. This class holds the values for
 * NoOfItems that are sold/ unsold/ pending for approval.
 * @author Vijet Badigannavar
 */
public class AdminSummary {
	/**
	 * Unsold items
	 */
	private int noItemsUnsold = 0;
	/**
	 * Sold items
	 */
	private int noItemsSold = 0;
	/**
	 * Pending items
	 */
	private int noItemsPending = 0;
	
	public int getNoItemsUnsold() {
		return noItemsUnsold;
	}
	public void setNoItemsUnsold(int noItemsUnsold) {
		this.noItemsUnsold = noItemsUnsold;
	}
	public int getNoItemsSold() {
		return noItemsSold;
	}
	public void setNoItemsSold(int noItemsSold) {
		this.noItemsSold = noItemsSold;
	}
	public int getNoItemsPending() {
		return noItemsPending;
	}
	public void setNoItemsPending(int noItemsPending) {
		this.noItemsPending = noItemsPending;
	}
	@Override
	public String toString() {
		return "AdminSummary [noItemsUnsold=" + noItemsUnsold + ", noItemsSold=" + noItemsSold + ", noItemsPending="
				+ noItemsPending + "]";
	}
	
	
	
	
}
