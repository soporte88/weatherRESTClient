/**
 * Copyright 2016, Alfonso Cruz, Inc., All Rights Reserved.
 */
package com.weather;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;

/**
 * The Class TestApplicationTests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
@WebAppConfiguration
public class TestApplicationTests {
	
	/** The rest template. */
	private RestTemplate restTemplate;
	
	/** The json content type. */
	final String jsonContentType = "application/json";
	
	/** The url. */
	final String url = "http://api.openweathermap.org/data/2.5/forecast/city";
	
	/** The url city. */
	final String urlCity = "http://api.openweathermap.org/data/2.5/weather";
	
	/** The app id. */
	final String appId = "X";
	
	/** The city. */
	private String city = "524901";
	

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		restTemplate = new RestTemplate();
	}
	
	/**
	 * Test method.
	 *
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testMethod() throws JsonParseException, JsonMappingException, IOException{
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		Map<String, String> requestParams = new HashMap<String, String>();
		HttpHeaders requestHeaders = new HttpHeaders();
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		requestParams.put("id", city);
		requestParams.put("appid", appId);
		ResponseEntity<String> response = restTemplate.exchange(url+"?id=524901&appid=443d426df86b1a15193ae59b476450f8", HttpMethod.GET, requestEntity,  String.class);
		Assert.notNull(response);
		System.out.println(response.getBody());
		
	}
	
	/**
	 * Getting the data from one city.
	 *
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testCityArray() throws JsonParseException, JsonMappingException, IOException{
		/**
		 * Only to Test the 3h property
		 */
		String cities2[] = {"Abernant"};
		
		for(String sCity : cities2){
			City city = this.testCityWeather(sCity);
			Assert.notNull(city, "The city should not be null");
			Assert.notNull(city.getSys().getCountry(), "The country should not be null");
			System.out.println(city.getName());
			System.out.println(city.getSys().getCountry());
			System.out.println(city.getCod());
			
			City cityById = testCityWeatherById(city.getId());
			Assert.notNull(cityById, "Testing by Id Failed: The city should not be null: "+city.getId());
			Assert.notNull(cityById.getSys().getCountry(), "Testing by id Failed: The country should not be null: "+city.getId());
			System.out.println(cityById.getName());
			System.out.println(cityById.getSys().getCountry());
			System.out.println(cityById.getCod());
			
			City cityByGeo = testCityByGeo(cityById.getCoord().getLat(), cityById.getCoord().getLon());
			Assert.notNull(cityByGeo, "Testing by Geo Failed: The city should not be null: "+"Lat: " +cityById.getCoord().getLat()+ "Lon: " + cityById.getCoord().getLon());
			Assert.notNull(cityByGeo.getSys().getCountry(), "Testing by Geo Failed: The country should not be null: "+"Lat: " +cityById.getCoord().getLat()+ "Lon: " + cityById.getCoord().getLon());
			System.out.println(cityByGeo.getName());
			System.out.println(cityByGeo.getSys().getCountry());
			System.out.println(cityByGeo.getCod());
		}
		System.out.println("Tested: " + cities.length +" cities: " + cities);
	}
	
	/**
	 * Test wheater by zip codes.
	 *
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Test
	public void testWheaterByZipCodes() throws JsonParseException, JsonMappingException, IOException{
		String country = "us";
		for(String zipCode : zipCodes ){
			City city = this.testCityWeatherByZip(zipCode, country);
			Assert.notNull(city, "The city should not be null");
			Assert.notNull(city.getSys().getCountry(), "The country should not be null");
			System.out.println(city.getName());
			System.out.println(city.getSys().getCountry());
			System.out.println(city.getCod());
		}
		System.out.println("Tested: " + zipCodes.length + " zip codes.");
	}
	
	/**
	 * Test city weather by zip.
	 *
	 * @param zipCode
	 *            the zip code
	 * @param country
	 *            the country
	 * @return the city
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private City testCityWeatherByZip(String zipCode, String country) throws JsonParseException, JsonMappingException, IOException{
		return executeExchange("zip="+zipCode+","+country);
	}
	
	/**
	 * Test city weather.
	 *
	 * @param city
	 *            the city
	 * @return the city
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private City testCityWeather(String city) throws JsonParseException, JsonMappingException, IOException{
		return executeExchange("q="+city);
	}
	
	/**
	 * Test city weather by id.
	 *
	 * @param cityId
	 *            the city id
	 * @return the city
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private City testCityWeatherById(String cityId) throws JsonParseException, JsonMappingException, IOException{
		return executeExchange("id="+cityId);
	}
	
	/**
	 * Test city by geo.
	 *
	 * @param lat
	 *            the lat
	 * @param lon
	 *            the lon
	 * @return the city
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private City testCityByGeo(String lat, String lon) throws JsonParseException, JsonMappingException, IOException{
		return executeExchange("lat="+lat+"&lon="+lon);
	}
	
	/**
	 * Execute exchange.
	 *
	 * @param parameters
	 *            the parameters
	 * @return the city
	 * @throws JsonParseException
	 *             the json parse exception
	 * @throws JsonMappingException
	 *             the json mapping exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private City executeExchange(String parameters) throws JsonParseException, JsonMappingException, IOException{
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		HttpHeaders requestHeaders = new HttpHeaders();
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		ResponseEntity<String> response = restTemplate.exchange(urlCity+"?"+parameters+"&appid="+appId, HttpMethod.GET, requestEntity,  String.class);
		System.out.println(response.getBody());
		return new Gson().fromJson(response.getBody(), City.class);  
	}
	
	/** The cities. */
	private String []cities = {"Abbeville",
			"Abbotsford",
			"Abbott",
			"Abbottsburg",
			"Abbottstown",
			"Abbyville",
			"Abell",
			"Abercrombie",
			"Aberdeen",
			"Aberfoil",
			"Abernant",
			"Abernathy",
			"Abeytas",
			"Abie",
			"Abilene",
			"Abingdon",
			"Abington",
			"Abiquiu",
			"Abita Springs",
			"Abo"};
	
	/** The zip codes. */
	private String []zipCodes = {"30301",
			"30302",
			"30303",
			"30304",
			"30307",
			"30308",
			"30311",
			"30312",
			"30316",
			"30317",
			"30305",
			"30306",
			"30309",
			"30310",
			"30313",
			"30314",
			"30315",
			"30318",
			"30319"};
}
