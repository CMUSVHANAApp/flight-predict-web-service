package models;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import models.fetcher.WeatherFetcher;
import models.fetcher.YelpFetcher;

public class FlightQuality{
	protected String flightNumber;
	protected int departDelay;
	protected int arrivalDelay;
	protected Airport departAirport;
	protected Airport arrivalAirport;
	protected Weather departWeather;
	protected Weather arrivalWeather;
	protected String airline;
	protected Date date;
	protected Date arrivalDate;
	protected HashMap<String, YelpRecommendations> recommendations;
	public FlightQuality(String airline, String flightNumber, Date arrivalDate, Date departureDate){
		this.airline = airline;
		this.flightNumber = flightNumber;
		this.arrivalDate = arrivalDate;
		this.date = departureDate;
		this.recommendations = new HashMap<String, YelpRecommendations>();
	}
	public FlightQuality(String flightNumber, Date arrivalDate, Date departureDate){
		this("", flightNumber, arrivalDate, departureDate);
	}
	public FlightQuality(String airline, String flightNumber, Date d, String departure, String arrival){
		this(airline, flightNumber, d, departure, arrival, true);
	}
	public FlightQuality(String airline, String flightNumber, Date d, String departure, String arrival, boolean fetchData){
		this.flightNumber = flightNumber;
		this.date = d;
		this.departAirport = Airport.findAirport(departure);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		Random r = new Random();
		cal.set(Calendar.HOUR, r.nextInt(20));
		cal.set(Calendar.MINUTE, r.nextInt(60));
		this.date = cal.getTime();
		
		
		cal.add(Calendar.HOUR, r.nextInt(23));
		cal.add(Calendar.MINUTE, r.nextInt(60));
		
		this.arrivalDate = cal.getTime();
		this.airline = airline;
		this.arrivalAirport = Airport.findAirport(arrival);
		this.recommendations = new HashMap<String, YelpRecommendations>();
	}
	public Date getArrivalDateTime(){
		return this.arrivalDate;
	}
	public Date getDepartureDateTime(){
		return this.date;
	}
	public String getArrivalDate(){
		return this.arrivalDate.toGMTString();
	}
	public String getAirline(){
		return this.airline;
	}
	public String getDate(){
		return this.date.toGMTString();
	}
	public Weather getDepartWeather(){
		return this.departWeather;
	}
	public Weather getArrivalWeather(){
		return this.arrivalWeather;
	}
	public Airport getDepartAirport(){
		return this.departAirport;
	}
	public Airport getArrivalAirport(){
		return this.arrivalAirport;
	}
	public HashMap<String, YelpRecommendations> getRecommendations(){
		return this.recommendations;
	}
	
	public int  getDepartDelay(){
		return this.departDelay;
	}
	public int getArrivalDelay(){
		return this.arrivalDelay;
	}
	public String getFlightNumber(){
		return this.flightNumber;
	}
	public void setDelay(int depart, int arrival){
		this.departDelay = depart;
		this.arrivalDelay = arrival;
		
	}
	public void setDepartureAirport(String airport){
		this.departAirport = Airport.findAirport(airport);
	}
	public void setArrivalAirport(String airport){
		this.arrivalAirport = Airport.findAirport(airport);
	}
	public void setAirline(String airline){
		this.airline = airline;
	}
	public void fetchWeather(){
		this.departWeather = WeatherFetcher.Fetch(this.departAirport.getGeoLocation().city, this.date);
		this.arrivalWeather = WeatherFetcher.Fetch(this.arrivalAirport.getGeoLocation().city, this.arrivalDate);
	}
	public void fetchRecommendations(){

		YelpFetcher yf = new YelpFetcher();
		if(this.departDelay <= 120 && this.departDelay > 0){
			this.recommendations.put("Dining", yf.fetch("fast+food", this.departAirport.getGeoLocation()));
		}
		
		else if(this.departDelay >= 120){
			this.recommendations.put("Dining", yf.fetch("restaurant", this.departAirport.getGeoLocation()));
		}
		
		else{
			this.recommendations.put("Dining", yf.fetch("restaurant", this.arrivalAirport.getGeoLocation()));
		}
		
		if(this.departDelay > 0){
			this.recommendations.put("Accomendation", yf.fetch("hotel", this.departAirport.getGeoLocation()));
			this.recommendations.put("Transportation", yf.fetch("transportation", this.departAirport.getGeoLocation()));
		}
		
		else{
			this.recommendations.put("Accomendation", yf.fetch("hotel", this.arrivalAirport.getGeoLocation()));
			this.recommendations.put("Transportation", yf.fetch("transportation", this.departAirport.getGeoLocation()));
		}
		
	}
	public String toString(){
		return "Flight Number: " + this.flightNumber + "'s Service quality: \n" +
				"Delay on departure in " + this.departDelay + " minutes\n" +
				"Delay on Arrival in " + this.arrivalDelay + " minutes."; 
	}
}
