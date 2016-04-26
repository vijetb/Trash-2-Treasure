package com.neu.trash2treasure.beans;


public class Item {

	private Long id;
	private Long nuID;
	private String label;
	private Category category;
	private Price price;
	private String image;
	private String description;
	private Boolean approvalStatus;
	private Boolean saleStatus;
	private Boolean priceSettlementStatus;
	
	
	
	
	public Long getNuID() {
		return nuID;
	}
	public void setNuID(Long nuID) {
		this.nuID = nuID;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long long1) {
		this.id = long1;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Price getPrice() {
		return price;
	}
	public void setPrice(Price price) {
		this.price = price;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String itemImage) {
		this.image = itemImage;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(Boolean approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	public Boolean getSaleStatus() {
		return saleStatus;
	}
	public void setSaleStatus(Boolean saleStatus) {
		this.saleStatus = saleStatus;
	}
	public Boolean getPriceSettlementStatus() {
		return priceSettlementStatus;
	}
	public void setPriceSettlementStatus(Boolean priceSettlementStatus) {
		this.priceSettlementStatus = priceSettlementStatus;
	}
	@Override
	public String toString() {
		return "Item [id=" + id + ", nuID=" + nuID + ", label=" + label
				+ ", category=" + category + ", price=" + price + ", image="
				+ image + ", description=" + description + ", approvalStatus="
				+ approvalStatus + ", saleStatus=" + saleStatus
				+ ", priceSettlementStatus=" + priceSettlementStatus + "]";
	}
	
	
	
}
