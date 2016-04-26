package com.neu.trash2treasure.controller;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.neu.trash2treasure.beans.AdminSummary;
import com.neu.trash2treasure.beans.Item;
import com.neu.trash2treasure.beans.Login;
import com.neu.trash2treasure.beans.Seller;
import com.neu.trash2treasure.beans.User;
import com.neu.trash2treasure.service.Trash2TreasureService;

/** 
 * Common Controller to intercept all the clients connections
 * @author Vijet Badigannavar
 **/
@Controller
@MultipartConfig
public class Trash2TreasureController {

	/**Memory constants **/
	private static final int MAX_MEMORY_SIZE = 1024 * 1024 * 10;
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 10;
	
	/** Image Count*/
	private static int imageCount = 0;

	/** Registers the new User to the system */
	@RequestMapping("/userRegister")
	protected ModelAndView userRegistration(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		ModelAndView mv = new ModelAndView("Registration");
		return mv;
	}

	/* Redirect to Login page */
	@RequestMapping("/login")
	protected ModelAndView userLogin(HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		ModelAndView mv = new ModelAndView("Login");
		return mv;
	}
	
	/**
	 * Redirects back to the home page 
	 */
	@RequestMapping("/Home/{userInfo}")
	protected ModelAndView homeRedirect(@PathVariable("userInfo") String userInfo) throws Exception {
		ModelAndView mv = null;
		if(null==userInfo)
			mv = new ModelAndView("Login");
		
		if(! userInfo.contains("__")){
			return null;
		}
		
		int nuId = Integer.parseInt(userInfo.split("__")[1]);
		if(userInfo.contains("Admin")){
			mv = new ModelAndView("Admin");
			mv.addObject("userInfo", userInfo);
			mv.addObject("userIdentification", "verified");
		}else if(userInfo.contains("Seller")){
			Double amountOwed =0.0;
			Trash2TreasureService service = new Trash2TreasureService();
			try{
				amountOwed = service.getAmountOwedByNUID((long) nuId);
			}catch(Exception e){
				mv = new ModelAndView("Seller");
				mv.addObject("userInfo", userInfo);
				mv.addObject("userIdentification", "verified");
				mv.addObject("amountOwed","NO DATA AVAILABLE");
			}
			mv = new ModelAndView("Seller");
			mv.addObject("userInfo", userInfo);
			mv.addObject("amountOwed","$"+amountOwed);
			mv.addObject("userIdentification", "verified");
		}
			
		return mv;
	}

	/**
	 * Logout the user.
	 */
	@RequestMapping("/Logout/{userInfo}")
	protected ModelAndView logout(@PathVariable("userInfo") String userInfo) throws Exception {
		ModelAndView mv =null;
		mv = new ModelAndView("Login");
		return mv;
	}

	
	/** User submits a new User registration request. 
	 * The registration details are pushed to the database.  */
	@RequestMapping(value = "/registrationSubmission", method = RequestMethod.POST)
	public ModelAndView registrationSubmission(HttpServletRequest req, HttpServletResponse res, @ModelAttribute("userBean") User userBean) {
		try {
			new Trash2TreasureService().registerUser(userBean);
		} catch (Exception e) {
			e.printStackTrace();
			ModelAndView mv = new ModelAndView("Registration");
			mv.addObject("errorMsg", "Oops,something gone wrong!! Please try again");
			return mv;
		}

		ModelAndView mv = null;
		if (userBean.getIsAdmin()) {
			mv = new ModelAndView("Admin");
		} else
			mv = new ModelAndView("Seller");
		String isAdmin = (userBean.getIsAdmin() ? "Admin" : "Seller");
		String userInfo = isAdmin+ "__" + userBean.getNuID();
		mv.addObject("userInfo", userInfo);
		mv.addObject("amountOwed","$"+0);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;

	}

	/** Validate the user*/
	@RequestMapping(value = "/loginUser")
	public ModelAndView loginUser(HttpServletRequest req,
			HttpServletResponse res,
			@ModelAttribute("loginBean") Login loginBean) {
		// verify and login
		ModelAndView mv = null;
		double amountOwed=0.0;
		User user = null;
		Trash2TreasureService service = new Trash2TreasureService();
		try {
			user = service.validateUserLogin(loginBean);
		} catch (Exception ex) {
			mv = new ModelAndView("Login");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again.Check your NuId and Password..!!");
			return mv;
		}

		if (null==user) {
			mv = new ModelAndView("Login");
			mv.addObject("msg", "Oops..Something went wrong..Please try again.Check your NuId and Password..!!");
			return mv;
		} else {
			Boolean isAdmin = user.getIsAdmin();
			String firstName = user.getFirstName();
			String viewName = "";
			if (isAdmin)
				viewName = "Admin";
			else
				viewName = "Seller";
			
			if(viewName.equals("Seller"))
				try {
					amountOwed = service.getAmountOwedByNUID(user.getNuID());
				} catch (SQLException e) {
					e.printStackTrace();
					mv = new ModelAndView("Login");
					mv.addObject("msg","Oops..Something went wrong..Please try again.Check your NuId and Password..!!");
					return mv;
				}
				mv = new ModelAndView(viewName);
				String userInfo = (isAdmin ? "Admin" : "Seller") + "__" + loginBean.getNuID();
				mv.addObject("userInfo", userInfo);
				if(viewName.equals("Seller")){
					if(amountOwed==0)
						amountOwed=0;
					mv.addObject("amountOwed","$"+amountOwed);
				}
					
			mv.addObject("username", firstName);
			mv.addObject("userIdentification", "verified");
		}
		return mv;
	}

