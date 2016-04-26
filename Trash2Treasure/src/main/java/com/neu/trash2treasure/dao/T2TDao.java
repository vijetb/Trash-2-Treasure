package com.neu.trash2treasure.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.neu.trash2treasure.beans.AdminSummary;
import com.neu.trash2treasure.beans.Category;
import com.neu.trash2treasure.beans.Item;
import com.neu.trash2treasure.beans.Login;
import com.neu.trash2treasure.beans.Price;
import com.neu.trash2treasure.beans.Seller;
import com.neu.trash2treasure.beans.User;

public class T2TDao {
	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost/trash2treasure";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "viji123";

	private static Connection connection = null;

	static {
		try {
			Class.forName(DRIVER_CLASS);
			connection = DriverManager.getConnection(DB_URL, DB_USERNAME,
					DB_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Database connected successfully..!");
	}

	public boolean createUser(User newUser) throws SQLException {

		if (newUser.getIsAdmin() && !("CS5200".equals(newUser.getAdminCode()))) {
			throw new SQLException();
		}

		String insertSellerQuery = "insert into user(fname,lname,emailId,nuId,"
				+ "phoneNo,isAdmin,securityQuestion_1,securityQuestionSolution_1,"
				+ "securityQuestion_2,securityQuestionSolution_2,userpwd) "
				+ " values(?,?,?,?,?,?,?,?,?,?,?);";

		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(insertSellerQuery,
						Statement.RETURN_GENERATED_KEYS);
		ppdStmt.setString(1, newUser.getFirstName());
		ppdStmt.setString(2, newUser.getLastName());
		ppdStmt.setString(3, newUser.getEmailId());
		ppdStmt.setString(4, String.valueOf(newUser.getNuID()));
		ppdStmt.setString(5, String.valueOf(newUser.getPhoneNumber()));
		if(newUser.getIsAdmin())
			ppdStmt.setInt(6, 1);
		else
			ppdStmt.setInt(6, 0);	
		ppdStmt.setString(7, newUser.getSecurityQuestion1());
		ppdStmt.setString(8, newUser.getSecurityAnswer1());
		ppdStmt.setString(9, newUser.getSecurityQuestion2());
		ppdStmt.setString(10, newUser.getSecurityAnswer2());
		ppdStmt.setString(11, newUser.getPassword());

		if (1 != ppdStmt.executeUpdate()) {
			throw new SQLException();
		}

		if (newUser.getIsAdmin()) {
			return true;
		}
		// also add the record to the seller table

		ResultSet generatedKeys = ppdStmt.getGeneratedKeys();
		generatedKeys.next();
		long generatedKey = generatedKeys.getLong(1);
		double amtOwed = 0.0;

		String insertIntoSellerQuery = "insert into seller values(?,?);";
		PreparedStatement ppdStmt1 = (PreparedStatement) connection
				.prepareStatement(insertIntoSellerQuery);
		ppdStmt1.setString(1, String.valueOf(generatedKey));
		ppdStmt1.setDouble(2, amtOwed);

		if (1 != ppdStmt1.executeUpdate()) {
			throw new SQLException();
		}

		return true;
	}

	// Check for login details
	public User validateUser(Login loginCredentails) throws SQLException {
		if (null == loginCredentails.getNuID()
				|| null == loginCredentails.getPassword()) {
			return null;
		}
		System.out.println("inside login dao");
		
		System.out.println("NUID: "+ loginCredentails.getNuID());
		System.out.println("Pwd: "+ loginCredentails.getPassword());
		
		User loginUser = null;
		String queryForLogin = "SELECT * from user u where u.nuId=? and u.userpwd=?;";
		PreparedStatement stmt = (PreparedStatement) connection
				.prepareStatement(queryForLogin);
		stmt.setString(1, loginCredentails.getNuID().toString());
		stmt.setString(2, loginCredentails.getPassword());
		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			loginUser = new User();
			try {
				loginUser.setId(Long.valueOf(resultSet.getString(1)));

				loginUser.setFirstName(resultSet.getString(2));
				;
				loginUser.setLastName(resultSet.getString(3));
				loginUser.setEmailId(resultSet.getString(4));
				loginUser.setNuID(Long.parseLong(resultSet.getString(5)));
				loginUser.setPhoneNumber(resultSet.getString(6));
				loginUser.setIsAdmin(resultSet.getBoolean(7));
				loginUser.setSecurityQuestion1(resultSet.getString(8));
				loginUser.setSecurityAnswer1(resultSet.getString(9));
				loginUser.setSecurityQuestion2(resultSet.getString(10));
				loginUser.setSecurityAnswer2(resultSet.getString(11));
				loginUser.setPassword(loginCredentails.getPassword());

				return loginUser;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error in retriving data from User table");
				return null;
			}
		}
		return null;
	}

	/**
	 * Check for valid credentials. Returns null, if the nuid doesnt exists or
	 * if its null.
	 * 
	 * @throws SQLException
	 */
	public List<String> forgotCredentials(String nuId) throws SQLException {
		List<String> securityQuestions = new ArrayList<String>();

		if (null != nuId && !nuId.isEmpty()) {
			String securityQuestionQuery = "Select u.securityQuestion_1, u.securityQuestion_2 "
					+ "from user u where u.nuId=?;";
			PreparedStatement stmt = (PreparedStatement) connection
					.prepareStatement(securityQuestionQuery);
			stmt.setString(1, nuId);
			ResultSet securityQuestionsResultSet = stmt.executeQuery();
			if (securityQuestionsResultSet.next()) {
				securityQuestions.add(securityQuestionsResultSet
						.getString("securityQuestion_1"));
				// securityQuestionsResultSet.next();
				securityQuestions.add(securityQuestionsResultSet
						.getString("securityQuestion_2"));
				return securityQuestions;
			}
			return null;
		}
		return null;
	}

	/**
	 * Check for solutions. if Returns null, if the nuid doesnt exists or if its
	 * null.
	 * 
	 * @throws SQLException
	 */
	public boolean validateSecurityQuestions(String s1, String s2, String nuId)
			throws SQLException {
		if (s1 != null && s2 != null && !s1.isEmpty() && !s2.isEmpty()) {
			String securityQuestion1Query = "Select u.securityQuestionSolution_1,u.securityQuestionSolution_2 "
					+ "from user u where u.nuId=?;";
			PreparedStatement stmt = (PreparedStatement) connection
					.prepareStatement(securityQuestion1Query);
			stmt.setString(1, nuId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return (s1.equals(rs.getString("securityQuestionSolution_1")) && s2
						.equals(rs.getString("securityQuestionSolution_2")));
			}
			return false;
		}
		return false;
	}

	// update user information:
	public User queryUser(long uId) throws SQLException {
		User user = null;
		Long userId = queryUserByNUID(uId);
		String queryUser = "select * from user u where u.id=?;";
		PreparedStatement stmt = (PreparedStatement) connection
				.prepareStatement(queryUser);
		stmt.setString(1, String.valueOf(userId));
		ResultSet resultSet = stmt.executeQuery();
		if (resultSet.next()) {
			user = new User();
			user.setId(Long.valueOf(resultSet.getString(1)));

			user.setFirstName(resultSet.getString(2));
			;
			user.setLastName(resultSet.getString(3));
			user.setEmailId(resultSet.getString(4));
			user.setNuID(Long.parseLong(resultSet.getString(5)));
			user.setPhoneNumber(resultSet.getString(6));
			user.setIsAdmin(resultSet.getBoolean(7));
			user.setSecurityQuestion1(resultSet.getString(8));
			user.setSecurityAnswer1(resultSet.getString(9));
			user.setSecurityQuestion2(resultSet.getString(10));
			user.setSecurityAnswer2(resultSet.getString(11));
			// user.setUserName(resultSet.getString(12));
			user.setPassword(resultSet.getString(12));
		}

		return user;
	}

	// update user information:
		public Long queryUserByNUID(long nuId) throws SQLException {
			User user = null;
			String queryUser = "select u.id from user u where u.nuId=?;";
			PreparedStatement stmt = (PreparedStatement) connection
					.prepareStatement(queryUser);
			stmt.setString(1, String.valueOf(nuId));
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getLong(1);
			}

			return -1L;
		}

