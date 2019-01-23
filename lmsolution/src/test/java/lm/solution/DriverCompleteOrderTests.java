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

public class DriverCompleteOrderTests {
	private static Logger logger = Logger.getLogger("LOG");
	private static String field_status = "status";
	private static String field_message = "message";
	private static String field_id = "id";
	private static ApiCallHandler apiCallHandlerObj = CommonUtils.apiCallHandlerObj;

	/**
	 * description : Verifies Complete order functionality when valid order Id is
	 * provided as input
	 */
	@Test()
	public void TestCase_Complete_Order_VerifyStatusCode_ValidID() {

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

			CommonUtils.completeOrder(id);

			// Get Status Code
			statusCode = apiCallHandlerObj.getstatusCode();
			logger.info("Valid Status Code for Complete Order" + statusCode);

			Assert.assertEquals(statusCode, HttpStatus.SC_OK, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies Complete order functionality when invalid order Id is
	 * provided as input
	 */
	@Test()
	public void TestCase_Complete_Order_VerifyStatusCode_InvalidID() {
		JSONObject inputJSon;
		int id = 0, statusCodeExpected = HttpStatus.SC_NOT_FOUND, actualStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");
			// Take Order
			CommonUtils.takeOrder(id);
			Assert.assertTrue(apiCallHandlerObj.getResponseJson().has("id"), "Take Order failed");

			// Complete Order for incorrect id
			CommonUtils.completeOrder((id + 1));

			// Get status
			actualStatus = apiCallHandlerObj.getstatusCode();
			logger.info("InValid Status Code for Complete Order" + actualStatus);

			Assert.assertEquals(actualStatus, statusCodeExpected, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies complete order functionality when logic flow is not
	 * followed : Cancelled to Completed
	 */
	@Test()
	public void TestCase_Complete_Order_VerifyStatusCode_InvalidFlow() {
		JSONObject inputJSon;
		int id = 0, statusCodeExpected = HttpStatus.SC_UNPROCESSABLE_ENTITY, actualStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			// Cancel Order
			CommonUtils.cancelOrder(id);
			Assert.assertTrue(apiCallHandlerObj.getResponseJson().has("id"), "Cancel Order failed");

			// Complete Order
			CommonUtils.completeOrder(id);

			// Get status
			actualStatus = apiCallHandlerObj.getstatusCode();
			logger.info("InValid Status Code for Incorrect flow of Complete Order" + actualStatus);

			Assert.assertEquals(actualStatus, statusCodeExpected, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies Order status for Complete order functionality when
	 * valid order Id is provided as input. Flow : ONGOING to Complete
	 */
	@Test()
	public void TestCase_Complete_Order_VerifyOrderStatus_ValidID() {

		JSONObject inputJSon, outputJson;
		int id = 0;
		String actualStatus, expectedStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);
			expectedStatus = CommonUtils.getExcelData("TestData", "Status_Completed");

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");
			// Take OrderF
			CommonUtils.takeOrder(id);
			Assert.assertTrue(apiCallHandlerObj.getResponseJson().has(field_id), "Take Order failed");

			// Complete Order and check status
			outputJson = CommonUtils.completeOrder(id);
			actualStatus = outputJson.getString(field_status);

			logger.info("Valid Status " + actualStatus);
			Assert.assertEquals(actualStatus, expectedStatus, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Negative scenario : Verifies error message for Complete order
	 * functionality when valid order Id is provided as input. Invalid Flow :
	 * ASSIGNING to Complete
	 */
	@Test()
	public void TestCase_Complete_Order_VerifyOrderStatus_InvalidFlow_AssignedToComplete() {

		JSONObject inputJSon, outputJson;
		int id = 0;
		String actualStatus, expectedStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);
			expectedStatus = CommonUtils.getExcelData("ErrorMessages", "ErrorMessage_OrderNotOnGoing");

			// Place Order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			// Complete Order and check status
			outputJson = CommonUtils.completeOrder(id);
			actualStatus = outputJson.getString(field_message);

			logger.info("Error Message " + actualStatus);

			Assert.assertEquals(actualStatus, expectedStatus, "Error message mismatch ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies Error Message for Complete order functionality when
	 * invalid order Id is provided as input
	 */
	@Test()
	public void TestCase_Complete_Order_VerifyErrorMessage_InvalidID() {
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
			
			// Take Order
			CommonUtils.takeOrder(id);
			Assert.assertTrue(apiCallHandlerObj.getResponseJson().has("id"), "Take Order failed");

			// Complete Order for incorrect id
			CommonUtils.completeOrder((id + 1));

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