	/** Updates the User Profile */
	@RequestMapping(value = "/UpdateProfile/{userInfo}")
	public ModelAndView uploadUserProfile(HttpServletRequest req, HttpServletResponse res,@PathVariable("userInfo") String userInfo){
		ModelAndView mv = null;
		Trash2TreasureService service = new Trash2TreasureService();
		Double amountOwed=0.0;
		boolean isAdmin = (userInfo.split("__")[0]).equals("Admin") ? true
				: false;
		int nuID = Integer.parseInt(userInfo.split("__")[1]);
		String viewName = "";
		if (isAdmin)
			viewName = "Admin";
		else
			viewName = "Seller";
		
		User userBean  = null;
		try {
			userBean = service.getUserProfile(nuID, isAdmin);
			if(viewName.equals("Seller"))
				amountOwed = service.getAmountOwedByNUID(Long.valueOf(nuID));
		} catch (SQLException e) {
			e.printStackTrace();
			mv = new ModelAndView(viewName);
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			if(viewName.equals("Seller"))
				mv.addObject("amountOwed",amountOwed);
			mv.addObject("userIdentification", "verified");
			return mv;
		}
		mv = new ModelAndView("UserProfileUpdate");
		mv.addObject("userBean", userBean);
		mv.addObject("userInfo", userInfo);
		if(viewName.equals("Seller")){
			if(amountOwed==0)
				amountOwed=0.0;
		
			mv.addObject("amountOwed","$"+amountOwed);
		}
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;

	}

	/** Updates user information  */
	@RequestMapping(value = "/UpdateUserInformation")
	public ModelAndView updateUserInformation(HttpServletRequest req,
			HttpServletResponse res, @ModelAttribute("userBean") User userBean){

		String userInfo = req.getParameter("userInfo");
		Trash2TreasureService service = new Trash2TreasureService();
		Double amountOwed=0.0;
		boolean isAdmin = (userInfo.split("__")[0]).equals("Admin") ? true
				: false;
		int nuID = Integer.parseInt(userInfo.split("__")[1]);

		ModelAndView mv = null;
		String viewName = "";
		if (isAdmin)
			viewName = "Admin";
		else
			viewName = "Seller";

		try {
			userBean.setNuID(Long.valueOf(nuID));
			service.UpdateUserProfile(userBean, nuID,isAdmin);
			if(viewName.equals("Seller"))
				amountOwed = service.getAmountOwedByNUID(Long.valueOf(nuID));
		} catch (SQLException e) {
			e.printStackTrace();
			mv = new ModelAndView(viewName);
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			if(viewName.equals("Seller")){
				if(amountOwed==0)
					amountOwed=0.0;
			
				mv.addObject("amountOwed","$"+amountOwed);
			}
			mv.addObject("userIdentification", "verified");
			return mv;
		}
		
		mv = new ModelAndView(viewName);

		mv.addObject("msg", "Profile Information updated successfully..!!");
		mv.addObject("userInfo", userInfo);
		if(viewName.equals("Seller"))
			mv.addObject("amountOwed",amountOwed);
		mv.addObject("userIdentification", "verified");
		return mv;
	}

	/** Seller Uploads an item to his inventory for sale*/
	@RequestMapping(value = "/UploadItem/{userInfo}")
	public ModelAndView uploadItem(HttpServletRequest req, HttpServletResponse res, @PathVariable("userInfo") String userInfo){
		ModelAndView mv = new ModelAndView("UploadItem");
		mv.addObject("userInfo", userInfo);
		mv.addObject("userIdentification", "verified");
		return mv;
	}


