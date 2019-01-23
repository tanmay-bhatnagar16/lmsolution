package lm.solution;

import org.testng.annotations.Test;

import lm.solution.ApiCallHandler;
import lm.solution.CommonUtils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;

public class CancelOrderTests {
	private static Logger logger = Logger.getLogger("LOG");
	private static String field_status = "status";
	private static String field_message = "message";
	private static ApiCallHandler apiCallHandlerObj = CommonUtils.apiCallHandlerObj;


	/**
	 * description : Verifies Cancel order functionality when valid order Id is
	 * provided as input. Flow : ASSINGING to Cancelled
	 */
	@Test()
	public void TestCase_Cancel_Order_VerifyStatusCode_ValidID_AfterPlaceOrder() {

		JSONObject inputJSon;
		int id = 0, statusCode = 0;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			CommonUtils.cancelOrder(id);

			// Get status code
			statusCode = apiCallHandlerObj.getstatusCode();
			logger.info("Valid Status Code for Cancel Order" + statusCode);

			Assert.assertEquals(statusCode, HttpStatus.SC_OK, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies Order status for Cancel order functionality when valid
	 * order Id is provided as input. Flow : ASSINGING to Cancelled
	 */
	@Test()
	public void TestCase_Cancel_Order_VerifyOrderstatus_ValidID_AfterPlaceOrder() {

		JSONObject inputJSon, outputJson;
		int id = 0;
		String actualStatus, expectedStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);
			expectedStatus = CommonUtils.getExcelData("TestData", "Status_Cancelled");

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			// Cancel Order
			outputJson = CommonUtils.cancelOrder(id);
			actualStatus = outputJson.getString(field_status);

			logger.info("Valid Status " + actualStatus);
			Assert.assertEquals(actualStatus, expectedStatus, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies Order status for Cancel order functionality when valid
	 * order Id is provided as input. Flow : OnGoing to Cancelled
	 */
	@Test()
	public void TestCase_Cancel_Order_VerifyOrderstatus_ValidID_AfterTakeOrder() {

		JSONObject inputJSon, outputJson;
		int id = 0;
		String actualStatus, expectedStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);
			expectedStatus = CommonUtils.getExcelData("TestData", "Status_Cancelled");

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			// Take Order
			CommonUtils.takeOrder(id);
			Assert.assertTrue(apiCallHandlerObj.getResponseJson().has("id"), "Take Order failed");

			// Cancel Order
			outputJson = CommonUtils.cancelOrder(id);
			actualStatus = outputJson.getString(field_status);

			logger.info("Valid Status " + actualStatus);
			Assert.assertEquals(actualStatus, expectedStatus, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies cancel order functionality when valid order Id is
	 * provided as input. Flow : ONGOING to Cancelled
	 */
	@Test()
	public void TestCase_Cancel_Order_VerifyStatusCode_ValidID_AfterTakeOrder() {

		JSONObject inputJSon;
		int id = 0, statusCode = 0;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			// Take Order
			CommonUtils.takeOrder(id);
			Assert.assertTrue(apiCallHandlerObj.getResponseJson().has("id"), "Take Order failed");

			CommonUtils.cancelOrder(id);

			// Get status code
			statusCode = apiCallHandlerObj.getstatusCode();
			logger.info("Valid Status Code for Cancel Order" + statusCode);

			Assert.assertEquals(statusCode, HttpStatus.SC_OK, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies cancel order functionality when invalid order Id is
	 * provided as input
	 */
	@Test()
	public void TestCase_Cancel_Order_VerifyStatusCode_InvalidID() {
		JSONObject inputJSon;
		int id = 0, statusCodeExpected = HttpStatus.SC_NOT_FOUND, actualStatus;
		try {

			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);
			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			// Cancel Order for invalid id
			CommonUtils.cancelOrder((id + 1));

			// Get status code
			actualStatus = apiCallHandlerObj.getstatusCode();
			logger.info("InValid Status Code for Cancel Order" + actualStatus);

			Assert.assertEquals(actualStatus, statusCodeExpected, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies cancel order functionality when invalid flow is
	 * followed : from Completed to Cancel
	 */
	@Test()
	public void TestCase_cancel_Order_VerifyStatusCode_InvalidFlow() {
		JSONObject inputJSon;
		int id = 0, statusCodeExpected = HttpStatus.SC_UNPROCESSABLE_ENTITY, actualStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			// Take Order
			CommonUtils.takeOrder(id);
			Assert.assertTrue(apiCallHandlerObj.getResponseJson().has("id"), "Take Order failed");

			// Complete Order
			CommonUtils.completeOrder(id);
			Assert.assertTrue(apiCallHandlerObj.getResponseJson().has("id"), "Complete Order failed");

			// Cancel Order
			CommonUtils.cancelOrder(id);

			actualStatus = apiCallHandlerObj.getstatusCode();
			logger.info("InValid Status Code for Incorrect flow of Cancel Order" + actualStatus);

			Assert.assertEquals(actualStatus, statusCodeExpected, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies Error Message for cancel order functionality when
	 * invalid order Id is provided as input
	 */
	@Test()
	public void TestCase_Cancel_Order_VerifyErrorMessage_InvalidID() {
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

			// cancel order with incorrect id
			CommonUtils.cancelOrder((id + 1));

			// Get status message
			actualStatus = apiCallHandlerObj.getResponseJson().getString(field_message);

			logger.info("Error Message" + actualStatus);
			Assert.assertEquals(actualStatus, expectedStatus, "Error Message do not match ");

		} catch (Exception e) {
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