		public Double queryAmountOwedByNUID(long nuId) throws SQLException {
			String queryUser = "select sum(i.price) from item i, user u where u.nuId = ? "
					+ "and i.userId = u.id and i.saleStatus =1 and i.priceSettlementStatus= 0;";
			PreparedStatement stmt = (PreparedStatement) connection
					.prepareStatement(queryUser);
			stmt.setString(1, String.valueOf(nuId));
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				System.out.println("inside amount owed present");
				return resultSet.getDouble(1);
			}

			return 0.00;
		}
		
	
	// Update the userinformation
	public int updateUserInformation(User user) throws SQLException {
		Long userId = queryUserByNUID(user.getNuID());
		String updateStmt = "update user u set"
				+ " u.fname=?,u.lname=?,u.emailId=?,u.phoneNo=?"
				+ " where u.id=?;";
		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(updateStmt);
		ppdStmt.setString(1, user.getFirstName());
		ppdStmt.setString(2, user.getLastName());
		ppdStmt.setString(3, user.getEmailId());
		ppdStmt.setString(4, user.getPhoneNumber());
		ppdStmt.setLong(5, userId);// (12, String.valueOf(user.getId()));
		return ppdStmt.executeUpdate();
	}

	// create new user

	// user uploads an item
	public int uploadNewItem(Item item) throws SQLException {
		if (item != null) {
			Long userId = queryUserByNUID(item.getNuID());
			String uploadItemQuery = "insert into "
					+ "item(userId,label,category,price,image,description,approvalStatus,saleStatus,priceSettlementStatus) "
					+ "values(?,?,?,?,?,?,?,?,?);";
			PreparedStatement ppdStmt = (PreparedStatement) connection
					.prepareStatement(uploadItemQuery);
			ppdStmt.setString(1, String.valueOf(userId));
			ppdStmt.setString(2, item.getLabel());
			ppdStmt.setString(3, item.getCategory().name());
			ppdStmt.setString(4, item.getPrice().name());
			ppdStmt.setString(5, item.getImage());
			ppdStmt.setString(6, item.getDescription());
			ppdStmt.setBoolean(7, item.getApprovalStatus());
			ppdStmt.setBoolean(8, item.getSaleStatus());
			ppdStmt.setBoolean(9, item.getPriceSettlementStatus());
			return ppdStmt.executeUpdate();
		}
		return 0;
	}

	// delete an item from the item table
	public int deleteItem(long itemId) throws SQLException {
		// Check if the item is approved, if so return -1
		if (checkIfItemIsApproved(itemId)) {
			return -1;
		}
		String deleteQuery = "delete from item where item.id = ?;";
		PreparedStatement ppdStmtToDelItem = (PreparedStatement) connection
				.prepareStatement(deleteQuery);
		ppdStmtToDelItem.setString(1, String.valueOf(itemId));
		return ppdStmtToDelItem.executeUpdate();
	}

	// update an item from the item table
	public int updateItem(Item item) throws SQLException {
		// Check if the item is approved, if so return -1
		if (checkIfItemIsApproved(item.getId())) {
			return -1;
		}

		String updateString = "update item i set i.label=?,i.category=?,i.price=?,i.image=?,i.description=? where i.id=?;";
		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(updateString);
		ppdStmt.setString(1, item.getLabel());
		ppdStmt.setString(2, item.getCategory().name());
		ppdStmt.setString(3, item.getPrice().name());
		ppdStmt.setString(4, item.getImage());
		ppdStmt.setString(5, item.getDescription());
		ppdStmt.setString(6, String.valueOf(item.getId()));

		return ppdStmt.executeUpdate();
	}

	/**
	 * Returns the Items associated with the UserId
	 * 
	 * @param userId
	 * @return null if there are no items
	 * @throws SQLException
	 */
	public List<Item> getAllItemsForUser(long nuID) throws SQLException {
		Long userId = queryUserByNUID(nuID);
		String userItemString = "select * from item i where i.userId = ?";
		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(userItemString);
		ppdStmt.setString(1, String.valueOf(userId));
		return fetchItemsOfUserOrAll(ppdStmt);
	}

	// Returns all the items
	public List<Item> itemsOfAllUsers() throws SQLException {
		String userItemString = "select * from item i order by i.approvalStatus";
		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(userItemString);
		return fetchItemsOfUserOrAll(ppdStmt);
	}
	
	public List<Item> getApprovedItems() throws SQLException {
		String userItemString = "select * from item i where i.approvalStatus=? and i.saleStatus=?";
		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(userItemString);
		ppdStmt.setInt(1,1);
		ppdStmt.setInt(2,0);
		return fetchItemsOfUserOrAll(ppdStmt);
	}

	// Returns all the items of specific category
	public List<Item> itemsSortedByCategory(Category category)
			throws SQLException {
		String categoryString = "select * from item i where i.category=? order by i.approvalStatus;";
		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(categoryString);
		ppdStmt.setString(1, category.name());
		return fetchItemsOfUserOrAll(ppdStmt);
	}

	// returns all the items of specific price
	public List<Item> itemsSortedByPrice(Price price) throws SQLException {
		String categoryString = "select * from item i where i.price=? order by i.approvalStatus;";
		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(categoryString);
		ppdStmt.setString(1, price.name());
		return fetchItemsOfUserOrAll(ppdStmt);
	}

	// generic function to fetch
	private List<Item> fetchItemsOfUserOrAll(PreparedStatement ppdStmt)
			throws SQLException {
		List<Item> items = new ArrayList<Item>();
		ResultSet rs = ppdStmt.executeQuery();
		while (rs.next()) {
			Item tempItem = new Item();
			tempItem.setId(Long.valueOf(String.valueOf(rs.getString(1))));
			tempItem.setNuID(Long.valueOf(String.valueOf(rs.getString(2))));
			tempItem.setLabel(rs.getString(3));
			tempItem.setCategory(Category.valueOf(rs.getString(4)));
			tempItem.setPrice(Price.valueOf(rs.getString(5)));
			 
			tempItem.setImage(rs.getString(6));
			tempItem.setDescription(rs.getString(7));
			tempItem.setApprovalStatus(rs.getBoolean(8));
			tempItem.setSaleStatus(rs.getBoolean(9));
			tempItem.setPriceSettlementStatus(rs.getBoolean(10));

			items.add(tempItem);
		}
		System.out.println("Items Count: "+items.size());

		if (items.isEmpty()) {
			return null;
		}
		return items;
	}

	private boolean checkIfItemIsApproved(long itemId) throws SQLException {
		boolean isItemApproved = false;

		String checkIfItemIsApproved = "select i.approvalStatus from item i where i.id = ?";
		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(checkIfItemIsApproved);
		ppdStmt.setString(1, String.valueOf(itemId));
		ResultSet rs = ppdStmt.executeQuery();
		if (rs.next()) {
			isItemApproved = rs.getBoolean("approvalStatus");
		}
		return isItemApproved;
	}

	/* ADMIN WORKS */

	/**
	 * REturns all the items that are not arrroved. null if there are no items
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Item> getPendingItems() throws SQLException {
		String categoryString = "select * from item i where i.approvalStatus='0'";
		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(categoryString);
		return fetchItemsOfUserOrAll(ppdStmt);
	}

	// APPROVES THE PENDING REQUESTS
	public int updatePendingItem(long itemId, boolean approvalStatus)
			throws SQLException {
		String updatePendingItemString = "update item i set i.approvalStatus = ? where i.id=?;";
		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(updatePendingItemString);
		ppdStmt.setBoolean(1, approvalStatus);
		ppdStmt.setString(2, String.valueOf(itemId));
		return ppdStmt.executeUpdate();
	}

	// Retrive Admin Summary


	public AdminSummary getAdminSummary() throws SQLException { AdminSummary
		adminSummary = null; int itemsUnsold = 0; int itemsSold = 0; int
		itemsPendingApproval = 0;

		String itemsUnsoldQuery =
				"select count(*) from item i where i.saleStatus=?;"; PreparedStatement
				ppdStmt = (PreparedStatement) connection
				.prepareStatement(itemsUnsoldQuery); 
				ppdStmt.setInt(1, 0); 
				ResultSet rs =
				ppdStmt.executeQuery(); 
				if (rs.next()) { 
					itemsUnsold = rs.getInt(1); 
					}
				else {
					return null; 
					}

				ppdStmt.setInt(1, 1);// items sold 
				ResultSet rsForSoldItems =
				ppdStmt.executeQuery(); 
				if (rsForSoldItems.next()) { 
					itemsSold =
							rsForSoldItems.getInt(1); 
					}

				String itemsPendingQuery =
						"select count(*) from item i where i.approvalStatus=0;";
				PreparedStatement ppdStmt1 = (PreparedStatement) connection
						.prepareStatement(itemsPendingQuery); ResultSet rs2 =
						ppdStmt1.executeQuery(); 
						if (rs2.next()) { 
							itemsPendingApproval = rs2.getInt(1); 
						}

						adminSummary = new AdminSummary();
						adminSummary.setNoItemsPending(itemsPendingApproval);
						adminSummary.setNoItemsSold(itemsSold);
						adminSummary.setNoItemsUnsold(itemsUnsold);
						return adminSummary; 
						}

	// TODO: // ITEM IS SOLD: // 1. update the status of the item, // 2.
	//update the corrosponding amount to the user account /**
	 /* 
	 * @param id
	 * 
	 * @return -1 indicates items doesnot exist/error in execution. 0 indicates
	 * item is not approved.
*/
	public int updateItemSold(long itemId) throws SQLException {
		// check if the item is approved.
		String itemCheckQuery = "select i.approvalStatus from item i where i.id=?;";
		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(itemCheckQuery);
		ppdStmt.setString(1, String.valueOf(itemId));
		ResultSet rs1 = ppdStmt.executeQuery();
		if (rs1.next()) {
			int status = rs1.getInt(1);
			if (status == 0) {
				return 0;
			}
		} else {
			return -1;
		}
		// update the status of the item.
		String updateItemStatusQuery = "update item i set i.saleStatus = 1 where i.id = ?;";
		PreparedStatement ppdStmt1 = (PreparedStatement) connection
				.prepareStatement(updateItemStatusQuery);
		ppdStmt1.setString(1, String.valueOf(itemId));
		int rs2 = ppdStmt1.executeUpdate();
		if (rs2 != 1) {
			return -1;
		}
		//
		String priceOfItemQuery = "select price from item i where i.id = ?;";
		PreparedStatement pdStmt = (PreparedStatement) connection
				.prepareStatement(priceOfItemQuery);
		pdStmt.setString(1, String.valueOf(itemId));

		ResultSet rsPrice = pdStmt.executeQuery();
		String itemPrice = null;
		if (rsPrice.next()) {
			itemPrice = rsPrice.getString(1);
		} else {
			return 1;
		}

		// update the user
		String getUserID = "select u.id from user u,item i where i.userId = u.id and i.id=?";
				PreparedStatement ppdStmt2 = (PreparedStatement) connection
				.prepareStatement(getUserID);
				ppdStmt2.setLong(1, itemId);
				
				ResultSet rs = ppdStmt2.executeQuery();
				rs.next();
				int userId = rs.getInt(1);
				
				
				
		String updateSellerAmountQuery = "update seller s set s.amountOwed= (? + s.amountOwed) where s.id=?;";
		ppdStmt2 = (PreparedStatement) connection
				.prepareStatement(updateSellerAmountQuery);

		// get price of item
		Price price = null;
		ppdStmt2.setInt(1, getAmountFromPrice(Price.valueOf(itemPrice)));
		ppdStmt2.setInt(2, userId);

		int rs3 = ppdStmt2.executeUpdate();
		if (rs3 != 1) {
			return -1;
		}
		return rs3;
	}

	private int getAmountFromPrice(Price price) {
		switch (price) {
		case USD_1:
			return 1;
		case USD_5:
			return 5;
		case USD_10:
			return 10;
		case USD_15:
			return 15;
		default:
			return 0;
		}
	}

	/**
	 * 
	 * REturns the item from table Item corresponding to given itemId
	 * 
	 * @throws SQLException
	 */

	public Item getItem(long itemId) throws SQLException {

		Item fetchedItem = null;

		String itemQuery = "select * from item i where i.id = ?;";

		PreparedStatement ppdStmt = (PreparedStatement) connection
				.prepareStatement(itemQuery);

		ppdStmt.setString(1, String.valueOf(itemId));

		ResultSet rs = ppdStmt.executeQuery();

		if (rs.next()) {

		fetchedItem = new Item();

			fetchedItem.setId(Long.valueOf(rs.getString(1)));

			fetchedItem.setNuID(Long.valueOf(rs.getString(2)));

			fetchedItem.setLabel(rs.getString(3));

			fetchedItem.setCategory(Category.valueOf(rs.getString(4)));

			fetchedItem.setPrice(Price.valueOf(rs.getString(5)));

			fetchedItem.setImage(rs.getString(6));
			fetchedItem.setDescription(rs.getString(7));

			fetchedItem.setApprovalStatus(rs.getBoolean(8));

			fetchedItem.setSaleStatus(rs.getBoolean(9));

			fetchedItem.setPriceSettlementStatus(rs.getBoolean(10));

			return fetchedItem;

		}

		return null;

	}
	

	
	public List<Seller> getSellersWhoOweMoney() throws SQLException{
		List<Seller> sellerList = new ArrayList<Seller>();
		String stmt = "select * from user u, seller s where s.id = u.id and not s.amountOwed = 0 and u.isAdmin = 0;";
		PreparedStatement ppdStmt = (PreparedStatement) connection.prepareStatement(stmt);
		
		ResultSet rs = ppdStmt.executeQuery();
		while(rs.next()){
			
			Seller tempSeller = new Seller();
			tempSeller.setId(Long.valueOf(rs.getString(1)));
			tempSeller.setFirstName(rs.getString(2));
			tempSeller.setLastName(rs.getString(3));
			tempSeller.setEmailId(rs.getString(4));
			tempSeller.setNuID(Long.valueOf(rs.getString(5)));
			tempSeller.setPassword(rs.getString(12));
			tempSeller.setPhoneNumber(rs.getString(6));
			tempSeller.setIsAdmin(rs.getBoolean(7));
			tempSeller.setSecurityAnswer1(rs.getString(9));
			tempSeller.setSecurityQuestion1(rs.getString(8));
			tempSeller.setSecurityAnswer2(rs.getString(11));
			tempSeller.setSecurityQuestion2(rs.getString(10));
			tempSeller.setAmountOwed(rs.getDouble(14));
			sellerList.add(tempSeller);
		}
		
		return sellerList;
	}
	
	public Seller getSellerByNUId(long nuId) throws SQLException{
		
		Seller tempSeller = null;
		String stmt = "select * from user u, seller s where s.id = u.id and s.amountOwed != 0 and u.nuId = ?;";
		PreparedStatement ppdStmt = (PreparedStatement) connection.prepareStatement(stmt);
		ppdStmt.setString(1, String.valueOf(nuId));
		
		ResultSet rs = ppdStmt.executeQuery();
		if(rs.next()){
			tempSeller = new Seller();
			tempSeller.setId(Long.valueOf(rs.getString(1)));
			tempSeller.setFirstName(rs.getString(2));
			tempSeller.setLastName(rs.getString(3));
			tempSeller.setEmailId(rs.getString(4));
			tempSeller.setNuID(Long.valueOf(rs.getString(5)));
			tempSeller.setPassword(rs.getString(12));
			tempSeller.setPhoneNumber(rs.getString(6));
			tempSeller.setIsAdmin(rs.getBoolean(7));
			tempSeller.setSecurityAnswer1(rs.getString(9));
			tempSeller.setSecurityQuestion1(rs.getString(8));
			tempSeller.setSecurityAnswer2(rs.getString(11));
			tempSeller.setSecurityQuestion2(rs.getString(10));
			tempSeller.setAmountOwed(rs.getDouble(14));
		}
		return tempSeller;
	}
	
	public boolean settleAmount(Long nuId) throws SQLException{
		
		// get the userId for this NUId,
		long userId;
		PreparedStatement ppdStmt = connection.prepareStatement("select u.id from user u where u.nuId = ?;");
		ppdStmt.setString(1, String.valueOf(nuId));
		ResultSet rs = ppdStmt.executeQuery();
		if(rs.next()){
			userId = Long.valueOf(rs.getString(1));
		}else{
			return false;
		}
		// update the seller owed to 0;
		PreparedStatement ppdStmt1 = connection.prepareStatement("update seller s set s.amountOwed = 0 where s.id = ?;");
		ppdStmt1.setString(1, String.valueOf(userId));
		int status =  ppdStmt1.executeUpdate();
		if(status != 1){
			return false;
		}
		// update all the items of the seller as settled.
		
		PreparedStatement ppdStmt2 = connection.prepareStatement("update item i set i.priceSettlementStatus = 1 where i.userId = ? and i.priceSettlementStatus = 0;");
		ppdStmt2.setString(1, String.valueOf(userId));
		int statusUpdate =  ppdStmt2.executeUpdate();
		if(statusUpdate != 1){
			return false;
		}
		
		return true;
	}

}
