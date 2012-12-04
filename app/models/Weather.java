package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Weather {
	double temp; // in F
	double windspeed; // in miles
	int code;
	String description;
	String icon;
	public Weather(double temp, double windspeed, int code, String description, String icon){
		this.temp = temp;
		this.windspeed = windspeed;
		this.code = code;
		this.description = description;
		this.icon = icon;
	}
	public double getTemp(){
		return this.temp;
	}
	public double getWindSpeed(){
		return this.windspeed;
	}
	public int getWeatherCode(){
		return this.code;
	}
	public String getWeatherDescription(){
		return this.description;
	}
	public String getWeatßherIcon(){
		return this.icon;
	}
	public String toString(){
		return "Temp:" + String.valueOf(this.temp) + "F, windspeed: " + String.valueOf(this.windspeed) + "miles/hr, desc: " + this.description + ", icon: " + this.icon;
		
	}
	public HashMap<String, Object> toHashMap(){				
		HashMap<String, Object> d = new HashMap<String, Object>();
		d.put("temp_F", this.temp);
		d.put("windspeed_mile", this.windspeed);
		d.put("weatherCode", this.code);
		d.put("weatherDescription", this.description);
		d.put("weatherIcon", this.icon);
		return d;
		
	}
}
