package com.assignment;

import java.util.HashMap;

public class Category {
	
	private String title;
	private int countRelatedProducts;
	
	/**
	 * This property maps "Attributes" to it's related labels that stored as Map that map "labels" to the count of related products.
	 * (It's used as counter property). 
	 */
	private HashMap<String, HashMap<String, Integer>> relatedAttributes;
	
public Category(String title, int countRelatedProducts) {
		super();
		this.title = title;
		this.countRelatedProducts = countRelatedProducts;
		this.relatedAttributes = new HashMap<String, HashMap<String, Integer>>();
	}

	public String getTitle() {
		return title;
	}

	public int getCountRelatedProducts() {
		return countRelatedProducts;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCountRelatedProducts(int countRelatedProducts) {
		this.countRelatedProducts = countRelatedProducts;
	}
	
	public HashMap<String, HashMap<String, Integer>> getRelatedAttributes() {
		return relatedAttributes;
	}

	//This method adds label to the correct attribute in the list of related attributes, 
	//and change the counter of each label.
	public void addLabel(String attribute, String label) {
		if(relatedAttributes.containsKey(attribute)) {
			if(relatedAttributes.get(attribute).containsKey(label)) {
				relatedAttributes.get(attribute).put(label, relatedAttributes.get(attribute).get(label) + 1);
			} else {
				HashMap<String, Integer> labelsRelated = new HashMap<String,Integer>();
				labelsRelated.put(label, 1);
				relatedAttributes.get(attribute).put(label, 1);
			}
		} else {
			HashMap<String, Integer> labelsRelated = new HashMap<String, Integer>();
			labelsRelated.put(label, 1);
			relatedAttributes.put(attribute, labelsRelated);
		}
	}
	
}
