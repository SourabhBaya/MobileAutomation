package org.oiShoppingList.common.misc;

import org.junit.Assert;
import org.oiShoppingList.common.util.LoggerConfig;
import org.oiShoppingList.common.util.TakeScreenshot;

public class FailureManagement 
{
	/**
	 * #Function Name: public void manageFailure(String strErrorMessage, String strContOnFail)
	 *
	 * #Description:This method is used to take screenshot of fail steps and provide the error with red color.
	 *
	 * #Input Parameters:
	 * 			@param strErrorMessage -  its an error message once the method will get failed.
	 *			 @param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 * 
	 * #Author: Sourabh Baya
	 */
	public void manageFailure(String strErrorMessage, String strContOnFail) {
		String strCaptureScreenshotDefaultValue = StaticData.strCaptureScreenshotOnFailureOnly;
		StaticData.strCaptureScreenshotOnFailureOnly = "No";
		try {
			new TakeScreenshot().getScreenshot();
			StaticData.strCaptureScreenshotOnFailureOnly = strCaptureScreenshotDefaultValue;

			if ((strContOnFail != null) && (strContOnFail.toLowerCase().contains("continue on failure"))) {
				StaticData.scenario.write("<h4 style=\"background-color: red;\"><b>Assertion Error</b></h4>");
				StaticData.scenario.write("<b>" + strErrorMessage + "</b>");
				StaticData.bIsFailed = true;
			}
			else {
				Assert.fail(strErrorMessage);
			}
		}
		catch (Exception e) {
			String strErrorMsg = "Exception encountered in manageFailure method : "+e.getMessage();
			LoggerConfig.log.error(strErrorMsg);
		}
	}
}