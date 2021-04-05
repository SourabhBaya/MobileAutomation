package org.oiShoppingList.common.toolbox;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.oiShoppingList.common.misc.EnvPropVariables;
import org.oiShoppingList.common.misc.FailureManagement;
import org.oiShoppingList.common.misc.StaticData;
import org.oiShoppingList.common.misc.Sync;
import org.oiShoppingList.common.util.CLParams;
import org.oiShoppingList.common.util.ElementUtil;
import org.oiShoppingList.common.util.LoggerConfig;
import org.oiShoppingList.common.util.TakeScreenshot;


public class Button {
	
	private int nMaximumWaitTime;
	
	public Button() {
		nMaximumWaitTime = new CLParams().ReadingIntegerCLParamsPreConfig("Maximum_Wait_Time", new EnvPropVariables().getPreConfigPath());
	}

	/**
	 * #Function Name: public boolean click(Boolean bWillAssert, String strField, WebElement element, String strContOnFail)
	 *
	 * #Description: This method is used to click on the particular element.
	 *
	 * #Input Parameters:
	 * 			@param bWillAssert - pass the boolean value is true.
	 * 			@param strField - Name of the field which is mention in Json file.
	 * 			@param element - value of the  element which we have to click.
	 * 			@param element - value of the  element which we have to click.
	 * 			@param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 * 			@return return the boolean value of element is clicked or not
	 *
	 * #Author: Sourabh Baya
	 */

	public boolean click(Boolean bWillAssert, String strField, WebElement element, String strContOnFail) {
		boolean bIsButtonClicked = false;

		try {
			LoggerConfig.log.debug("#### Clicking the element ####");
			element.click();
			bIsButtonClicked = true;
			new TakeScreenshot().getScreenshot();
		}
		catch (StaleElementReferenceException e1) {
			int nWaitTime = 0;
			int nMaxTime = nMaximumWaitTime;
            LoggerConfig.log.error("Error Encountered since element is not attached to the DOM "+e1.getMessage());

			while (nWaitTime <= nMaxTime) {
				try {
					element.click();
					bIsButtonClicked = true;
					new TakeScreenshot().getScreenshot();
					break;
				} 
				catch (StaleElementReferenceException e2) {
					element =new ElementUtil().fetchElement(StaticData.strIdentifierType, StaticData.strIdentifiervalue);
					LoggerConfig.log.debug("Stale Element Exception occured:"+e2.getMessage());
				}
				catch (Exception e) {
					LoggerConfig.log.debug("Exception occured:"+e.getMessage());
				}
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} 
				catch (InterruptedException e) {
					LoggerConfig.log.debug("Interrupted Exception occured:"+e.getMessage());
				}
				nWaitTime = nWaitTime + 500;
			}
			LoggerConfig.log.debug("Exception occured:"+e1.getMessage());
		}
		catch (UnhandledAlertException e5) {
			LoggerConfig.log.debug("Reached Unhandled Alert Exception.."+e5.getMessage());
		}
		catch (WebDriverException e2) {
			LoggerConfig.log.debug("Web Driver Exception occured:"+e2.getMessage());
			try {

				new Sync().waitForMilliSeconds(500);
				element.click();
				bIsButtonClicked = true;
				new TakeScreenshot().getScreenshot();
			}
			catch (WebDriverException e3) {
				try {
					JavascriptExecutor executor = (JavascriptExecutor) StaticData.driver;
					executor.executeScript("arguments[0].click();", element);
					LoggerConfig.log.debug("Exception occured:"+e3.getMessage());
				}
				catch (Exception e4) {
					if (bWillAssert) {
						LoggerConfig.log.debug("Exception occured:"+e4.getMessage());
						String strErrorMsg = "Unable to click " + strField + " link. Exception occurred:"
								+ e4.getMessage();
						new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
					}
					LoggerConfig.log.debug("Exception occured:"+e4.getMessage());
				}
				LoggerConfig.log.debug("Exception occured:"+e3.getMessage());
			}
			catch (Exception e6) {
				if (bWillAssert) {
					String strErrorMsg = "Unable to click " + strField + " link. Exception occurred:" + e6.getMessage();
					LoggerConfig.log.error( "Unable to click " + strField + " link. Exception occurred:" +e6.getMessage());
					new	FailureManagement().manageFailure(strErrorMsg, strContOnFail);
				}
				LoggerConfig.log.debug("Exception occured:"+e6.getMessage());
			}
		} catch (Exception e) {
			LoggerConfig.log.debug("Exception occured:"+e.getMessage());
		}
		return bIsButtonClicked;
	}
}