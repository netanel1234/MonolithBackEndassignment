package com.assignment;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Model {
	
	/**
	 * This method fetch the data from the API mentioned, on every request. 
	 * @return The response from the API as JSON object.
	 */
	public JSONObject getJSON() {
		
		URL url;
		HttpURLConnection connection;
		Scanner scanner;
		JSONParser parser;
		JSONObject jsonObject = null;
		
		try {
			
			//Establish connection 
			url = new URL("https://backend-assignment.bylith.com/index.php");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			
			//Getting the response code
			int responseCode = connection.getResponseCode();
			
			if(responseCode != 200) {
				throw new Exception("HttpResponseCode: " + responseCode);
			} else {
				
				//Write all the JSON data into a string using a scanner
				String inline = "";
				scanner = new Scanner(url.openStream());
				while(scanner.hasNext()) {
					inline += scanner.nextLine();
				}
				
				//Close the scanner
				scanner.close();
				
				//Using the JSON simple library parse the string into a json object
				parser = new JSONParser();
				jsonObject = (JSONObject) parser.parse(inline);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return jsonObject;
	}
	
	/**
	 * This method parse the JSONArray - "attributes" from the JSONObject received from the API 
	 * @param jsonObject - the JSON object received from the API
	 * @return the JSON array "attributes" as Attribute[]
	 */
	private Attribute[] getAttributeList(JSONObject jsonObject) {
		
		//Get the "attributes" (JSONArray) from jsonObject
		JSONArray jsonAttributes = (JSONArray) jsonObject.get("attributes");
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		JSONObject currAttribute;
		Long currAttributeId;
		String currAttributeTitle;
		JSONArray currAttributeLabels;
		HashMap<Long, String> tempAtrributeLabels = null;
		JSONObject currLabel;
		
		//Build List<Attribute>
		for(int i = 0; i < jsonAttributes.size(); i++)
		{
			//currAttribute - the attribute currently under construction 
			currAttribute = (JSONObject) jsonAttributes.get(i);
			
			//Extract the "id" for currAttribute
			currAttributeId = (Long) currAttribute.get("id");
			
			//Extract the "title" for currAttribute
			currAttributeTitle = currAttribute.get("title").toString();
			
			//Extract the "labels" JSONArray from currAttribute
			currAttributeLabels = (JSONArray) currAttribute.get("labels");
			
			//Composing currAttribute "labels" property
			tempAtrributeLabels = new HashMap<Long, String>();
			for(int j = 0; j < currAttributeLabels.size(); j++) {
				currLabel = (JSONObject) currAttributeLabels.get(j);
				tempAtrributeLabels.put((Long) currLabel.get("id"), currLabel.get("title").toString());
			}
			
			//Composing new Attribute and add it to List<Attribute>
			attributes.add(new Attribute(currAttributeId, currAttributeTitle, tempAtrributeLabels));
		}
				
		//Convert from List<Attribute> to Attribute[]
		Attribute[] attributesAsArray = new Attribute[attributes.size()];
		for(int i = 0; i < attributes.size(); i++)
			attributesAsArray[i] = attributes.get(i);
		
		return attributesAsArray;
	}
	
	/**
	 * This method parse the JSONArray - "products" from the JSONObject received from the API
	 * @return list of products
	 */
	public Product[] getProductList(JSONObject jsonObject) 
	{
		
		Attribute[] attributes = getAttributeList(jsonObject);
		
		//Extract "products" (JSONArray) from jsonObject
		JSONArray jsonProducts = (JSONArray) jsonObject.get("products");
		
		List<Product> products = new ArrayList<Product>();
		JSONObject currProduct;
		HashMap<Long, String> currCategories;
		JSONArray jsonCategories;
		Long currId, currPrice;
		String currTitle;
		Product product;
		
		//Build List<Product>
		for(int i = 0; i < jsonProducts.size(); i++) {
			
			//currProduct - the product currently under construction 
			currProduct = (JSONObject) jsonProducts.get(i);
			
			//The "id" for currProduct  
			currId = (Long) currProduct.get("id");
			
			//The "title" for currProduct 
			currTitle = currProduct.get("title").toString();
			
			//The "price" for currProduct 
			currPrice = (Long) currProduct.get("price");
			
			//Create the "categories" property for currProduct
			currCategories = new HashMap<Long, String>();
			jsonCategories = (JSONArray) currProduct.get("categories");
			for(int j = 0; j < jsonCategories.size(); j++) {
				JSONObject currCategory = (JSONObject) jsonCategories.get(j);
				currCategories.put((Long) currCategory.get("id"), currCategory.get("title").toString());
			}
			
			//Create the "labelsId" property for currProduct
			JSONArray jsonLabels = (JSONArray) currProduct.get("labels");
			ArrayList<Long> currLabelsId = new ArrayList<Long>();
			for(int j = 0; j < jsonLabels.size(); j++)
				currLabelsId.add((Long) jsonLabels.get(j));
			
			//Composing the product
			product = new Product(currId, currTitle, currPrice, currCategories, currLabelsId);
			
			//Add the product to List<Product>
			products.add(product);
			
			//Composing "labels" HashMap property in every product that map (labels) "id" to (labels) "title".
			//(Note - Not necessary to composing the Product, but to help later on).  
			for(int j = 0; j < currLabelsId.size(); j++) 
				for(Attribute attribute : attributes) 
					if(attribute.getLabels().containsKey(currLabelsId.get(j))) 
						product.addLabel(attribute.getTitle(), attribute.getLabels().get(currLabelsId.get(j)));
			
		}
				
		//Convert from List<Product> to Product[] 
		Product[] productsAsArray = new Product[products.size()];
		for(int i = 0; i < productsAsArray.length; i++)
			productsAsArray[i] = products.get(i);
		
		return productsAsArray;
	}
	
	/**
	 * Composing Category[] and send it back
	 * @return list of categories
	 */
	public Category[] getCategoryList(Product[] products) {
		
		HashMap<String, Integer> countRelatedProducts = new HashMap<String, Integer>();
		HashMap<Long, String> currProductCategories = new HashMap<Long, String>();
		ArrayList<Category> categories = new ArrayList<Category>();
		
		//Create HashMap that map "category" to the count of product related to it
		for(int i = 0; i < products.length; i++) {
			currProductCategories = products[i].getCategories();
			for(Map.Entry<Long, String> currCategory : currProductCategories.entrySet()) {
				String currCategoryTitle = currCategory.getValue();
				if(countRelatedProducts.containsKey(currCategoryTitle))
					countRelatedProducts.put(currCategoryTitle, countRelatedProducts.get(currCategoryTitle) + 1);
				else
					countRelatedProducts.put(currCategoryTitle, 1);
			}
		}
		
		//Start of composing List<Category>. Initialization of "id" and "title" properties.
		for(Map.Entry<String, Integer> entry : countRelatedProducts.entrySet()) 
			categories.add(new Category(entry.getKey(), entry.getValue()));
				
		//For each element in List<Category>, finding the attributes related to it,
		//and map each "label" to the count of related product.
		for(int i = 0; i < categories.size(); i++) {
			Category currCategory = categories.get(i);
			for(int j = 0; j < products.length; j++) {
				Product currProduct = products[j];
				if(currProduct.getCategories().containsValue(currCategory.getTitle())) {
					for(Map.Entry<String, ArrayList<String>> currLabel : currProduct.getLabels().entrySet()) {
						try {
							for(String labelTitle : currLabel.getValue())
								currCategory.addLabel(currLabel.getKey(), labelTitle);
						} catch(NullPointerException e) { 
							/* If "labelTitle" is null (means: that currProduct has no categories) - do nothing */ 
							}
					}
				}
			}
		}
				
		//Convert  from List<Category> to Category[]
		Category[] categoriesAsArray = new Category[categories.size()];
		for(int i = 0; i < categoriesAsArray.length; i++)
			categoriesAsArray[i] = categories.get(i);
		
		return categoriesAsArray;
	}

}
