package com.assignment;

import java.util.ArrayList;
import java.util.HashMap;

public class Product{
		
	private Long id;
	private String title;
	private Long price;
	private HashMap<Long, String> categories;
	private ArrayList<Long> labelsId;
	private HashMap<String, ArrayList<String>> labels;
	
	public Product(Long id, String title, Long price, HashMap<Long, String> categories, ArrayList<Long> labels) {
		super();
		this.id = id;
		this.title = title;
		this.price = price;
		this.categories = categories;
		this.labelsId = labels;
		this.labels = new HashMap<String, ArrayList<String>>();
	}
	
	public void addLabel(String title, String label) {
		if(!this.labels.containsKey(title)) {
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(label);
			this.labels.put(title, temp);
		} else {
			this.labels.get(title).add(label);
		}
	}
	
	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Long getPrice() {
		return price;
	}

	public HashMap<Long, String> getCategories() {
		return categories;
	}

	public ArrayList<Long> getLabelsId() {
		return labelsId;
	}

	public HashMap<String, ArrayList<String>> getLabels() {
		return labels;
	}

	
}
