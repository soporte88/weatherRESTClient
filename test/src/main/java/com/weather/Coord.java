package com.weather;

import java.io.Serializable;

public class Coord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * City geo location, longitude
	 */
	private String lon;
	/*
	 * City geo location, latitude
	 */
	private String lat;

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}
}
