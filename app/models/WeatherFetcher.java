package models;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class WeatherFetcher {
	public static void main(String [ ] args){
		String city = "Mountain View, CA+States";
		System.out.println("Weather of " + city + ":");
		System.out.println(WeatherFetcher.Fetch(city));
		
	}
	public static Weather Fetch(String city, Date date){
		HashMap<Date, Weather> ws = Fetch(city);
		if(ws.containsKey(date)){
			return ws.get(date);
		}
		return null;
	}
	public static HashMap<Date, Weather> Fetch(String city){
		
		HashMap<Date, Weather> dateWeather = new HashMap<Date, Weather>();
		HttpClient httpclient = new DefaultHttpClient();
		try {

            List<NameValuePair> qparams = new ArrayList<NameValuePair>();
            qparams.add(new BasicNameValuePair("q", city));
            URIBuilder uri = new URIBuilder("http://free.worldweatheronline.com/feed/weather.ashx");
            uri.addParameter("q", city);
            uri.addParameter("format", "json");
            uri.addParameter("num_of_days", "5");
            uri.addParameter("key", "c2c813d05c230948123011");
            
            
            HttpGet httpget = new HttpGet(uri.build());
            
            System.out.println("executing request " + httpget.getURI());

            // Create a response handler
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(httpget, responseHandler);
            JSONObject myjson = new JSONObject(responseBody).getJSONObject("data");
            JSONObject current = myjson.getJSONArray("current_condition").getJSONObject(0);
            
            JSONArray forecasts = myjson.getJSONArray("weather");
           
            
            Weather currentWeather = new Weather(
            				new Date(),
            				current.getDouble("temp_F"), 
            				current.getDouble("windspeedMiles"), 
            				current.getInt("visibility"),
            				current.getInt("pressure"),
            				current.getInt("weatherCode"),
            				current.getJSONArray("weatherDesc").getJSONObject(0).getString("value"),
            				current.getJSONArray("weatherIconUrl").getJSONObject(0).getString("value")
            				); 
            dateWeather.put(new Date(), currentWeather);
            for(int i=0; i< forecasts.length(); i++){
            	JSONObject jw = forecasts.getJSONObject(i);
            	Date d = new SimpleDateFormat("yyyy-MM-dd").parse(jw.getString("date")); 
            	Weather w = new Weather(
            			d ,
            			(jw.getDouble("tempMaxF") + jw.getDouble("tempMinF")) / 2, 
            			jw.getDouble("windspeedMiles"),
            			jw.getInt("weatherCode"), 
        				jw.getJSONArray("weatherDesc").getJSONObject(0).getString("value"),
        				jw.getJSONArray("weatherIconUrl").getJSONObject(0).getString("value")
				);
            	dateWeather.put(d, w);
            	
            }
            
            
            
        } catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
		return dateWeather;
	}

}

