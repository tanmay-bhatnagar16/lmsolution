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

public class DriverTakeOrderTests {
	private static Logger logger = Logger.getLogger("LOG");
	private static String field_status = "status";
	private static String field_message = "message";
	private static ApiCallHandler apiCallHandlerObj = CommonUtils.apiCallHandlerObj;


	/**
	 * description : Verifies take order functionality when valid order Id is
	 * provided as input
	 */
	@Test()
	public void TestCase_Take_Order_VerifyStatusCode_ValidID() {

		JSONObject inputJSon;
		int id = 0, statusCode = 0;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);
			// Place order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			CommonUtils.takeOrder(id);
			// fetch status code
			statusCode = apiCallHandlerObj.getstatusCode();
			logger.info("Valid Status Code for Take Order " + statusCode);

			Assert.assertEquals(statusCode, HttpStatus.SC_OK, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies take order functionality when valid order Id is
	 * provided as input . Flow : ASSIGNING To Ongoing
	 */
	@Test()
	public void TestCase_Take_Order_VerifyOrderStatus_ValidID() {

		JSONObject inputJSon, outputJson;
		int id = 0;
		String expectedStatus, actualStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);
			expectedStatus = CommonUtils.getExcelData("TestData", "Status_Ongoing");
			// Place order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			outputJson = CommonUtils.takeOrder(id);
			// Get status code
			actualStatus = outputJson.getString(field_status);
			logger.info("Valid Status " + actualStatus);

			Assert.assertEquals(actualStatus, expectedStatus, "Status do not match ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies take order functionality when valid order Id is
	 * provided as input
	 */
	@Test()
	public void TestCase_Take_Order_VerifyStatusCode_InvalidID() {
		JSONObject inputJSon;
		int id = 0, statusCodeExpected = HttpStatus.SC_NOT_FOUND, actualStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);
			// Place order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			// Take order
			CommonUtils.takeOrder((id + 1));

			// Fetch status code
			actualStatus = apiCallHandlerObj.getstatusCode();
			logger.info("InValid Status Code for Take Order" + actualStatus);

			Assert.assertEquals(actualStatus, statusCodeExpected, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies take order functionality when incorrect flow is
	 * followed : from Cancelled To Ongoing
	 */
	@Test()
	public void TestCase_Take_Order_VerifyStatusCode_InvalidFlow() {
		JSONObject inputJSon;
		int id = 0, statusCodeExpected = HttpStatus.SC_UNPROCESSABLE_ENTITY, actualStatus;
		try {
			// Create test data
			inputJSon = CommonUtils.getDefaultJSON(false);

			// Place order
			id = CommonUtils.placeOrder(inputJSon);
			Assert.assertTrue(id != 0, "ID Not Generated ");

			// Cancel order
			CommonUtils.cancelOrder(id);

			CommonUtils.takeOrder(id);
			actualStatus = apiCallHandlerObj.getstatusCode();
			logger.info("InValid Status Code for Incorrect flow of Take Order" + actualStatus);

			Assert.assertEquals(actualStatus, statusCodeExpected, "Status do not match ");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * description : Verifies Error Message for take order functionality when
	 * invalid order Id is provided as input
	 */
	@Test()
	public void TestCase_Take_Order_VerifyErrorMessage_InvalidID() {
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
			CommonUtils.takeOrder((id + 1));

			// Get status code
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
