package models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class City implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6631141542987098608L;
	protected String name;
	protected GeoLocation geoLocation;
	public City(String name){
		this(name, true);
	}
	public City(String name, boolean fetchData){
		this.name = name;
		if(fetchData){
			this.geoLocation = GeoLocationFetcher.Fetch(name);
		}
	}
	
	/* getter & setter */
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public void setGeoLocation(GeoLocation g){
		this.geoLocation = g;
	}
	public GeoLocation getGeoLocation(){
		return this.geoLocation;
	}
	
	
}
