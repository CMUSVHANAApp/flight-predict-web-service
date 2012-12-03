package controllers;

import java.util.ArrayList;
import java.util.Iterator;

import models.Prediction;
import models.WeatherFetcher;
import models.Weather;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
  public static Result index() {
    return ok("Hello, world");
  }
  public static Result weathers(String city){
	  ArrayList<Weather> ws = WeatherFetcher.Fetch(city);
	  //return TODO;
	  String r ="";
	  Iterator<Weather> w_it = ws.iterator();
	  while(w_it.hasNext()){
		  Weather w = w_it.next();
		  r += w.toString() + "\n";
	  }
	  
	  return ok("weather of "+ city+ ":\n" +r);
  }
  public static Result predictions(String flightNumber){
	  Prediction p = new Prediction();
	  return ok(p.predict(flightNumber).toString());
  }
  
}