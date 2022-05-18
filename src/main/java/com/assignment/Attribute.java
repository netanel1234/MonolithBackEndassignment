package com.assignment;

import java.util.HashMap;

public class Attribute {
	
	private Long id;
	private String title;
	private HashMap<Long, String> labels;
	
	public Attribute(Long id, String title, HashMap<Long, String> labels) {
		super();
		this.id = id;
		this.title = title;
		this.labels = labels;
	}

	public String getTitle() {
		return title;
	}

	public HashMap<Long, String> getLabels() {
		return labels;
	}

}
