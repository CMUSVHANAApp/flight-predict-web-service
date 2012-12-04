package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import models.Prediction;
import models.WeatherFetcher;
import models.Weather;
import play.*;
import play.libs.Json;
import play.mvc.*;

import views.html.*;
import views.*;

public class Application extends Controller {
  
  public static Result index() {
    return ok(home.render());
  }
  public static Result weathers(String city){
	  ArrayList<Weather> ws = WeatherFetcher.Fetch(city);
	  ArrayList<HashMap> r = new ArrayList<HashMap>(); 
	  //return TODO;
	  Iterator<Weather> w_it = ws.iterator();
	  while(w_it.hasNext()){
		  Weather w = w_it.next();
		  r.add(w.toHashMap());
	  }	  
	  
	  return ok(new Json().toJson(r).toString());
  }
  public static Result predictions(String flightNumber){
	  Prediction p = new Prediction();
	  
	  //return ok(p.predict(flightNumber).toString());
	  return ok(p.predict(flightNumber).toJson());
  }
  
}