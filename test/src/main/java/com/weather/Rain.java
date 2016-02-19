package com.weather;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Rain implements Serializable{
	/**
	 * 
	 */
    private static final long serialVersionUID = 1L;
	
    @SerializedName("3h")
	private String _3h;

	public String get_3h() {
		return _3h;
	}

	public void set_3h(String _3h) {
		this._3h = _3h;
	}

}
