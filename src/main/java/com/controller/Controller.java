package com.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.assignment.Attribute;
import com.assignment.Category;
import com.assignment.Model;
import com.assignment.Product;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Set HttpSession object to share data in the application context
		HttpSession session = request.getSession();
		
		Model model = new Model();
		
		//Establish connection and get the response from the external API
		JSONObject jsonObject = model.getJSON();
		
		//If the response from the API is empty return 
		if(jsonObject == null) {
			
			session.setAttribute("empty", null);
			session.setAttribute("products", null);
			session.setAttribute("categories", null);
			
		} else {
			
			//Parse the JSONObject and get Product[]
			Product[] listOfProducts = model.getProductList(jsonObject);
			
			//Use the Product[] to get Category[]
			Category[] listOfCategories = model.getCategoryList(listOfProducts);
			
			//Attach the lists to the application context
			session.setAttribute("products", listOfProducts);
			session.setAttribute("categories", listOfCategories);
		}
			
		//Return to html page.
		getServletContext().getRequestDispatcher("/home.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
