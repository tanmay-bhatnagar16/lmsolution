package lm.solution;

import org.testng.annotations.Test;

import lm.solution.ApiCallHandler;
import lm.solution.CommonUtils;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import java.lang.reflect.Method;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;

public class FetchOrderTests {
	private static Logger logger = Logger.getLogger("LOG");

	private static String field_status = "status";
	private static String field_message = "message";
	private static String field_stops = "stops";
	private static String field_distance = "drivingDistancesInMeters";
	private static String field_fare = "fare";
	private static String field_amount = "amount";
	private static ApiCallHandler apiCallHandlerObj = CommonUtils.apiCallHandlerObj;
	/**
	 * description : Verifies fetch order functionality when valid order Id is
	 * provided as input
	 */
	@Test()
	public void TestCase_Fetch_Order_VerifyStatusCode_ValidID() {

		JSONObject inputJSon;
		int id = 0, statusCode = 0;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);

			// Place order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			CommonUtils.fetchOrder(id);
			// Get status code
			statusCode = apiCallHandlerObj.getstatusCode();
			logger.info("Valid Status Code" + statusCode);

			Assert.assertEquals(statusCode, HttpStatus.SC_OK, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies Order status in fetch order functionality for an order
	 * just placed and when valid order Id is provided as input
	 */
	@Test()
	public void TestCase_Fetch_Order_VerifyOrderStatusForPlacedOrder() {

		JSONObject inputJSon;
		int id = 0;
		String actualStatus, expectedStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);
			expectedStatus = CommonUtils.getExcelData("TestData", "Status_Assigning");

			// Place order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			CommonUtils.fetchOrder(id);
			// Get status code
			actualStatus = apiCallHandlerObj.getResponseJson().getString(field_status);
			logger.info("Valid Status " + actualStatus);

			Assert.assertEquals(actualStatus, expectedStatus, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies fetch order functionality when invalid order Id is
	 * provided as input
	 */
	@Test()
	public void TestCase_Fetch_Order_VerifyStatusCode_InvalidID() {
		JSONObject inputJSon;
		int id = 0, actualStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);

			// Place order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			// fetch order with incorrect id
			CommonUtils.fetchOrder((id + 1));

			// Get status code
			actualStatus = apiCallHandlerObj.getstatusCode();

			logger.info("InValid Status Code" + actualStatus);
			Assert.assertEquals(actualStatus, HttpStatus.SC_NOT_FOUND, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies Error Message for fetch order functionality when
	 * invalid order Id is provided as input
	 */
	@Test()
	public void TestCase_Fetch_Order_VerifyErrorMessage_InvalidID() {
		JSONObject inputJSon;
		int id = 0;
		String actualStatus, expectedStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);
			expectedStatus = CommonUtils.getExcelData("ErrorMessages", "ErrorMessage_OrderNotFound");
			// Place order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			// fetch order with incorrect id
			CommonUtils.fetchOrder((id + 1));

			// Get status code
			actualStatus = apiCallHandlerObj.getResponseJson().getString(field_message);

			logger.info("Error Message" + actualStatus);
			Assert.assertEquals(actualStatus, expectedStatus, "Error Message do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies JSON Fields "Stops" for fetch order functionality when
	 * valid order Id is provided as input
	 */
	@Test()
	public void TestCase_Fetch_Order_VerifyStopsCounts_ValidID() {

		JSONObject inputJSon = new JSONObject();
		int expectedCount, id = 0;
		try {

			JSONArray actualDistances = new JSONArray();
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);
			expectedCount = inputJSon.getJSONArray(field_stops).length();

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			CommonUtils.fetchOrder(id);
			// Fetch driving distance from response
			actualDistances = apiCallHandlerObj.getResponseJson().getJSONArray(field_stops);
			logger.info("Length is : " + actualDistances.length());

			Assert.assertEquals(actualDistances.length(), expectedCount, "Number of Stops do not match ");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}


	/** description : Verifies JSON Fields "Driving distances" for fetch order
	 * functionality when valid order Id is provided as input
	 */
	@Test()
	public void TestCase_Fetch_Order_VerifyDrivingDistances_ValidID() {

		JSONObject inputJSon = new JSONObject();
		int id = 0;
		try {

			JSONArray actualDistances, expectedDistances = new JSONArray();
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");
			expectedDistances = apiCallHandlerObj.getResponseJson().getJSONArray(field_distance);

			CommonUtils.fetchOrder(id);
			// Fetch driving distance from response
			actualDistances = apiCallHandlerObj.getResponseJson().getJSONArray(field_distance);
			logger.info("Length is : " + actualDistances.length());
			Assert.assertEquals(actualDistances.length(), expectedDistances.length(),
					"Number of Distances  do not match ");

			for (int i = 0; i < actualDistances.length(); i++) {
				Assert.assertEquals(actualDistances.getDouble(i), expectedDistances.getDouble(i),
						"Distance value mismatch");

			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}


	 /** description : Verifies JSON Fields "Stops" for fetch order functionality when
	 * valid order Id is provided as input
	 */
	@Test()
	public void TestCase_Fetch_Order_VerifyFareAmount_ValidID() {

		JSONObject inputJSon = new JSONObject();
		int id = 0;
		try {

			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");
			// fetch fare from place order response
			JSONObject outputJSon = apiCallHandlerObj.getResponseJson().getJSONObject(field_fare);
			double expectedFare = outputJSon.getDouble(field_amount);

			CommonUtils.fetchOrder(id);
			// fetch fare from fetch order response
			outputJSon = apiCallHandlerObj.getResponseJson().getJSONObject(field_fare);
			double actualFare = outputJSon.getDouble(field_amount);

			logger.info("Fare is : " + actualFare);

			Assert.assertEquals(actualFare, expectedFare, "Fare mismatch ");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		logger.info("Starting test case :" + method.getName());
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {

		if (result.isSuccess())
			logger.info(result.getMethod().getMethodName() + " PASSED.");
		else
			logger.info(result.getMethod().getMethodName() + " FAILED");

	}

}
