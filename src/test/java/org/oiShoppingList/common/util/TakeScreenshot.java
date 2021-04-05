package org.oiShoppingList.common.util;


import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.oiShoppingList.common.misc.StaticData;


public class TakeScreenshot 
{
	/**
	 * #Function Name: public void getScreenshot()
	 *
	 * #Description: This method is used to take the screenshot of each steps.
	 *
	 * #Input Parameters:
	 *
	 * #Author: Sourabh Baya
	 */
	public void getScreenshot()
	{
		if(StaticData.strCaptureScreenshotOnFailureOnly.equalsIgnoreCase("No"))
		{

			LoggerConfig.log.debug("Taking screen shot for each steps");
			
			TakesScreenshot scrShot =((TakesScreenshot)StaticData.driver);
	        byte[] byScreenshot = scrShot.getScreenshotAs(OutputType.BYTES);

	        LoggerConfig.log.debug("Embed screenshot to the report");
	        StaticData.scenario.embed(byScreenshot, "image/png");

		}     
        
	}
}
