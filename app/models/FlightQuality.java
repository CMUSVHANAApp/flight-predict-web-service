package models;


import java.util.Date;

public class FlightQuality{
	protected String flightNumber;
	protected int departDelay;
	protected int arrivalDelay;
	protected City departAirport;
	protected City arrivalAirport;
	protected Weather departWeather;
	protected Weather arrivalWeather;
	protected String airline;
	protected Date date;
	
	public FlightQuality(String airline, String flightNumber, Date d, String departure, String arrival){
		this.flightNumber = flightNumber;
		this.date = d;
		this.departAirport = new City(departure);
		this.airline = airline;
		this.arrivalAirport = new City(arrival);
		this.departWeather = WeatherFetcher.Fetch(departure, d);
		this.departWeather = WeatherFetcher.Fetch(arrival, d);
	}
	public String getAirline(){
		return this.airline;
	}
	public Date getDate(){
		return this.date;
	}
	public Weather getDepartWeather(){
		return this.departWeather;
	}
	public Weather getArrivalWeather(){
		return this.arrivalWeather;
	}
	public City getDepartAirport(){
		return this.departAirport;
	}
	public City getArrivalAirport(){
		return this.arrivalAirport;
	}
	
	public int  getDepartDelay(){
		return this.departDelay;
	}
	public int getArrivalDelay(){
		return this.arrivalDelay;
	}
	
	public void setDelay(int depart, int arrival){
		this.departDelay = depart;
		this.arrivalDelay = arrival;
	}
	public String toString(){
		return "Flight Number: " + this.flightNumber + "'s Service quality: \n" +
				"Delay on departure in " + this.departDelay + " minutes\n" +
				"Delay on Arrival in " + this.arrivalDelay + " minutes."; 
	}
}
