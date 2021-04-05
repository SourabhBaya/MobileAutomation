package org.oiShoppingList.common.toolbox;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.oiShoppingList.common.misc.EnvPropVariables;
import org.oiShoppingList.common.misc.FailureManagement;
import org.oiShoppingList.common.misc.StaticData;
import org.oiShoppingList.common.util.CLParams;
import org.oiShoppingList.common.util.ElementUtil;
import org.oiShoppingList.common.util.LoggerConfig;
import org.oiShoppingList.common.util.TakeScreenshot;


public class Textbox 
{
	private int nMaximumWaitTime;
	
	public Textbox() {
		nMaximumWaitTime = new CLParams().ReadingIntegerCLParamsPreConfig("Maximum_Wait_Time", new EnvPropVariables().getPreConfigPath());
	}
 	/**
	 * #Function Name: public boolean enterValue(Boolean bWillAssert, String strField, WebElement element, String strValue, String strContOnFail)
	 *
	 * #Description: This method is used to enter value into a field.
	 *
	 * #Input Parameters:
	 * 			@param bWillAssert - pass the boolean value is true.
	 * 			@param strField - Name of the field which is mention in Json file.
	 *			@param element - value of the  element where we have to enter value.
	 * 			@param strValue - value which we need to pass into a field.
	 * 			@param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 * 			@return true - if data entered in field matches with the given value
	 * 					false - if data entered not matched
	 *
	 * #Author: Sourabh Baya
	 */
	public boolean enterValue(Boolean bWillAssert, String strField, WebElement element, String strValue, String strContOnFail)
	{
		boolean bIsValueEntered = false;
		LoggerConfig.log.debug("### Entering value into the Text box element ###");

		try
		{
			if(new EnvPropVariables().getTestScope().equalsIgnoreCase("Mobile")) {
				enterValueForMobile(element,strValue);
			}

		}
		catch(StaleElementReferenceException e1) {
			int nWaitTime = 0;
			int nMaxTime = nMaximumWaitTime;

			while(nWaitTime <= nMaxTime) {
				try {
					element = new ElementUtil().fetchElement(StaticData.strIdentifierType, StaticData.strIdentifiervalue);
					LoggerConfig.log.debug("#### Waiting for stability of the element ####");
					waitingForStabilityOfElement(element);

					element.sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE);

					int nCount1 = 0;
					int nMaxvalue1=nMaximumWaitTime/1000;
					boolean bIsValueEntered1 = false;

					while(nCount1<=nMaxvalue1) {
						bIsValueEntered1 = toVerifyDataInField(element, strValue);

						if(bIsValueEntered1) {
							break;
						}
						else {
							LoggerConfig.log.debug("Field is not ready yet, hence waiting for more stability");

							try {
								TimeUnit.SECONDS.sleep(1);
							}
							catch (InterruptedException e) {
								LoggerConfig.log.debug("Exception occured:"+e.getMessage());
							}
							nCount1++;
						}
						element.sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE);
						element.sendKeys(strValue);
					}
					if(bWillAssert) {
						if(!bIsValueEntered1) {
							String strErrorMsg = "Unable to enter '" + strValue + "' in " + strField + " textbox";
							LoggerConfig.log.error(strErrorMsg);
							new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
						}
					}
					break;
				}
				catch (StaleElementReferenceException e2) {
					element = new ElementUtil().fetchElement(StaticData.strIdentifierType, StaticData.strIdentifiervalue);
					LoggerConfig.log.debug("Stale Element Exception occured:"+e2.getMessage());
				}
				catch (Exception e) {
					LoggerConfig.log.debug("Exception occured:"+e.getMessage());
				}
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				}
				catch (InterruptedException e) {
					LoggerConfig.log.debug("Exception occured:"+e.getMessage());
				}
				nWaitTime = nWaitTime+500;
			}
			LoggerConfig.log.debug("Stale Element Reference Exception occured:"+e1.getMessage());
		}
		catch (WebDriverException e3) {
			try {

				LoggerConfig.log.debug("#### Waiting for stability of the element ####");
				waitingForStabilityOfElement(element);
				element.clear();
				element.sendKeys(strValue);
				if (element.getAttribute("value").equals(strValue)) {
					bIsValueEntered = true;
				}
				else {
					if(bWillAssert) {
						String strErrorMsg = "Unable to enter '" + strValue + "' in " + strField + " textbox"+e3.getMessage();
						LoggerConfig.log.error(strErrorMsg,e3);
						new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
					}
				}
			}
			catch(Exception e4) {
				if(bWillAssert) {
					String strErrorMsg = "Unable to enter " + strValue + " value in " + strField + " textbox. Exception occurred:" + e4.getMessage();
					LoggerConfig.log.error(e4);
					new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
				}
				LoggerConfig.log.debug("Exception occured:"+e4.getMessage());
			}
			LoggerConfig.log.debug("Web driver Exception occured:"+e3.getMessage());
		}
		catch (Exception e) {
			LoggerConfig.log.debug("Exception occured:"+e.getMessage());
		}
		new TakeScreenshot().getScreenshot();
		return bIsValueEntered;
	}

	public void enterValueForMobile(WebElement element, String strValue) {
		element.click();
		LoggerConfig.log.debug("#### Waiting for stability of the element ####");
		waitingForStabilityOfElement(element);
		element.sendKeys(strValue);
	}


	/**
	 * #Function Name: public boolean pressEnterKey(Boolean bWillAssert, String strField, WebElement element, String strContOnFail)
	 * '
	 * #Description: This method is used to do enter.
	 *
	 * #Input Parameters:
	 * 			@param bWillAssert - pass the boolean value is true.
	 * 			@param strField - Name of the field which is mention in Json file.
	 * 			@param element - value of the element which we need to be click.
	 * 			@param strContOnFail -Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 * 			@return true - if Enter Key is pressed
	 * 					false - if Press enter key is not working
	 *
	 * #Author: Sourabh Baya
	 */
	public boolean pressEnterKey(Boolean bWillAssert, String strField, WebElement element, String strContOnFail)
	{
		boolean bIsPressEnterKeyWorked = false;
		LoggerConfig.log.debug("### Pressing Enter Key ###");

		try {
			LoggerConfig.log.debug("#### Waiting for stability of the element ####");
			//waitingForStabilityOfElement(element);
			element.sendKeys(Keys.RETURN);
			bIsPressEnterKeyWorked = true;
		}
		catch(Exception e) {
			if(bWillAssert) {
				String strErrorMsg = "Error encountered while pressing Enter key on field : "+strField +e.getMessage();
				LoggerConfig.log.error(strErrorMsg);
				new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
			}
			LoggerConfig.log.debug("Exception occured:"+e.getMessage());
		}
		return bIsPressEnterKeyWorked;
		
	}


	/**
	 * #Description: This method is used verify data entered in the field matches with the data sent.
	 *
	 * #Function Name: public boolean toVerifyDataInField(WebElement element, String strValue)
	 * '
	 * #Input Parameters:
	 * 			@param element - value of the element which we need to be click.
	 * 			@param strValue - Value which is sent to the field
	 * 			@return true - if data entered is matched with the data present in field
	 * 					false - if data doesnt match
	 *
	 * #Author: Sourabh Baya
	 */


	public boolean toVerifyDataInField(WebElement element, String strValue) {
		boolean bIsDataEntered = false;
		try {
			if (element.getAttribute("value").equals(strValue)) {
				bIsDataEntered = true;
			}
			else {
				LoggerConfig.log.debug("Data entered is not matching to what is reflected.");
				bIsDataEntered = false;
			}
		}
		catch(NullPointerException e) {
			try {
				if (!element.getAttribute("textContent").equals("")) {
					bIsDataEntered = true;
					//StaticData.currentRunningScenario.write("Entered '" + strValue + "' in " + strLogicalName + " textbox");
				} else {
					LoggerConfig.log.debug("Data entered is not matching to what is reflected.");
					bIsDataEntered = false;
				}
			} catch (NullPointerException ex) {
				try {
					LoggerConfig.log.debug("Could not find the attributes value and textContent for the element and hence considering that the value has been entered already");
					bIsDataEntered = true;
				} catch (Exception exe){
					LoggerConfig.log.debug(exe.getMessage());
				}
			}
		}
		return bIsDataEntered;
	}


	/**
	 * #Description: This method is used check the stability of the webelement
	 *
	 * #Function Name: public void waitingForStabilityOfElement(WebElement element)
	 * '
	 * #Input Parameters:
	 * 			@param element - value of the element which we need to be click.
	 * 			@return true - if element is enabled and clickable
	 * 					false - if element is disabled
	 *
	 * #Author: Sourabh Baya
	 */
	
	public boolean waitingForStabilityOfElement(WebElement element) {
		Boolean bToProceed = false;

		try {
			int nCount = 0;
			int nMaxvalue=nMaximumWaitTime/1000;
			
			while(nCount<=nMaxvalue) {
				try {
					if (element.isEnabled()) {
						LoggerConfig.log.debug("Textbox field is enabled");
						element.click();
						LoggerConfig.log.debug("Textbox field is clickable");
						bToProceed = true;
						break;
					}
				}
				catch(Exception e) {
					LoggerConfig.log.debug("Exception encountered, hence trying again...." + e.getMessage());
				}
				LoggerConfig.log.debug("Field is not enabled yet, hence waiting");
				TimeUnit.SECONDS.sleep(1);
				nCount++;
			 }
		}
		catch(Exception e) {
			LoggerConfig.log.debug(e.getMessage());
		}
		return  bToProceed;
	}
}
