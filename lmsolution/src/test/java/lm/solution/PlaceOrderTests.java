package lm.solution;

import org.testng.annotations.Test;

import lm.solution.ApiCallHandler;
import lm.solution.CommonUtils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;

public class PlaceOrderTests {
	private static Logger logger = Logger.getLogger("LOG");
	static String testCaseName;
	static double fareBelowTwoKm;
	static double fareAboveTwoKm;
	static double rateBelowTwoKmNightCharge;
	static double rateAboveTwoKmNightCharge;
	private static ApiCallHandler apiCallHandlerObj = CommonUtils.apiCallHandlerObj;
	private static String field_fare= "fare";
	private static String field_distance= "drivingDistancesInMeters";
	private static String field_amount= "amount";
	private static String field_currency= "currency";
	private static String field_message= "message";
	private static String field_stops= "stops";
	private static String field_orderAt= "OrderAT";
	
	

	/**
	 * description : Verifies place order functionality and checks generation of
	 * valid ID
	 */
	@Test
	public void Testcase_placeOrder_verifyID() {

		JSONObject inputJSon = new JSONObject();
		int id = 0;
		try {

			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);

			logger.info("ID is : " + id);

			Assert.assertTrue(id != 0, "ID Not Generated ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies place order functionality and checks currency field
	 * 
	 */
	@Test
	public void Testcase_placeOrder_verifyCurrency() {

		JSONObject inputJSon = new JSONObject(), outputJSon;
		String expectedCurrency;
		int id = 0;
		try {

			// Create test data
			expectedCurrency = CommonUtils.getExcelData("TestData", "currency");
			inputJSon = CommonUtils.getDefaultJSON(false);

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			// fetch currency from response
			outputJSon = apiCallHandlerObj.getResponseJson().getJSONObject(field_fare);
			String actualcurrency = outputJSon.getString(field_currency);
			logger.info("ID is : " + id);

			Assert.assertEquals(actualcurrency, expectedCurrency, "Currency Not Correct ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies place order functionality and checks Number of
	 * distance counts are correct as per provided json
	 */
	@Test()
	public void Testcase_placeOrder_verifyDistancesCount() {

		JSONObject inputJSon = new JSONObject();
		int expectedCount;
		try {

			JSONArray actualDistances = new JSONArray();
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);
			expectedCount = inputJSon.getJSONArray(field_stops).length() - 1;

			// Place Order
			CommonUtils.placeOrder(inputJSon);
			// Fetch driving distance from response
			actualDistances = apiCallHandlerObj.getResponseJson().getJSONArray(field_distance);
			logger.info("Length is : " + actualDistances.length());

			Assert.assertEquals(actualDistances.length(), expectedCount, "Number of distances do not match ");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies place order functionality and checks that fare is
	 * correctly generated if distance is less than 2 kms
	 */
	@Test()
	public void Testcase_placeOrder_verifyFareBelowTwoKms() {

		JSONObject inputJSon, outputJSon;
		double expectedFare;
		double actualFare;
		try {
			
			// Create test data
			inputJSon = new JSONObject(CommonUtils.getExcelData("TestCaseParams", testCaseName));
			expectedFare = Double.parseDouble(CommonUtils.getExcelData("TestData", "Fare_LessThan_2KM"));

			// Place Order
			CommonUtils.placeOrder(inputJSon);

			// fetch fare from response
			outputJSon = apiCallHandlerObj.getResponseJson().getJSONObject(field_fare);
			actualFare = outputJSon.getDouble(field_amount);
			logger.info("Fare is : " + actualFare);

			Assert.assertEquals(actualFare, expectedFare, "Fare amount do not match ");

		} catch (Exception e) {
			logger.error("Exception is : " + e.getMessage());
		}

	}

	/**
	 * description : Verifies place order functionality and that fare is correctly
	 * generated if distance is more than 2 kms
	 */
	@Test()
	public void Testcase_placeOrder_verifyFareAboveTwoKms() {
		JSONObject inputJSon = new JSONObject(), outputJSon1;
		double expectedFare;
		double fareRate, baseFare, rateKms;
		double actualFare;
		try {

			// Create test data
			inputJSon = new JSONObject(CommonUtils.getExcelData("TestCaseParams", testCaseName));
			fareRate = Double.parseDouble(CommonUtils.getExcelData("TestData", "FareRate_MoreThan_2KM"));
			baseFare = Double.parseDouble(CommonUtils.getExcelData("TestData", "Fare_LessThan_2KM"));
			rateKms = Double.parseDouble(CommonUtils.getExcelData("TestData", "RateKMS"));

			// Place Order
			CommonUtils.placeOrder(inputJSon);

			// calculate expected fare
			expectedFare = CommonUtils.calculateFare(apiCallHandlerObj.getResponseJson(), baseFare, fareRate, rateKms);
			// fetch actual fare
			outputJSon1 = apiCallHandlerObj.getResponseJson().getJSONObject(field_fare);
			actualFare = outputJSon1.getDouble(field_amount);
			logger.info("Additional Fare is : " + actualFare);

			Assert.assertEquals(actualFare, expectedFare, "Fare amount do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies place advance order functionality and checks
	 * generation of valid IDs
	 */
	@Test()
	public void Testcase_placeAdvanceOrder_VerifyId() {
		JSONObject inputJSon = new JSONObject();
		int id = 0;
		try {

			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(true);

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);

			logger.info("Advance ID is : " + id);
			Assert.assertTrue(id != 0, "ID Not Generated ");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies place advance order functionality and checks Number of
	 * distance counts are correct as per provided json
	 */
	@Test()
	public void Testcase_placeAdvanceOrder_verifyDistancesCount() {

		JSONObject inputJSon = new JSONObject();
		int expectedCount = 2;

		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(true);
			expectedCount = inputJSon.getJSONArray(field_stops).length() - 1;

			// Place Order
			CommonUtils.placeOrder(inputJSon);

			// Fetch distance
			JSONArray actualDistances = apiCallHandlerObj.getResponseJson().getJSONArray(field_distance);
			logger.info("Length for Advance order is : " + actualDistances.length());

			Assert.assertEquals(actualDistances.length(), expectedCount, "Number of distances do not match ");

		} catch (Exception e) { // TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * description : Verifies place advance order functionality and checks that fare
	 * is correctly generated if distance is less than 2 kms
	 */
	@Test()
	public void Testcase_placeAdvanceOrder_verifyFareBelowTwoKms() {

		JSONObject inputJSon = new JSONObject(), outputJSon1;
		double expectedFare;
		double actualFare;
		try {
			expectedFare = Double.parseDouble(CommonUtils.getExcelData("TestData", "Fare_LessThan_2KM"));
			// Create test data
			inputJSon = new JSONObject(CommonUtils.getExcelData("TestCaseParams", testCaseName));
			inputJSon.put(field_orderAt, CommonUtils.getFuturePastDate(true, false, 0));

			// Place Order
			CommonUtils.placeOrder(inputJSon);

			// Fetch fare
			outputJSon1 = apiCallHandlerObj.getResponseJson().getJSONObject(field_fare);
			actualFare = outputJSon1.getFloat(field_amount);
			logger.info("Fare for Advance booking is : " + actualFare);

			Assert.assertEquals(actualFare, expectedFare, "Fare amount do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * description : Verifies place advance order functionality and that fare is
	 * correctly generated if distance is more than 2 kms
	 */
	@Test()
	public void Testcase_placeAdvanceOrder_verifyFareAboveTwoKms() {
		JSONObject inputJSon = new JSONObject(), outputJSon1;

		double expectedFare;
		double fareRate, baseFare, rateKms;
		double actualFare;
		try {

			// Create test data
			inputJSon = new JSONObject(CommonUtils.getExcelData("TestCaseParams", testCaseName));
			inputJSon.put(field_orderAt, CommonUtils.getFuturePastDate(true, false, 0));
			fareRate = Double.parseDouble(CommonUtils.getExcelData("TestData", "FareRate_MoreThan_2KM"));
			baseFare = Double.parseDouble(CommonUtils.getExcelData("TestData", "Fare_LessThan_2KM"));
			rateKms = Double.parseDouble(CommonUtils.getExcelData("TestData", "RateKMS"));

			// Place Order
			CommonUtils.placeOrder(inputJSon);

			// calculate expected fare
			expectedFare = CommonUtils.calculateFare(apiCallHandlerObj.getResponseJson(), baseFare, fareRate, rateKms);

			// Fetch fare
			outputJSon1 = apiCallHandlerObj.getResponseJson().getJSONObject("fare");
			actualFare = outputJSon1.getDouble("amount");
			logger.info("Additional Fare for Advance booking is : " + actualFare);

			Assert.assertEquals(actualFare, expectedFare, "Fare amount do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block

			Assert.fail(e.getMessage());
		}
	}

	/**
	 * description : Verifies place advance order functionality and that NIGHT fare
	 * is correctly generated if distance is less than 2 kms
	 */
	@Test()
	public void Testcase_placeAdvanceOrder_verifyFareBelowTwoKms_NightCharges() {

		JSONObject inputJSon = new JSONObject(), outputJSon1;
		double expectedFare;
		double actualFare;
		try {
			expectedFare = Double.parseDouble(CommonUtils.getExcelData("TestData", "Fare_LessThan_2KM_Night"));
			// Create test data
			inputJSon = new JSONObject(CommonUtils.getExcelData("TestCaseParams", testCaseName));
			inputJSon.put(field_orderAt, CommonUtils.getFuturePastDate(true, true, 0));

			// Place Order
			CommonUtils.placeOrder(inputJSon);

			// fetch fare
			outputJSon1 = apiCallHandlerObj.getResponseJson().getJSONObject(field_fare);
			actualFare = outputJSon1.getFloat(field_amount);
			logger.info("Night Fare for Advance booking is : " + actualFare);

			Assert.assertEquals(actualFare, expectedFare, "Fare amount do not match ");
		} catch (Exception e) { //
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * description : Verifies place advance order functionality and that NIGHT fare
	 * is correctly generated if distance is less than 2 kms
	 */
	@Test()
	public void Testcase_placeAdvanceOrder_verifyFareAboveTwoKms_NightCharges() {
		JSONObject inputJSon = new JSONObject(), outputJSon1;
		double fareRate, baseFare, rateKms, expectedFare;
		double actualFare;
		try {

			// Create test data
			inputJSon = new JSONObject(CommonUtils.getExcelData("TestCaseParams", testCaseName));
			inputJSon.put(field_orderAt, CommonUtils.getFuturePastDate(true, false, 0));
			fareRate = Double.parseDouble(CommonUtils.getExcelData("TestData", "FareRate_MoreThan_2KM"));
			baseFare = Double.parseDouble(CommonUtils.getExcelData("TestData", "Fare_LessThan_2KM"));
			rateKms = Double.parseDouble(CommonUtils.getExcelData("TestData", "RateKMS"));

			// Place Order
			CommonUtils.placeOrder(inputJSon);

			// calculate expected fare
			expectedFare = CommonUtils.calculateFare(apiCallHandlerObj.getResponseJson(), baseFare, fareRate, rateKms);

			// Fetch fare
			outputJSon1 = apiCallHandlerObj.getResponseJson().getJSONObject(field_fare);
			actualFare = outputJSon1.getDouble(field_amount);
			logger.info("Additional  Night Fare for Advance booking is : " + actualFare);

			Assert.assertEquals(actualFare, expectedFare, "Fare amount do not match ");

		} catch (Exception e) { //
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
	
		}

	}

	/*
	 * description : Negative Scenario : Verify the behavior when only single Stop
	 * is sent in request valid ID
	 */
	@Test
	public void Testcase_placeOrder_verifySingleStopValue() {

		JSONObject inputJSon = new JSONObject();
		int id = 0;
		String actualMessage = null,expectedMessage;
		try {

			// Create test data
			inputJSon = new JSONObject(CommonUtils.getExcelData("TestCaseParams", testCaseName));
			expectedMessage = CommonUtils.getExcelData("ErrorMessages", "ErrorMessage_IncorrectField_Stops");

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);

			// Fetch message
			if(id==0)
			{
				actualMessage= apiCallHandlerObj.getResponseJson().getString(field_message);
			}
			logger.info("Message for incorrect stops is : " + actualMessage);


			Assert.assertEquals(actualMessage, expectedMessage, "Failure Message do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		testCaseName = method.getName();
		logger.info("Starting test case :" + testCaseName);
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {

		if (result.isSuccess())
			logger.info(testCaseName + " PASSED.");
		else
			logger.info(testCaseName + " FAILED");

	}

}
