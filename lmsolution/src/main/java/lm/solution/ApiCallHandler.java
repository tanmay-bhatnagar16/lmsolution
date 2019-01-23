package lm.solution;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class ApiCallHandler

{
	private  int statusCode;
	private  JSONObject responseJSON;
	private static String url;
	private  String placeOrderEp;
	private  String takeOrderEp;
	private  String fetchOrderEp;
	private  String completeOrderEp;
	private  String cancelOrderEp;

	public  JSONObject postRequest(String endPoint, JSONObject inputJSon)
			throws ParseException, IOException, InterruptedException {
		responseJSON = null;
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(endPoint);

		post.setHeader("Content-type", "application/json");

		StringEntity se = new StringEntity(inputJSon.toString());
		post.setEntity(se);

		HttpResponse response = client.execute(post);
		Thread.sleep(1000);
		setstatusCode(response.getStatusLine().getStatusCode());

		HttpEntity entity = response.getEntity();

		String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

		JSONObject o = new JSONObject(json);

		client.close();
		return o;

	}

	public  JSONObject getRequest(String endPoint, String urlParameters)
			throws ParseException, IOException, InterruptedException {
		responseJSON = null;
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(endPoint + urlParameters);
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		Thread.sleep(1000);
		setstatusCode(response.getStatusLine().getStatusCode());
		String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

		JSONObject o = new JSONObject(json);
		setResponseJson(o);
		client.close();
		return o;
	}

	public  JSONObject putRequest(String endPoint, String urlParameters)
			throws ParseException, IOException, InterruptedException {
		responseJSON = null;
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPut request = new HttpPut(endPoint + urlParameters);
		HttpResponse response = client.execute(request);
		Thread.sleep(1000);
		setstatusCode(response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();

		String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
		JSONObject o = new JSONObject(json);
		setResponseJson(o);
		client.close();
		return o;
	}

	// get and Set methods for private members
	public  int getstatusCode() {
		return statusCode;
	}

	public  void setstatusCode(int code) {
		statusCode = code;
	}

	public  JSONObject getResponseJson() {
		return responseJSON;
	}

	public  void setResponseJson(JSONObject json) {
		responseJSON = json;
	}

	public static String getHostUrl() {
		return url;
	}

	public static void setHostUrl(String urlString) {
		url = urlString;
	}

	public  String getPlaceOrderEp() {
		return placeOrderEp;
	}

	public  void setPlaceOrderEp(String url) {
		placeOrderEp = url;
	}

	public  String getTakeOrderEP() {
		return takeOrderEp;
	}

	public  void setTakeOrderEp(String url) {
		takeOrderEp = url;
	}

	public  String getFetchOrderEP() {
		return fetchOrderEp;
	}

	public  void setFetchOrderEp(String url) {
		fetchOrderEp = url;
	}

	public  String getCompleteOrderEP() {
		return completeOrderEp;
	}

	public  void setCompleteOrderEp(String url) {
		completeOrderEp = url;
	}

	public  String getCancelOrderEP() {
		return cancelOrderEp;
	}

	public  void setCancelOrderEp(String url) {
		cancelOrderEp = url;
	}

}
