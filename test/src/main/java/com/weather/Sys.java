package com.weather;

import java.io.Serializable;

public class Sys implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * Internal parameter
	 */
	private String id;
	/*
	 * Internal parameter
	 */
	private String type;
	/*
	 * Internal parameter
	 */
	private String message;
	/*
	 * Country code (GB, JP etc.)
	 */
	private String country;
	/*
	 * Sunrise time, unix, UTC
	 */
	private String sunrise;
	/*
	 * Sunset time, unix, UTC
	 */
	private String sunset;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSunrise() {
		return sunrise;
	}

	public void setSunrise(String sunrise) {
		this.sunrise = sunrise;
	}

	public String getSunset() {
		return sunset;
	}

	public void setSunset(String sunset) {
		this.sunset = sunset;
	}

}