	/** Seller Uploads an items
	 */
	@RequestMapping(value = "/SellerUploadItem", method = RequestMethod.POST)
	public ModelAndView sellerUploadItem(HttpServletRequest req,
			HttpServletResponse res, @ModelAttribute("itemBean") Item itemBean) {
		
		String userInfo = null;
		String label = null;
		String category = null;
		String price = null;
		String description = null;
		Double amountOwed=0.0;
		String[] userInfoDate = null;
		Trash2TreasureService service = new Trash2TreasureService();
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload sfu  = new ServletFileUpload(factory);
        ModelAndView mv = null;
        List items = null;
		try {
			
			items = sfu.parseRequest(req);
			Iterator<FileItem> it = items.iterator();
			while (it.hasNext()) 
			{
				FileItem fileItem = it.next();
				boolean isFormField = fileItem.isFormField();
				
				if (isFormField) 
				{
					if(fileItem.getFieldName().equals("userInfo"))
					{
						userInfo= fileItem.getString();
						Long nuID = Long.parseLong(userInfo.split("__")[1]);
						amountOwed = service.getAmountOwedByNUID(Long.valueOf(nuID));
					}
					if(fileItem.getFieldName().equals("label"))
					{
						label=fileItem.getString();
						itemBean.setLabel(label);
					}
					if(fileItem.getFieldName().equals("category"))
					{
						category=fileItem.getString();
						itemBean.setCategory(service.getCategory(category));
					}
					if(fileItem.getFieldName().equals("price"))
					{
						price=fileItem.getString();
						itemBean.setPrice(service.getPrice(price));
					}
					if(fileItem.getFieldName().equals("description"))
					{
						description=fileItem.getString();
						itemBean.setDescription(description);
					}
					
			}
				else
				{
					userInfoDate = userInfo.split("__");
					File image = new File(fileItem.getName());
					String UPLOAD_DIR = "WEB-INF\\uploads";
					String applicationPath = req.getServletContext().getRealPath("");
			        // constructs path of the directory to save uploaded file
			        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
			          
			        // creates the save directory if it does not exists
			        File fileSaveDir = new File(uploadFilePath);
			        if (!fileSaveDir.exists()) {
			            fileSaveDir.mkdirs();
			        }
			        
			        String fileName = userInfoDate[1]+"_"+(++imageCount) +image.getName();
			        String filePath = uploadFilePath + File.separator + fileName;
			        File uploadedFile = new File(filePath);
					fileItem.write(uploadedFile);
					File itemImage = new File(filePath);
					byte[] bFile = new byte[(int) itemImage.length()];
					FileInputStream fis = new FileInputStream(itemImage);
					fis.read(bFile);
					fis.close();
					
					++imageCount;
					
					itemBean.setImage(fileName);
					
			}
            
        }
			
			
		} catch (Exception e1) {
			
			e1.printStackTrace();
			mv = new ModelAndView("Seller");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			mv.addObject("userIdentification", "verified");
			
			if(amountOwed==0)
				amountOwed=0.0;
			mv.addObject("amountOwed","$"+amountOwed);
			return mv;
		}
        
		boolean isAdmin = 
				(userInfoDate[0].equals("Admin") ? true : false);
		Long nuID = Long.parseLong(userInfoDate[1]);
		
		String viewName = "";
		if (isAdmin)
			viewName = "Admin";
		else
			viewName = "Seller";
		
		itemBean.setNuID(nuID);
		itemBean.setApprovalStatus(false);
		itemBean.setSaleStatus(false);
		itemBean.setPriceSettlementStatus(false);
		
		try {
			service.uploadItem(itemBean);
		} catch (SQLException e) {
			e.printStackTrace();
			mv = new ModelAndView(viewName);
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			mv.addObject("userIdentification", "verified");
			if(viewName.equals("Seller")){
				if(amountOwed==0)
					amountOwed=0.0;
			
				mv.addObject("amountOwed","$"+amountOwed);
			}
			mv.addObject("amountOwed",amountOwed);
			return mv;
			
		}

		mv = new ModelAndView("Seller");
		mv.addObject("msg", "Item Uploaded successfully..!!");
		if(viewName.equals("Seller"))
			mv.addObject("amountOwed",amountOwed);

		mv.addObject("userInfo", userInfo);

		
		mv.addObject("userIdentification", "verified");

		return mv;
	}


