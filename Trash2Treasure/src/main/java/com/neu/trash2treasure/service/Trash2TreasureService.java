package com.neu.trash2treasure.service;

import java.sql.SQLException;
import java.util.List;

import com.neu.trash2treasure.beans.AdminSummary;
import com.neu.trash2treasure.beans.Category;
import com.neu.trash2treasure.beans.Item;
import com.neu.trash2treasure.beans.Login;
import com.neu.trash2treasure.beans.Price;
import com.neu.trash2treasure.beans.Seller;
import com.neu.trash2treasure.beans.User;
import com.neu.trash2treasure.dao.T2TDao;

public class Trash2TreasureService {
	private T2TDao dao = new T2TDao();

	public boolean registerUser(User userBean) throws SQLException {
		return dao.createUser(userBean);

	}

	public User validateUserLogin(Login loginBean) {
		User loginUser = null;
		try {
			loginUser = dao.validateUser(loginBean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loginUser;
	}

	public boolean isUserAdmin(Login loginBean) {
		return false;
	}

	public String getFirstName(Login loginBean) {
		return null;
	}

	public void uploadItem(Item itemBean) throws SQLException {
		
		dao.uploadNewItem(itemBean);

	}

	public List<Item> getSellerItems(boolean isAdmin, int nuID)
			throws SQLException {
		// TODO Auto-generated method stub

		List<Item> sellerItems = dao.getAllItemsForUser(nuID);

		return sellerItems;
	}

	public void deleteItem(int deleteItemId) throws SQLException {
		// TODO Auto-generated method stub

		dao.deleteItem(deleteItemId);

	}

	public Item getItemInformation(int updateItemId) throws SQLException {
		// TODO Auto-generated method stub

		return dao.getItem(updateItemId);
	}

	public void updateItemDetails(Item itemBean) throws SQLException {
		// TODO Auto-generated method stub
		dao.updateItem(itemBean);

	}

	public List<Item> getAllItems() throws SQLException {
		// TODO Auto-generated method stub

		List<Item> allItems = dao.itemsOfAllUsers();
		return allItems;
	}

	public List<Item> getFilteredItems(String sortBy, String sortCriteria) throws SQLException {
		// TODO Auto-generated method stub
		List<Item> filteredItems = null;
		if (sortBy.equals("sortByCategory")) {
			filteredItems = dao
					.itemsSortedByCategory(getCategory(sortCriteria));
		} else {
			filteredItems = dao.itemsSortedByPrice(getPrice(sortCriteria));
		}
		return filteredItems;
	}

	public List<Item> getPendingItems() throws SQLException {
		// TODO Auto-generated method stub
		List<Item> pendingItems = dao.getPendingItems();
		
		return pendingItems;
	}

	public User getUserProfile(int nuID, boolean isAdmin) throws SQLException {
		// TODO Auto-generated method stub
		
		User user = dao.queryUser(nuID);
		
		return user;
	}

	public boolean UpdateUserProfile(User userBean, int nuID, boolean isAdmin) throws SQLException {
		// TODO Auto-generated method stub
		
		dao.updateUserInformation(userBean);
		return false;
	}

	public Category getCategory(String category) {
		switch (category) {
		case "FURNITURE":
			return Category.FURNITURE;
		case "APPARELS":
			return Category.APPARELS;
		case "ELECTRONICS":
			return Category.ELECTRONICS;
		case "FOOTWEAR":
			return Category.FOOTWEAR;
		case "KITCHEN_AND_DINING":
			return Category.KITCHEN_AND_DINING;
		case "STATIONARY":
			return Category.STATIONARY;
		default:
			return Category.OTHERS;

		}

	}

	public Price getPrice(String price) {
		switch (price) {
		case "USD_1":
			return Price.USD_1;
		case "USD_10":
			return Price.USD_10;
		case "USD_15":
			return Price.USD_15;
		default:
			return Price.FREE;
		}

	}

	public void approvePendingItem(int itemId) throws SQLException {
		// TODO Auto-generated method stub
		dao.updatePendingItem(itemId, true);
	}
	
	public List<Item> getApprovedItems() throws SQLException {
		// TODO Auto-generated method stub
		return dao.getApprovedItems();
	}
	
	public double getAmountOwedByNUID(Long nuID) throws SQLException {
		// TODO Auto-generated method stub
		return dao.queryAmountOwedByNUID(nuID);
	}

	public void markAsSoldItem(int itemMarkAsSold) throws SQLException {
		// TODO Auto-generated method stub
		dao.updateItemSold(Long.valueOf(itemMarkAsSold));
	}

	public List<Seller> getPendingSettlementForSellers() throws SQLException {
		// TODO Auto-generated method stub
		return dao.getSellersWhoOweMoney();
	}

	public void settleThisSeller(Long nuID) throws SQLException {
		// TODO Auto-generated method stub
		dao.settleAmount(nuID);
	}

	public AdminSummary getAdminSummary() throws SQLException {
		// TODO Auto-generated method stub
		return dao.getAdminSummary();
	}

}