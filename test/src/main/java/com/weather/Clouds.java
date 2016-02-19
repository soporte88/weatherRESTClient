package com.weather;

import java.io.Serializable;

public class Clouds implements Serializable{
	/**
	 * 
	 */
    private static final long serialVersionUID = 1L;
	private String all;

	public String getAll() {
		return all;
	}

	public void setAll(String all) {
		this.all = all;
	}
}