	/** modify the item that is uploaded */
	@RequestMapping(value = "/ModifyItem/{userInfo}")
	public ModelAndView modifyItem(HttpServletRequest req, HttpServletResponse res,@PathVariable("userInfo") String userInfo) {

		if(! userInfo.contains("__")){
			return null;
		}
		Double amountOwed=0.0;
		// fetch all items of this user and send to this page
		Trash2TreasureService service = new Trash2TreasureService();
		boolean isAdmin = (userInfo.split("__")[0]).equals("Admin") ? true: false;
		int nuID = Integer.parseInt(userInfo.split("__")[1]);
		
		ModelAndView mv = null;
		List<Item> sellerItems;
		try {
			amountOwed = service.getAmountOwedByNUID(Long.valueOf(nuID));
			sellerItems = service.getSellerItems(isAdmin, nuID);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			mv = new ModelAndView("Seller");
			mv.addObject("msg", "Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			mv.addObject("userIdentification", "verified");
			
			if(amountOwed==0)
				amountOwed=0.0;
		
			mv.addObject("amountOwed","$"+amountOwed);
			return mv;
		}

		String UPLOAD_DIR = "images";
		String applicationPath = req.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        
		if (sellerItems == null || sellerItems.size() == 0) {
			mv = new ModelAndView("Seller");
			mv.addObject("msg", "You donot have any items in your list..!!");
			if(amountOwed==0)
				amountOwed=0.0;
		
			mv.addObject("amountOwed","$"+amountOwed);
			
		} else {
			mv = new ModelAndView("ModifyItem");
			mv.addObject("items", sellerItems);

		}
		mv.addObject("userInfo", userInfo);

		mv.addObject("userIdentification", "verified");
		return mv;

	}


	/** Deletes a item from the store */
	@RequestMapping("/DeleteThisItem/{userInfo}/{itemId}")
	public ModelAndView deleteThisItem( @PathVariable("itemId") String itemId, 
			@PathVariable("userInfo") String userInfo){

		if(! userInfo.contains("__")){
			return null;
		}
		Double amountOwed=0.0;
		// fetch all items of this user and send to this page
		int deleteItemId = Integer.parseInt(itemId);
		// pass this id to dao for deletion of the item

		Trash2TreasureService service = new Trash2TreasureService();

		boolean isAdmin = (userInfo.split("__")[0]).equals("Admin") ? true
				: false;
		int nuID = Integer.parseInt(userInfo.split("__")[1]);
			
		ModelAndView mv = null;
		try {
			amountOwed = service.getAmountOwedByNUID(Long.valueOf(nuID));
			service.deleteItem(deleteItemId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			mv = new ModelAndView("Seller");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			if(amountOwed==0)
				amountOwed=0.0;
		
			mv.addObject("amountOwed","$"+amountOwed);
			mv.addObject("userIdentification", "verified");
			return mv;
		}

		// fetch seller items again
		List<Item> sellerItems;
		try {
			sellerItems = service.getSellerItems(isAdmin, nuID);
		} catch (SQLException e) {
			mv = new ModelAndView("Seller");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			mv.addObject("amountOwed",amountOwed);
			mv.addObject("userIdentification", "verified");
			return mv;
		}


		if (sellerItems==null || sellerItems.size() == 0) {
			mv = new ModelAndView("Seller");
			mv.addObject("msg", "You donot have any items in your list..!!");
			if(amountOwed==0)
				amountOwed=0.0;
		
			mv.addObject("amountOwed","$"+amountOwed);
		} else {
			mv = new ModelAndView("ModifyItem");
			mv.addObject("items", sellerItems);
			mv.addObject("msg", "Item deleted..!!");
		}

		mv.addObject("userInfo", userInfo);

		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;

	}


	/** Modifies/ Updates an Item
	 */
	@RequestMapping("/UpdateThisItem/{userInfo}/{itemId}")
	public ModelAndView updateThisItem(
			@PathVariable("userInfo") String userInfo,
			@PathVariable("itemId") String itemId)
			{
		if(! userInfo.contains("__")){
			return null;
		}
		Double amountOwed=0.0;
		// fetch all items of this user and send to this page
		int updateItemId = Integer.parseInt(itemId);
		// pass this id to dao and fetch the item

		Long nuID = Long.parseLong(userInfo.split("__")[1]);
		Trash2TreasureService service = new Trash2TreasureService();
		ModelAndView mv = new ModelAndView("UpdateAnItem");
		Item item = null;
		try {
			amountOwed = service.getAmountOwedByNUID(Long.valueOf(nuID));
			item = service.getItemInformation(updateItemId);
		} catch (SQLException e) {
			mv = new ModelAndView("Seller");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			if(amountOwed==0)
				amountOwed=0.0;
		
			mv.addObject("amountOwed","$"+amountOwed);
			mv.addObject("userIdentification", "verified");
			return mv;
		}
		mv.addObject("item", item);
		// to be computed
		mv.addObject("userIdentification", "verified");

		mv.addObject("userInfo", userInfo);
		return mv;

	}

	/** Updates the Items*/
	@RequestMapping(value = "/UpdateEditedItem", method = RequestMethod.POST)
	public ModelAndView updateEditedItem(HttpServletRequest req,
			HttpServletResponse res, @ModelAttribute("itemBean") Item itemBean) {
		
		String userInfo = req.getParameter("userInfo");
		Double amountOwed=0.0;
		// store registration details and login
		boolean isAdmin = (userInfo.split("__")[0]).equals("Admin") ? true
				: false;
		int nuID = Integer.parseInt(userInfo.split("__")[1]);
		
		Trash2TreasureService service = new Trash2TreasureService();
		ModelAndView mv = null;
		try {
			amountOwed = service.getAmountOwedByNUID(Long.valueOf(nuID));
			service.updateItemDetails(itemBean);
		} catch (SQLException e) {
			mv = new ModelAndView("Seller");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			if(amountOwed==0)
				amountOwed=0.0;
		
			mv.addObject("amountOwed","$"+amountOwed);
			mv.addObject("userIdentification", "verified");
			return mv;
			
		}

		// fetch seller items again
		List<Item> sellerItems = null;
		try {
			sellerItems = service.getSellerItems(isAdmin, nuID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			mv = new ModelAndView("Seller");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			if(amountOwed==0)
				amountOwed=0.0;
		
			mv.addObject("amountOwed","$"+amountOwed);
			mv.addObject("userIdentification", "verified");
			return mv;
		}

		
		if (sellerItems.size() == 0) {
			mv = new ModelAndView("Seller");
			mv.addObject("msg", "You donot have any items in your list..!!");
			if(amountOwed==0)
				amountOwed=0.0;
		
			mv.addObject("amountOwed","$"+amountOwed);
		} else {
			mv = new ModelAndView("ModifyItem");
			mv.addObject("items", sellerItems);
			mv.addObject("msg", "Item details successfully edited..!!");
		}

		mv.addObject("userInfo", userInfo);

		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;
	}

	/** Returns all the Items that should be displayed to a particular seller */
	@RequestMapping(value = "/ViewMyItems/{userInfo}")
	public ModelAndView viewSellerItems(@PathVariable("userInfo") String userInfo) {
		if(! userInfo.contains("__")){
			return null;
		}
		
		Double amountOwed=0.0;
		boolean isAdmin = (userInfo.split("__")[0]).equals("Admin") ? true
				: false;
		int nuID = Integer.parseInt(userInfo.split("__")[1]);

		Trash2TreasureService service = new Trash2TreasureService();
		ModelAndView mv = null;
		List<Item> sellerItems;
		try {
			amountOwed = service.getAmountOwedByNUID(Long.valueOf(nuID));
			sellerItems = service.getSellerItems(isAdmin, nuID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			mv = new ModelAndView("Seller");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			if(amountOwed==0)
				amountOwed=0.0;
		
			mv.addObject("amountOwed","$"+amountOwed);
			mv.addObject("userIdentification", "verified");
			return mv;
		}

		if (sellerItems == null || sellerItems.size() == 0) {
			mv = new ModelAndView("Seller");
			mv.addObject("msg", "You donot have any items in your list..!!");
			if(amountOwed==0)
				amountOwed=0.0;
		
			mv.addObject("amountOwed","$"+amountOwed);
		} else {
			mv = new ModelAndView("DisplaySellerItems");
			mv.addObject("items", sellerItems);

		}

		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;

	}

	/**
	 * Displays all the items that should be displayed to all the users.
	 */
	@RequestMapping(value = "/ViewAllItems/{userInfo}")
	public ModelAndView viewAllItems(HttpServletRequest req,
			HttpServletResponse res,@PathVariable("userInfo") String userInfo){

		if(! userInfo.contains("__")){
			System.out.println("Inside if");
			return null;
		}
		//String userInfo = req.getParameter("userInfo");
		System.out.println("in viewAllItems");
		System.out.println("User Info: "+ userInfo);
		int nuID = Integer.parseInt(userInfo.split("__")[1]);
		// fetch all items
		Double amountOwed=0.0;
		Trash2TreasureService service = new Trash2TreasureService();
		ModelAndView mv = null;
		List<Item> allItems;
		try {
			amountOwed = service.getAmountOwedByNUID(Long.valueOf(nuID));
			allItems = service.getAllItems();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mv = new ModelAndView("Seller");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			if(amountOwed==0)
				amountOwed=0.0;
		
			mv.addObject("amountOwed","$"+amountOwed);
			mv.addObject("userIdentification", "verified");
			return mv;
		}

		// fetch amount owed
		
		if (allItems==null || allItems.size() == 0) {
			mv = new ModelAndView("Seller");
			mv.addObject("msg", "There are no items in the system..!!");
			mv.addObject("amountOwed", amountOwed);
		} else {
			mv = new ModelAndView("DisplayItems");
			mv.addObject("items", allItems);

		}
		
		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;
	}

	/**
	 * Return the items sorted.
	 */
	@RequestMapping(value = "/SortedItems", method = RequestMethod.POST)
	public ModelAndView sortedItemsDisplay(HttpServletRequest req,
			HttpServletResponse res) {

		String userInfo = req.getParameter("userInfo");
		System.out.println("in sortedItemsDisplay");
		System.out.println("User Info: "+ userInfo);
		ModelAndView mv =null;
		if(userInfo==null){
			System.out.println("inside if: ");
			mv = new ModelAndView("viewItems");
		}else
			mv = new ModelAndView("DisplayItems");
		String sortType = req.getParameter("sort");
		String sortCriteria = "all";
		List<Item> filteredItems = null;
		Trash2TreasureService service = new Trash2TreasureService();
		if (sortType.equals("categoryRadio")) {
			sortCriteria = req.getParameter("category");
			
			System.out.println("Sort By Category: "+sortCriteria);
			try {
				filteredItems = service.getFilteredItems("sortByCategory",
						sortCriteria);
			} catch (SQLException e) {
				mv.addObject("msg",
						"Oops..Something went wrong..Please try again...!!");
				return mv;
			}
			if (filteredItems==null|| filteredItems.size() == 0) {
				try {
					filteredItems = service.getAllItems();
				} catch (SQLException e) {
					mv.addObject("msg",
							"Oops..Something went wrong..Please try again...!!");
					return mv;
				}
				mv.addObject("msg", "No items found for value: " + sortCriteria+" ..Displaying all items");
			} else {
				mv.addObject("msg", "Items sorted by: "+ sortCriteria);
			}

		} else if (sortType.equals("priceRadio")) {
			sortCriteria = req.getParameter("price");
			try {
				filteredItems = service.getFilteredItems("sortByPrice",
						sortCriteria);
			} catch (SQLException e) {
				mv.addObject("msg",
						"Oops..Something went wrong..Please try again...!!");
				return mv;
			}

			if (filteredItems==null||filteredItems.size() == 0) {
				try {
					filteredItems = service.getAllItems();
				} catch (SQLException e) {
					mv.addObject("msg",
							"Oops..Something went wrong..Please try again...!!");
					return mv;
				}
				mv.addObject("msg", "No items found for value: " + sortCriteria+" ..Displaying all items");
			} else {
				mv.addObject("msg", "Items sorted by: "+ sortCriteria);
			}
		} else {
			try {
				filteredItems = service.getAllItems();
			} catch (SQLException e) {
				mv.addObject("msg",
						"Oops..Something went wrong..Please try again...!!");
				return mv;
			}
			mv.addObject("msg", "Displaying all items..!!");

		}

		// fetch all items

		mv.addObject("items", filteredItems);

		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;
	}

	/**
	 * Sort the items for the buyer.
	 */
	@RequestMapping(value = "/SortedItemsBuyer", method = RequestMethod.POST)
	public ModelAndView sortedItemsDisplayBuyer(HttpServletRequest req,
			HttpServletResponse res) {

		String userInfo = req.getParameter("userInfo");
		System.out.println("in sortedItemsDisplay");
		System.out.println("User Info: "+ userInfo);
		ModelAndView mv =null;
		
			mv = new ModelAndView("viewItems");
		String sortType = req.getParameter("sort");
		String sortCriteria = "all";
		List<Item> filteredItems = null;
		Trash2TreasureService service = new Trash2TreasureService();
		if (sortType.equals("categoryRadio")) {
			sortCriteria = req.getParameter("category");
			
			System.out.println("Sort By Category: "+sortCriteria);
			try {
				filteredItems = service.getFilteredItems("sortByCategory",
						sortCriteria);
			} catch (SQLException e) {
				mv.addObject("msg",
						"Oops..Something went wrong..Please try again...!!");
				return mv;
			}
			if (filteredItems==null|| filteredItems.size() == 0) {
				try {
					filteredItems = service.getAllItems();
				} catch (SQLException e) {
					mv.addObject("msg",
							"Oops..Something went wrong..Please try again...!!");
					return mv;
				}
				mv.addObject("msg", "No items found for value: " + sortCriteria+" ..Displaying all items");
			} else {
				mv.addObject("msg", "Items sorted by: "+ sortCriteria);
			}

		} else if (sortType.equals("priceRadio")) {
			sortCriteria = req.getParameter("price");
			try {
				filteredItems = service.getFilteredItems("sortByPrice",
						sortCriteria);
			} catch (SQLException e) {
				mv.addObject("msg",
						"Oops..Something went wrong..Please try again...!!");
				return mv;
			}

			if (filteredItems==null||filteredItems.size() == 0) {
				try {
					filteredItems = service.getAllItems();
				} catch (SQLException e) {
					mv.addObject("msg",
							"Oops..Something went wrong..Please try again...!!");
					return mv;
				}
				mv.addObject("msg", "No items found for value: " + sortCriteria+" ..Displaying all items");
			} else {
				mv.addObject("msg", "Items sorted by: "+ sortCriteria);
			}
		} else {
			try {
				filteredItems = service.getAllItems();
			} catch (SQLException e) {
				mv.addObject("msg",
						"Oops..Something went wrong..Please try again...!!");
				return mv;
			}
			mv.addObject("msg", "Displaying all items..!!");

		}

		mv.addObject("items", filteredItems);
		return mv;
	}

	
	
	/**
	 * Approve the pending items of the admin
	 */
	@RequestMapping("/AdminApprovePendingItems/{userInfo}")
	protected ModelAndView adminApprovePendingItems(HttpServletRequest req,
			HttpServletResponse res,@PathVariable("userInfo") String userInfo){

		ModelAndView mv = new ModelAndView("Admin");

		Trash2TreasureService service = new Trash2TreasureService();
		// fetch pending items
		List<Item> pendingItems;
		try {
			pendingItems = service.getPendingItems();
		} catch (SQLException e) {
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			// to be computed
			mv.addObject("userIdentification", "verified");
			return mv;
		}

		if (pendingItems==null || pendingItems.size() == 0) {
			mv.addObject("msg", "There are no pending items in the system..!!");
		} else {
			mv = new ModelAndView("PendingItemsApproval");
			mv.addObject("items", pendingItems);

		}
		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;

	}
	
	
	/**
	 * Admin approves the items
	 */
	@RequestMapping("/AdminApproveAnItem/{userInfo}/{itemId}")
	public ModelAndView adminApprovePendingItem(HttpServletRequest req,
			HttpServletResponse res, @PathVariable("itemId") String itemId,
			@PathVariable("userInfo") String userInfo){

		int id = Integer.parseInt(itemId);

		ModelAndView mv = null;
		// fetch pending items
		Trash2TreasureService service = new Trash2TreasureService();
		// fetch pending items
		List<Item> pendingItems = null;
		try {
			service.approvePendingItem(id);
			pendingItems = service.getPendingItems();
		} catch (SQLException e) {
			mv = new ModelAndView("Admin");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			// to be computed
			mv.addObject("userIdentification", "verified");
			return mv;
		}

		if (pendingItems==null || pendingItems.size() == 0) {
			mv = new ModelAndView("Admin");
			mv.addObject("msg", "There are no pending items in the system..!!");
		} else {
			mv = new ModelAndView("PendingItemsApproval");
			mv.addObject("items", pendingItems);

		}
		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;

	}

	/**
	 * Method invoked when the admin Sells a item.
	 */
	@RequestMapping("/AdminSaleEvent/{userInfo}")
	protected ModelAndView adminSaleItem(HttpServletRequest req,
			HttpServletResponse res,@PathVariable("userInfo") String userInfo){
		
		//String userInfo = req.getParameter("userInfo");
		System.out.println("in adminSaleItem");
		System.out.println("User Info: "+ userInfo);
		ModelAndView mv = null;
		// fetch all items
		Trash2TreasureService service = new Trash2TreasureService();
		// fetch pending items
		List<Item> approvedItems;
		try {
			approvedItems = service.getApprovedItems();
		} catch (Exception e) {
			mv = new ModelAndView("Admin");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			// to be computed
			mv.addObject("userIdentification", "verified");
			return mv;
		}

		if (approvedItems==null || approvedItems.size() == 0) {
			mv = new ModelAndView("Admin");
			mv.addObject("msg", "There are no items in the system..!!");
		} else {
			mv = new ModelAndView("SaleOfItems");
			mv.addObject("items", approvedItems);

		}
		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;

	}

	/**
	 * Admin updates an item as sold
	 */
	@RequestMapping("/AdminSaleAnItem/{userInfo}/{itemId}")
	public ModelAndView adminSaleThisItem(HttpServletRequest req,
			HttpServletResponse res, @PathVariable("itemId") String itemId,
			@PathVariable("userInfo") String userInfo){
		
		// fetch all items of this user and send to this page
		int itemMarkAsSold = Integer.parseInt(itemId);
		// pass this id to dao for approving, i.e. status change to approved
		
		ModelAndView mv = null;
		Trash2TreasureService service = new Trash2TreasureService();
		List<Item> approvedItems = null;
		try {
			service.markAsSoldItem(itemMarkAsSold);
			// fetch all items
			approvedItems = service.getApprovedItems();
		} catch (SQLException e) {
			mv = new ModelAndView("Admin");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			// to be computed
			mv.addObject("userIdentification", "verified");
			return mv;
		}
		if (approvedItems==null || approvedItems.size() == 0) {
			mv = new ModelAndView("Admin");
			mv.addObject("msg", "All the items have been sold..!!");
		} else {
			mv = new ModelAndView("SaleOfItems");
			mv.addObject("items", approvedItems);
			mv.addObject("msg", "Item marked as sold..!!");
		}
		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;

	}

	/**
	 * Items for the admin to view
	 */
	@RequestMapping("/AdminViewAllItems/{userInfo}")
	protected ModelAndView adminViewAllItems(HttpServletRequest req,
			HttpServletResponse res,@PathVariable("userInfo") String userInfo){

		ModelAndView mv = null;
		Trash2TreasureService service = new Trash2TreasureService();
		// fetch all items
		List<Item> allItems = null;
		try {
			allItems = service.getAllItems();
		} catch (SQLException e) {
			mv = new ModelAndView("Admin");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			// to be computed
			mv.addObject("userIdentification", "verified");
			return mv;
		}

		if (allItems==null || allItems.size() == 0) {
			mv = new ModelAndView("Admin");
			mv.addObject("msg", "There are no items in the system..!!");
		} else {
			mv = new ModelAndView("DisplayItems");
			mv.addObject("items", allItems);
			
		}

		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;

	}
	
	/**
	 * Admin deletes an item when its not a valid one
	 */
	@RequestMapping("/AdminDeleteItem/{userInfo}")
	protected ModelAndView adminDeleteItem(HttpServletRequest req,
			HttpServletResponse res,@PathVariable("userInfo") String userInfo){
		
		//String userInfo = req.getParameter("userInfo");
		System.out.println("in adminDeleteItem");
		System.out.println("User Info: "+ userInfo);
		ModelAndView mv = null;
		// fetch all items
		Trash2TreasureService service = new Trash2TreasureService();
		// fetch pending items
		List<Item> allItems;
		try {
			allItems = service.getAllItems();
		} catch (SQLException e) {
			mv = new ModelAndView("Admin");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			// to be computed
			mv.addObject("userIdentification", "verified");
			return mv;
		}

		if (allItems.size() == 0) {
			mv = new ModelAndView("Admin");
			mv.addObject("msg", "There are no items in the system..!!");
		} else {
			mv = new ModelAndView("AdminDeleteItem");
			mv.addObject("items", allItems);

		}
		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;

	}

	@RequestMapping("/AdminDeleteThisItem/{userInfo}/{itemId}")
	public ModelAndView adminDeleteThisItem(HttpServletRequest req,
			HttpServletResponse res, @PathVariable("itemId") String itemId,
			@PathVariable("userInfo") String userInfo){

		// fetch all items of this user and send to this page
		int deleteItemId = Integer.parseInt(itemId);
		// pass this id to dao for approving, i.e. status change to approved

		ModelAndView mv = null;
		Trash2TreasureService service = new Trash2TreasureService();
		List<Item> allItems = null;
		try {
			service.deleteItem(deleteItemId);
			// fetch all items
			allItems = service.getAllItems();
		} catch (SQLException e) {
			mv = new ModelAndView("Admin");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			// to be computed
			mv.addObject("userIdentification", "verified");
			return mv;
		}
		if (allItems==null || allItems.size() == 0) {
			mv = new ModelAndView("Admin");
			mv.addObject("msg", "There are no items in the system..!!");
		} else {
			mv = new ModelAndView("AdminDeleteItem");
			mv.addObject("items", allItems);
		}

		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;
}

	/**
	 * Admin settles an item
	 */
	@RequestMapping("/AdminSettleItems/{userInfo}")
	public ModelAndView adminSettleItems(HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable("userInfo") String userInfo){
		Trash2TreasureService service = new Trash2TreasureService();
		ModelAndView mv = null;
		//String userInfo = req.getParameter("userInfo");
		System.out.println("in adminSettleItems");
		System.out.println("User Info: "+ userInfo);
		List<Seller> settleTheseSellers = null;
		try{
			settleTheseSellers = service.getPendingSettlementForSellers();
		}catch(Exception e){
			mv = new ModelAndView("Admin");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			// to be computed
			mv.addObject("userIdentification", "verified");
			return mv;
		}
		
		if((settleTheseSellers == null) || (settleTheseSellers.size() == 0)){
			mv = new ModelAndView("Admin");
			mv.addObject("msg",
					"No pending settlements..");
			
		}else{
			System.out.println(settleTheseSellers.size());
			mv = new ModelAndView("SettleSellers");
			mv.addObject("users", settleTheseSellers);
		}
		
		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;

	}
	
	/**
	 * Admin settles the seller
	 */
	@RequestMapping("AdminSettleSeller/{userInfo}/{sellerNUID}")
	public ModelAndView adminSettleThisSeller(HttpServletRequest req,
			HttpServletResponse res, @PathVariable("sellerNUID") String sellerNUID,
			@PathVariable("userInfo") String userInfo){
		Trash2TreasureService service = new Trash2TreasureService();
		ModelAndView mv = null;
	
		List<Seller> settleTheseSellers = null;
		try{
			service.settleThisSeller(Long.valueOf(sellerNUID));
		}catch(Exception e){
			mv = new ModelAndView("Admin");
			mv.addObject("msg",
					"Oops..Seller amount settlement failed..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			// to be computed
			mv.addObject("userIdentification", "verified");
			return mv;
		}
		
		try{
			settleTheseSellers = service.getPendingSettlementForSellers();
		}catch(Exception e){
			mv = new ModelAndView("Admin");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			// to be computed
			mv.addObject("userIdentification", "verified");
			return mv;
		}
		
		if(settleTheseSellers == null || settleTheseSellers.size()==0){
			mv = new ModelAndView("Admin");
			mv.addObject("msg",
					"No pending settlements..");
			
		}else{
			mv = new ModelAndView("SettleSellers");
			mv.addObject("users", settleTheseSellers);
			mv.addObject("settleSuccess","Seller Amount has been settled..!!");
		}
		
		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		return mv;
		
	}
	
	/**
	 * Displays the admin summary
	 */
	@RequestMapping("AdminSummary/{userInfo}")
	public ModelAndView adminSummary(HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable("userInfo") String userInfo){
		Trash2TreasureService service = new Trash2TreasureService();
		ModelAndView mv = null;
	
		AdminSummary adminSummary = null;
		try{
			adminSummary = service.getAdminSummary();
		}catch(Exception e){
			e.printStackTrace();
			mv = new ModelAndView("Admin");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			mv.addObject("userInfo", userInfo);
			// to be computed
			mv.addObject("userIdentification", "verified");
			return mv;
		}
		
		mv = new ModelAndView("AdminSummary");
		mv.addObject("adminSummary",adminSummary);
		mv.addObject("userInfo", userInfo);
		// to be computed
		mv.addObject("userIdentification", "verified");
		
		return mv;
	}
	/**
	 * View all the items of the Buyer
	 */
	@RequestMapping("/viewItemsBuyer")
	protected ModelAndView buyerViewAllItems(HttpServletRequest req,
			HttpServletResponse res){

		ModelAndView mv = null;

		Trash2TreasureService service = new Trash2TreasureService();
		// fetch all items
		List<Item> items = null;
		try {
			items = service.getAllItems();
		} catch (SQLException e) {
			mv = new ModelAndView("DisplayItems");
			mv.addObject("msg",
					"Oops..Something went wrong..Please try again...!!");
			return mv;
		}

		if (items==null || items.size() == 0) {
			mv = new ModelAndView("index");
			mv.addObject("msg", "There are no items in the system..!!");
		} else {
			System.out.println("**************");
			mv = new ModelAndView("viewItems");
			mv.addObject("items", items);
		}
		return mv;
	}
}
