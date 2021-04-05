package org.oiShoppingList.common.toolbox;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.oiShoppingList.common.misc.EnvPropVariables;
import org.oiShoppingList.common.misc.FailureManagement;
import org.oiShoppingList.common.misc.StaticData;
import org.oiShoppingList.common.util.CLParams;
import org.oiShoppingList.common.util.ElementUtil;
import org.oiShoppingList.common.util.LoggerConfig;
import org.oiShoppingList.common.util.TakeScreenshot;


public class Display {

    private int nMaxTime;
    private String strElementData = "";
	
	public Display() {
		nMaxTime =  new CLParams().ReadingIntegerCLParamsPreConfig("Maximum_Wait_Time", new EnvPropVariables().getPreConfigPath());
	}

	/**
	 * #Function Name: public String getData(String strField, WebElement element)
	 *
	 * #Description: This method is used to get the text data of an element.
	 *
	 * #Input Parameters:
	 * 			@param strField - Name of the field which is mentioned in Json file.
	 * 			@param element -  web element
	 * 			@return this method returns the text value of an element.
	 * 
	 * #Author: Sourabh Baya
	 */
	public String getData(String strField, WebElement element) {

		int nWaitTime = 0;
		try {
			LoggerConfig.log.debug("### Getting text value of the element ###");
			strElementData = element.getText().trim();
			new TakeScreenshot().getScreenshot();
			return strElementData;
		}
		catch (StaleElementReferenceException e1) {
            LoggerConfig.log.debug("Error occured since element is not present in the DOM "+e1.getMessage());
			while (nWaitTime < nMaxTime) {
				try {
					strElementData = element.getText().trim();
					new TakeScreenshot().getScreenshot();
					return strElementData;
				}
				catch (StaleElementReferenceException e2) {
					element =new ElementUtil().fetchElement(StaticData.strIdentifierType, StaticData.strIdentifiervalue);
					LoggerConfig.log.debug("Stale Element Exception occured:"+e2.getMessage());
				}
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				}
				catch (InterruptedException e) {
					LoggerConfig.log.debug("Interrupted Exception occured:"+e.getMessage());
				}
				nWaitTime = nWaitTime + 500;
			}
			LoggerConfig.log.debug("Stale element Exception occured:"+e1.getMessage());
		}
		LoggerConfig.log.debug("Element Text is "+strElementData);
		new TakeScreenshot().getScreenshot();
		return strElementData;
	}

	/**
	 * #Function Name: public boolean isElementDisplayed(Boolean bWillAssert,
	 * 	                  String strField, String strIdentifierType, String
	 * 	                  strIdentifierValue, String strPage, String strContOnFail)
	 *
	 * #Description: This method is used to check the particular element is displayed or not.
	 *
	 * #Input Parameters:
	 *			 @param bWillAssert - pass the boolean value is true.
	 * 			 @param strField - Name of the field which is mention in Json file.
	 *   		 @param strPage - Name of the workbook file.
	 * 			 @param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 * 			 @return return the boolean value of element is display or not
	 *
	 * #Author: Sourabh Baya
	 */
	public boolean isElementDisplayed(Boolean bWillAssert, String strField, String strPage, String strContOnFail) throws IOException {
		boolean bIsElementDisplayed = false;
		int nWaitTime = 0;
		WebElement element = null;
        try {
			element =new ElementUtil().getElement(true,strPage, strField, strContOnFail);
			LoggerConfig.log.debug("### Checking if the element is being displayed ###");

			if (element != null) {
				LoggerConfig.log.debug(strField + " element is not null....");
				while (nWaitTime <= nMaxTime) {
					try {
						if (element.isDisplayed()) {
							bIsElementDisplayed = true;
							new TakeScreenshot().getScreenshot();
							LoggerConfig.log.debug(strField + " is displayed....");
							break;
						}
					}
					catch (StaleElementReferenceException e) {

						LoggerConfig.log.debug("Stale Element Exception occured:"+e.getMessage());
					}
					catch (Exception e1) {
						new TakeScreenshot().getScreenshot();
						LoggerConfig.log.debug("Exception occured:"+e1.getMessage());
					}
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (InterruptedException e2) {
						LoggerConfig.log.debug("Interrupted Exception occured:"+e2.getMessage());
					}
					nWaitTime = nWaitTime + 500;
				}
				if (!element.isDisplayed() && !bIsElementDisplayed) {
					if (bWillAssert) {
						String strErrorMsg = strField
								+ " element is not displayed, though it was expected to be displayed.";
						LoggerConfig.log.error(strErrorMsg);
						new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
					}
				}
			}
		}
        catch (Exception e){
			LoggerConfig.log.debug("Exception occured:"+e.getMessage());
		}
        LoggerConfig.log.debug("Is element Displayed "+bIsElementDisplayed);
		return bIsElementDisplayed;
	}

	public boolean isElementNotDisplayed(Boolean bWillAssert, String strField, String strPage, String strContOnFail) throws IOException {
		boolean bIsElementNotDisplayed = false;
		LoggerConfig.log.debug("#### Checking if the element is not getting displayed ####");
		WebElement element = null;

		try {
			element = (new ElementUtil()).getElement(false, strPage, strField, strContOnFail);
			if (element != null && element.isDisplayed()) {
				if (bWillAssert) {
					String strErrorMsg = strField + " element is displayed, though it was expected not to be displayed.";
					LoggerConfig.log.debug(strErrorMsg);
					(new FailureManagement()).manageFailure(strErrorMsg, strContOnFail);
				}
			} else {
				bIsElementNotDisplayed = true;
				(new TakeScreenshot()).getScreenshot();
				LoggerConfig.log.debug(strField + " element is not present in DOM");
			}
		} catch (Exception var8) {
			LoggerConfig.log.debug("Exception occured:" + var8.getMessage());
		}

		LoggerConfig.log.debug("Is element Not displayed " + bIsElementNotDisplayed);
		return bIsElementNotDisplayed;
	}
}
