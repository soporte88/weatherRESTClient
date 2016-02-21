package com.weather;

import java.io.Serializable;
import java.util.List;

public class CityList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cod;
	private String calctime;
	private String cnt;
	private String message;
	private String count;
	private List<City> list;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getCalctime() {
		return calctime;
	}

	public void setCalctime(String calctime) {
		this.calctime = calctime;
	}

	public String getCnt() {
		return cnt;
	}

	public void setCnt(String cnt) {
		this.cnt = cnt;
	}

	public List<City> getList() {
		return list;
	}

	public void setList(List<City> list) {
		this.list = list;
	}

}
