package controllers;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import models.Airport;
import models.GeoLocation;
import models.Prediction;
import models.Weather;
import models.YelpRecommendations;
import models.fetcher.AirportInfoFetcher;
import models.fetcher.WeatherFetcher;
import models.fetcher.YelpFetcher;
import play.*;
import play.libs.Json;
import play.mvc.*;

import views.html.*;
import views.*;

public class Application extends Controller {
  
  public static Result index() {
    return ok(index.render());
  }
  
  public static Result weathers(String format, String city) throws JsonGenerationException, JsonMappingException, IOException, IntrospectionException{
	  
	  HashMap<Date, Weather> ws = WeatherFetcher.Fetch(city);
	  if(format.equals("csv")){
		  
		  return ok(Weather.toCSV(ws));//.as("text/csv");
	  }	
	  return ok(new ObjectMapper().writeValueAsString(ws));
  }
  public  static Result weather(String format, String city, String date) throws ParseException, JsonGenerationException, JsonMappingException, IOException{
	  Weather w = WeatherFetcher.Fetch(city, new SimpleDateFormat("yyyy-MM-dd").parse(date));
	  if(format.equals("csv")){
		  return ok(w.toCSV());
	  }
	  return ok(new ObjectMapper().writeValueAsString(w));
  }
  
  public  static Result recommendations(String query, String location) throws ParseException, JsonGenerationException, JsonMappingException, IOException{
	  YelpFetcher yf = new YelpFetcher();
	  YelpRecommendations yrs = yf.fetch(query, location);
	  return ok(new ObjectMapper().writeValueAsString(yrs));
  }
  
  public static Result predictions(String airline, String flightNumber, String strDate, String departure, String arrival) throws JsonGenerationException, JsonMappingException, IOException, ParseException{
	  Prediction p = new Prediction();
	  Date d = new Date();
	  if(!strDate.equals("")){
		  d = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
	  }
	  return ok( new ObjectMapper().writeValueAsString(p.predict(airline, flightNumber, d, departure, arrival)) );
  }
  
  public static Result flights(String airportCode) throws JsonGenerationException, JsonMappingException, IOException, ParseException{
	  return ok(new ObjectMapper().writeValueAsString(new AirportInfoFetcher().fetch(Airport.findAirport(airportCode))));
  }
  public static Result flights2(String departAirportCode, String arrivalAirportCode) throws JsonGenerationException, JsonMappingException, IOException, ParseException{
	  return ok(new ObjectMapper().writeValueAsString(new AirportInfoFetcher().fetch(Airport.findAirport(departAirportCode),  Airport.findAirport(arrivalAirportCode))));
  }
}