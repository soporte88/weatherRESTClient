/**
 * Copyright 2016, Alfonso Cruz, Inc., All Rights Reserved.
 */
package com.weather;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Spliterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.ArrayUtils;
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
	
	final String baseUrl = "http://api.openweathermap.org/data/2.5/";
	/** The url city. */
	final String urlCity = baseUrl+"weather";
	
	final String urlBox = baseUrl+"box/city";
	
	final String urlFind = baseUrl+"find";
	
	final String urlGroup = baseUrl+"group";
	
	/** The app id. */
	final String appId = "443d426df86b1a15193ae59b476450f8";
	
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
			//System.out.println(cityById.getName());
			//System.out.println(cityById.getSys().getCountry());
			//System.out.println(cityById.getCod());
			
			City cityByGeo = testCityByGeo(cityById.getCoord().getLat(), cityById.getCoord().getLon());
			Assert.notNull(cityByGeo, "Testing by Geo Failed: The city should not be null: "+"Lat: " +cityById.getCoord().getLat()+ "Lon: " + cityById.getCoord().getLon());
			Assert.notNull(cityByGeo.getSys().getCountry(), "Testing by Geo Failed: The country should not be null: "+"Lat: " +cityById.getCoord().getLat()+ "Lon: " + cityById.getCoord().getLon());
			//System.out.println(cityByGeo.getName());
			//System.out.println(cityByGeo.getSys().getCountry());
			//System.out.println(cityByGeo.getCod());
		}
		System.out.println("Tested: " + cities.length +" cities: ");
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
	
	@Test
	public void testWheaterByRectangleBbox() throws JsonParseException, JsonMappingException, IOException{
		String bbox = "12,32,15,37,10";
		String cluster = "yes";

		CityList cityList = this.testCityWeatherByRectangle(bbox, cluster);
		Assert.notNull(cityList, "The cityList should not be null");
		Assert.notNull(cityList.getCod(), "The cityList.cod should not be null");
		Assert.notNull(cityList.getCalctime(), "The cityList.calctime should not be null");
		for(City city : cityList.getList()){
			Assert.notNull(city, "The city should not be null");
			Assert.notNull(city.getName(), "The city.name should not be null");
			Assert.notNull(city.getMain().getTemp_min(), "The city.temp_min should not be null");
			Assert.notNull(city.getMain().getTemp_max(), "The city.temp_min should not be null");
			Assert.notNull(city.getMain().getPressure(), "The city.pressure should not be null");
			Assert.notNull(city.getWeather(), "The city.weather should not be null");
			for(Weather weather : city.getWeather()){
				Assert.notNull(weather.getMain(), "The city.weather.main should not be null");
				Assert.notNull(weather.getDescription(), "The city.weather.description should not be null");
				System.out.println(weather.getMain() + " " + weather.getDescription());
			}
			System.out.println(city.getName());
			//System.out.println(city.getSys().getCountry());
			System.out.println(city.getCod());
		}

		System.out.println("Tested: " + cityList.getList().size() + " cities.");
	}
	
	@Test
	public void testWheaterByCycle() throws JsonParseException, JsonMappingException, IOException{
		String lat = "55.5";
		String lon = "37.5";
		String cnt = "10";

		CityList cityList = this.testCityWeatherByCycle(lat, lon, cnt);
		Assert.notNull(cityList, "The cityList should not be null");
		Assert.notNull(cityList.getMessage(), "The cityList.message should not be null");
		Assert.notNull(cityList.getCod(), "The cityList.cod should not be null");
		Assert.notNull(cityList.getCount(), "The cityList.count should not be null");
		for(City city : cityList.getList()){
			Assert.notNull(city, "The city should not be null");
			Assert.notNull(city.getName(), "The city.name should not be null");
			Assert.notNull(city.getMain().getTemp_min(), "The city.temp_min should not be null");
			Assert.notNull(city.getMain().getTemp_max(), "The city.temp_min should not be null");
			Assert.notNull(city.getMain().getPressure(), "The city.pressure should not be null");
			Assert.notNull(city.getWeather(), "The city.weather should not be null");
			for(Weather weather : city.getWeather()){
				Assert.notNull(weather.getMain(), "The city.weather.main should not be null");
				Assert.notNull(weather.getDescription(), "The city.weather.description should not be null");
				System.out.println(weather.getMain() + " " + weather.getDescription());
			}
			System.out.println(city.getName());
			//System.out.println(city.getSys().getCountry());
			System.out.println(city.getCod());
		}

		System.out.println("Tested: " + cityList.getList().size() + " cities.");
	}
	
	@Test
	public void testWheaterByGroup() throws JsonParseException, JsonMappingException, IOException{
		String citiesIds = new String();
		for(String cityId : cityIds){
			if(citiesIds.isEmpty()){
				citiesIds+=cityId;
			}else{
				citiesIds+=","+cityId;
			}
		}
		String units = "metric";
		CityList cityList = this.testCityWeatherByGroup(citiesIds, units);
		Assert.notNull(cityList, "The cityList should not be null");
		Assert.notNull(cityList.getCnt(), "The cityList.cnt should not be null");
		for(City city : cityList.getList()){
			Assert.notNull(city, "The city should not be null");
			Assert.notNull(city.getName(), "The city.name should not be null");
			Assert.notNull(city.getMain().getTemp_min(), "The city.temp_min should not be null");
			Assert.notNull(city.getMain().getTemp_max(), "The city.temp_min should not be null");
			Assert.notNull(city.getMain().getPressure(), "The city.pressure should not be null");
			Assert.notNull(city.getWeather(), "The city.weather should not be null");
			for(Weather weather : city.getWeather()){
				Assert.notNull(weather.getMain(), "The city.weather.main should not be null");
				Assert.notNull(weather.getDescription(), "The city.weather.description should not be null");
				System.out.println(weather.getMain() + " " + weather.getDescription());
			}
			System.out.println(city.getName());
			//System.out.println(city.getSys().getCountry());
		}

		System.out.println("Tested: " + cityList.getList().size() + " cities by group");
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
		return executeExchange(urlCity,"zip="+zipCode+","+country);
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
		return executeExchange(urlCity,"q="+city);
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
		return executeExchange(urlCity,"id="+cityId);
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
		return executeExchange(urlCity,"lat="+lat+"&lon="+lon);
	}
	
	private CityList testCityWeatherByRectangle(String bbox, String cluster) throws JsonParseException, JsonMappingException, IOException{
		return executeExchangeList(urlBox,"bbox="+bbox+"&cluster="+cluster);
	}
	
	private CityList testCityWeatherByCycle(String lat, String lon, String cnt) throws JsonParseException, JsonMappingException, IOException{
		return executeExchangeList(urlFind,"lat="+lat+"&lon="+lon+"&cnt="+cnt);
	}
	
	private CityList testCityWeatherByGroup(String cities,String units) throws JsonParseException, JsonMappingException, IOException{
		return executeExchangeList(urlGroup,"id="+cities + "&units="+units);
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
	private City executeExchange(String urlParam, String parameters) throws JsonParseException, JsonMappingException, IOException{
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		HttpHeaders requestHeaders = new HttpHeaders();
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		ResponseEntity<String> response = restTemplate.exchange(urlParam+"?"+parameters+"&appid="+appId, HttpMethod.GET, requestEntity,  String.class);
		System.out.println(response.getBody());
		return new Gson().fromJson(response.getBody(), City.class);  
	}
	
	private CityList executeExchangeList(String urlParam, String parameters) throws JsonParseException, JsonMappingException, IOException{
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		HttpHeaders requestHeaders = new HttpHeaders();
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		ResponseEntity<String> response = restTemplate.exchange(urlParam+"?"+parameters+"&appid="+appId, HttpMethod.GET, requestEntity,  String.class);
		System.out.println(response.getBody());
		return new Gson().fromJson(response.getBody(), CityList.class);  
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
	
	private String[] cityIds = {"384848","475279","4344544","6199126",
			"3007202","3333225","211098","6553056","2916630","3495858","6356141","6454050","6454341","6455404","7298888",
			"7300545","612366","3716661","6537554","6541492","4012176","4006702"};
}
