package org.oiShoppingList.common.misc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.oiShoppingList.common.util.CLParams;
import org.oiShoppingList.common.util.ElementUtil;
import org.oiShoppingList.common.util.LoggerConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Sync {

	private int nMaxWaitTime;
	private int nShortMaxWaitTime;
	private int nPageLoadWaitTime;
	
	public Sync() {
		nMaxWaitTime = new CLParams().ReadingIntegerCLParamsPreConfig("Maximum_Wait_Time", new EnvPropVariables().getPreConfigPath());
		nShortMaxWaitTime = new CLParams().ReadingIntegerCLParamsPreConfig("Short_Maximum_Wait_Time", new EnvPropVariables().getPreConfigPath());
		nPageLoadWaitTime = new CLParams().ReadingIntegerCLParamsPreConfig("Page_Load_Wait_Time", new EnvPropVariables().getPreConfigPath());
	}

	/**
	 * #Description: This method is used for sleep.
	 * 
	 * #Function Name: public void sleep(Integer nSeconds)
	 *
	 * #Input Parameters:
	 * 			@param nSeconds - How much time the method will wait.
	 * 
	 * #Author: Sourabh Baya
	 */
	public void sleep(Integer nSeconds) {
		long lngSecondsLong = (long) nSeconds;
		try {
			LoggerConfig.log.debug("Thread Sleep");
			Thread.sleep(lngSecondsLong);
		} catch (Exception e) {
			LoggerConfig.log.error(e);
			e.printStackTrace();
		}
	}

	/**
	 * #Function Name: public void waitForMilliSeconds(int nStrWaitName)
	 *
	 * #Description: This method is used to wait for milli seconds
	 *
	 * #Input Parameters:
	 * 			@param nStrWaitName - value of wait time
	 * 
	 * #Author: Sourabh Baya
	 */
	public void waitForMilliSeconds(int nStrWaitName) {
		try {
			LoggerConfig.log.debug("Waiting for Milli Seconds");
			TimeUnit.MILLISECONDS.sleep(nStrWaitName);
		} catch (Exception e) {
			LoggerConfig.log.debug(e);
		}
	}


	/**
	 * #Function Name: public WebElement waitforElement(Boolean bWillAssert, String
	 *                  strField, String strIdentifierType, String strIdentifiervalue, String
	 *                 strContOnFail)
	 *
	 * #Description: This method is used to wait for an element.
	 *
	 * #Input Parameters
	 * 			@param bWillAssert -  pass the boolean value is true.
	 * 			@param strField - Name of the field which is mention in Json file.
	 * 			@param strIdentifierType - Type of the locators.
	 * 			@param strIdentifiervalue - Value of the locators.
	 * 			@param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 * 			@return return the element
	 * 
	 * #Author:Sourabh Baya
	 */
	public WebElement waitforElement(Boolean bWillAssert, String strField, String strIdentifierType,
			String strIdentifiervalue, String strContOnFail) {
		int nMaxTime;
		int nWaitTime = 0;
		LoggerConfig.log.debug("#### Explicitly waiting for element to be found ####");
		// WebDriver driver = DriverManager.getDriver();

		if (bWillAssert) {
			nMaxTime = nMaxWaitTime;
		} else {
			nMaxTime = nShortMaxWaitTime;
		}

		WebElement objElement = null;
		By locator = new ElementUtil().getByLocator(strIdentifierType,strIdentifiervalue);
		while (nWaitTime <= nMaxTime) {
			try {

				objElement = StaticData.driver.findElement(locator);

				if (objElement != null) {

					TimeUnit.MILLISECONDS.sleep(500);

					return objElement;
				} else {
					TimeUnit.MILLISECONDS.sleep(500);
					nWaitTime = nWaitTime + 500;
				}
			} catch (Exception e) {
				try {
					LoggerConfig.log.debug("Waiting for a defined time and increasing the time if element not found");
					TimeUnit.MILLISECONDS.sleep(500);
					nWaitTime = nWaitTime + 500;
				} catch (Exception e1) {
					LoggerConfig.log.debug("Exception occured while waiting for a defined time and increasing the time if element not found"+e1.getMessage());
					nWaitTime = nWaitTime + 500;
				}
				LoggerConfig.log.debug("Exception occured:"+e.getMessage());
			}
		}

		// Just waiting for element stability
		try {
			LoggerConfig.log.debug("Waiting for a defined time for element stability");
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (Exception e) {
			LoggerConfig.log.debug("Exception occured while waiting for a defined time "+e.getMessage());
			e.printStackTrace();
		}

		if ((objElement == null) && (bWillAssert)) {

			String strErrorMsg = "";


				strErrorMsg = "The field : " + strField + " with " + strIdentifierType + " : " + strIdentifiervalue
						+ " is not present";

				LoggerConfig.log.error(strErrorMsg);


            try {
				new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
			}
            catch (Exception e){
				LoggerConfig.log.debug("Exception occurred:"+e.getMessage());
			}

		}
		return objElement;
	}

	/**
	 * #Function Name: public List<WebElement> waitforElements(Boolean bWillAssert,
	 *                  String strField, String strIdentifierType, String strIdentifiervalue,
	 *                  String strContOnFail)
	 *
	 * #Description: This method is used to wait for list of an element.
	 *
	 * #Input Parameters:
	 * 			@param bWillAssert -  pass the boolean value is true.
	 * 			@param strField - Name of the field which is mention in Json file.
	 * 			@param strIdentifierType - Type of the locators.
	 * 			@param strIdentifiervalue - Value of the locators.
	 * 			@param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 * 			@return return list of an element
	 * 
	 * #Author: Sourabh Baya
	 */
	public List<WebElement> waitforElements(Boolean bWillAssert, String strField, String strIdentifierType,
			String strIdentifiervalue, String strContOnFail) {
		int nMaxTime;
		int nWaitTime = 0;

        LoggerConfig.log.debug("#### Explicitly waiting for Elements to be found ####");

		if (bWillAssert) {
			nMaxTime = nMaxWaitTime;
		} else {
			nMaxTime = nShortMaxWaitTime;
		}

		List<WebElement> objElementList = null;
		By locator = new ElementUtil().getByLocator(strIdentifierType,strIdentifiervalue);
		while (nWaitTime <= nMaxTime) {
			try {
				objElementList = StaticData.driver.findElements(locator);
				if (objElementList != null) {
					TimeUnit.MILLISECONDS.sleep(500);
					return objElementList;
				}
				else {
					TimeUnit.MILLISECONDS.sleep(500);
					nWaitTime = nWaitTime + 500;
				}
			}
			catch (Exception e) {
				try {
					LoggerConfig.log.debug("Waiting for a defined time and increasing the time if element not found");
					TimeUnit.MILLISECONDS.sleep(500);
					nWaitTime = nWaitTime + 500;
				} catch (Exception e1) {
					LoggerConfig.log.debug("Exception occured while waiting for a defined time and increasing the time if element not found"+e1);
					nWaitTime = nWaitTime + 500;
				}
				LoggerConfig.log.debug("Exception occured:"+e.getMessage());
			}
		}

		try {
			LoggerConfig.log.debug("Waiting for a defined time for element stability");
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (Exception e) {
			LoggerConfig.log.error("Exception occured while waiting for a defined time "+e.getMessage());
		}

		if ((objElementList == null) && (bWillAssert)) {
			String strErrorMsg = "The field : " + strField + " with " + strIdentifierType + " : " + strIdentifiervalue
					+ " is not present in " + StaticData.driver.getTitle() + " page.";

			LoggerConfig.log.error("Error Encountered as The field : " + strField + " with " + strIdentifierType + " : " + strIdentifiervalue
					+ " is not present in " + StaticData.driver.getTitle() + " page.");

			try {
				new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
			} catch (Exception e){
				LoggerConfig.log.debug("Exception occured:"+e.getMessage());
			}
		}
		return objElementList;
	}
}