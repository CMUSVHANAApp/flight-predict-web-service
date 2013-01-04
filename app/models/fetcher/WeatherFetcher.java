package models.fetcher;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import models.Airport;
import models.Weather;

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
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import play.Logger;
import scala.util.Random;

public class WeatherFetcher {
	public static void main(String[] args) {
		String city = "Mountain View, CA+States";
		System.out.println("Weather of " + city + ":");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +1);
		System.out.println(WeatherFetcher.Fetch(city,  cal.getTime()));
	}

	public static Weather Fetch(String city, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
	  
		
		Date yesterday = cal.getTime();
		if (date.before(yesterday)) {
			Airport a = Airport.findAirport(city);
			Weather w = FetchHistory(a, date);
			return w;
		} else {
			Logger.warn(date.toLocaleString());
			HashMap<Date, Weather> ws = Fetch(city);
			Date d = new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0);
			if (ws.containsKey(d)) {
				return ws.get(d);
			}
			
		}
		return null;
	}

	public static Weather FetchHistory(Airport airport, Date d) {
		HttpClient httpclient = new DefaultHttpClient();
		Weather w = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dateString = format.format(d);
		URIBuilder uri;
		try {
			uri = new URIBuilder("http://weather-api.net/api/query.php");
			uri.addParameter("key", "0d443aaac5e3dc33a02ed75c070af5705fe08c27");
			uri.addParameter("dates", dateString);
			uri.addParameter("precip", "yes");
			uri.addParameter("latitude", String.valueOf(airport.getGeoLocation().getLatitude()));
			uri.addParameter("longitude", String.valueOf(airport.getGeoLocation().getLongitude()));
			HttpGet httpget = new HttpGet(uri.build());
			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(httpget, responseHandler);
			ObjectMapper objectMapper = new ObjectMapper();
			//w = objectMapper.readValue(responseBody, Weather.class);
			JsonFactory factory = objectMapper.getJsonFactory();
			JsonParser jsonParser = factory.createJsonParser(responseBody);
			ObjectCodec oc = jsonParser.getCodec();
			JsonNode node = oc.readTree(jsonParser);
			if(node == null) return  null;
			try{
				node = node.get(node.get("station").get("ymd").asText());
			}
			catch(Exception e){
				return null;
			}
		
			Iterator<JsonNode> it = node.getElements();
			w = new Weather();
			while(it.hasNext()){
				try {
					node = it.next();
					SimpleDateFormat ds = new SimpleDateFormat("hhmm");
					Date wd = ds.parse(node.get("time").asText());
					if(wd.getHours() != d.getHours()) continue;
					ds = new SimpleDateFormat("yyyyMMdd");
					w.setDate(ds.parse(node.get("date").asText()));
					w.setDate(new Date(w.getDate().getYear(), w.getDate().getMonth(), w.getDate().getDate(), wd.getHours(), wd.getMinutes()));

					w.setVisibility(node.get("visibilityObDistDim").asInt());
					w.setTemp(node.get("airTemp").asDouble());
					w.setPressure(node.get("airPressureObSeaLevelPressure").asInt());
					w.setWindSpeed( node.get("windObSpeedRate").asDouble());
					return w;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return w;

	}

	public static HashMap<Date, Weather> Fetch(String airport) {

		HashMap<Date, Weather> dateWeather = new HashMap<Date, Weather>();
		HttpClient httpclient = new DefaultHttpClient();
		try {
			System.out.println("QUERY WEATHER:" + airport);
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			qparams.add(new BasicNameValuePair("q", airport));
			URIBuilder uri = new URIBuilder("http://free.worldweatheronline.com/feed/weather.ashx");
			uri.addParameter("q", airport);
			uri.addParameter("format", "json");
			uri.addParameter("num_of_days", "5");
			uri.addParameter("key", "c2c813d05c230948123011");

			HttpGet httpget = new HttpGet(uri.build());

			System.out.println("executing request " + httpget.getURI());

			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(httpget, responseHandler);
			JSONObject myjson = new JSONObject(responseBody)
					.getJSONObject("data");
			JSONObject current = myjson.getJSONArray("current_condition")
					.getJSONObject(0);

			JSONArray forecasts = myjson.getJSONArray("weather");

			Weather currentWeather = new Weather(new Date(),
					current.getDouble("temp_F"),
					current.getDouble("windspeedMiles"),
					current.getInt("visibility"), current.getInt("pressure"),
					current.getInt("weatherCode"), current
							.getJSONArray("weatherDesc").getJSONObject(0)
							.getString("value"), current
							.getJSONArray("weatherIconUrl").getJSONObject(0)
							.getString("value"));
			dateWeather.put(new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate()), currentWeather);
			for (int i = 0; i < forecasts.length(); i++) {
				JSONObject jw = forecasts.getJSONObject(i);
				SimpleDateFormat ds = new SimpleDateFormat("yyyy-MM-dd");
				ds.setTimeZone(TimeZone.getDefault());
				Date d = ds.parse(jw.getString("date"));
				Weather w = new Weather(
						d,
						(jw.getDouble("tempMaxF") + jw.getDouble("tempMinF")) / 2,
						jw.getDouble("windspeedMiles"), jw
								.getInt("weatherCode"), jw
								.getJSONArray("weatherDesc").getJSONObject(0)
								.getString("value"), jw
								.getJSONArray("weatherIconUrl")
								.getJSONObject(0).getString("value"));
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
