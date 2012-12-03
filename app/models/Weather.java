package models;

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
}
