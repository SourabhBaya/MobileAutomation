package org.amazon.common.scripts;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebElement;
import org.amazon.common.misc.EnvPropVariables;
import org.amazon.common.misc.FailureManagement;
import org.amazon.common.misc.Sync;
import org.amazon.common.toolbox.*;
import org.amazon.common.util.*;


public class GeneralScript {
	
	private int nMaxWaitTime = 0;
	private String strTestDataSeparator = "";
	
	
	public GeneralScript() {

		nMaxWaitTime =  new CLParams().ReadingIntegerCLParamsPreConfig("Maximum_Wait_Time", new EnvPropVariables().getPreConfigPath());
		strTestDataSeparator = new CLParams().ReadingStringCLParams("TestData_Separator",new EnvPropVariables().getPreConfigPath(),"PreConfig");

	}

	/**
	 * #Function Name:public void enterData(String strField, String strPage,String strContOnFail)
	 *
	 * #Description:This method is used for enter a value into a field.
	 *
	 * #Input Parameters:
	 * 			@param strField - Name of the field which is mention in Json file.
	 * 			@param strPage - WorkJson File Name
	 *          @param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 *
	 * #Author: Sourabh Baya
	 */
	public void enterData(String strField, String strPage, String strContOnFail) {
		try {
			WebElement element ;
			// String pagename = page+" Page";
			String strField_Value ;
				element = new ElementUtil().getElement(true, strPage, strField,
						strContOnFail);

			strField_Value = new TestDataUtil().getfieldValues(strField);
			if (element != null) {
				new Textbox().enterValue(true, strField, element, strField_Value, strContOnFail);
			}

		} catch (Exception e) {

			String strErrorMsg = "Error encountered while trying to enter data in " + strField + " in " + strPage
					+ " page." + e.getMessage();

			LoggerConfig.log.debug(e);

			new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
		}

	}

	/**
	 * #Function Name:public void searchData(String strField, String strPage,String strContOnFail)
	 *
	 * #Description:This method is used to find an element
	 *
	 * #Input Parameters:
	 * 			@param strField - Name of the field which is mention in Json file.
	 * 			@param strPage - Json File Name
	 *          @param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 *
	 * #Author: Sourabh Baya
	 */
	public void searchData(String strField, String strPage, String strContOnFail) {
		try {
			WebElement element;


				element = new ElementUtil().getElement(true,strPage, strField, strContOnFail);

			String strField_Value = new TestDataUtil().getfieldValues(strField);

			if (strField_Value.contains("<") && strField_Value.contains(">")) {
				int nStartindex = strField_Value.indexOf('<');
				int nEndindex = strField_Value.indexOf('>');
				String strGetStringtoReplace = strField_Value.substring(nStartindex, nEndindex + 1);
				String strTestDataField = strGetStringtoReplace.substring(1, strGetStringtoReplace.length() - 1);

				String strTestDataValue = new TestDataUtil().getfieldValues(strTestDataField);

				strField_Value = new ElementUtil().replaceTestData(strField_Value, strGetStringtoReplace, strTestDataValue);

			}

			if (element != null) {
				new Textbox().enterValue(true, strField, element, strField_Value, strContOnFail);
				new Textbox().pressEnterKey(true, strField, element, strContOnFail);
			}

		} catch (Exception e) {
			String strErrorMsg = "Error encountered while trying to enter and search data on " + strField + " field in "
					+ strPage + " page." + e.getMessage();

			LoggerConfig.log.debug(e);

			new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
		
		}

	}

	/**
	 * #Function Name:public void ButtonClick(String strField, String strPage,String strContOnFail)
	 *
	 * #Description:This method is used to click on a button or an element.
	 *
	 * #Input Parameters:
	 * 			@param strField - Name of the field which is mention in Json file.
	 * 			@param strPage - Json File Name
	 *          @param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 *
	 * #Author:Sourabh Baya
	 */
	public void ButtonClick(String strField, String strPage, String strContOnFail) {
		try {
			WebElement element ;

				element = new ElementUtil().getElement(true, strPage, strField,strContOnFail);

			if (element != null) {
				new Button().click(true, strField, element, strContOnFail);
			}
		} catch (Exception e) {
			String strErrorMsg = "Error encountered while trying to click on " + strField + " button in " + strPage
					+ " page." + e.getMessage();

			LoggerConfig.log.debug(e);

			new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
			
		}

	}

	/**
	 * #Function Name:public void LinkClick_ifPresent(String strField, String strPage,String strContOnFail)
	 *
	 * #Description:This method is used to find a link if its present then click.
	 *
	 * #Input Parameters:
	 * 			@param strField - Name of the field which is mention in Json file.
	 * 			@param strPage - Json File Name
	 *          @param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 *
	 * #Author:Sourabh Baya
	 */
	public void LinkClick_ifPresent(String strField, String strPage, String strContOnFail) {
		try {
			WebElement element;

				element = new ElementUtil().getElement(false,strPage, strField, strContOnFail);

			if (element != null) {
				new Button().click(true, strField, element, strContOnFail);
			}

		} catch (Exception e) {
			String strErrorMsg = "Error encountered while trying to click " + strField + " link if present in "
					+ strPage + " page." + e.getMessage();

			LoggerConfig.log.debug(e);
			
			new	FailureManagement().manageFailure(strErrorMsg, strContOnFail);
			
		}

	}

	/**
	 * #Function Name:public void LinkClick(String strField, String strPage,String strContOnFail)
	 *
	 * #Description:This method is used to click on a link when only single link is present.
	 *
	 * #Input Parameters:
	 * 			@param strField - Name of the field which is mention in Json file.
	 * 			@param strPage - Json File Name
	 *          @param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 *
	 * #Author: Sourabh Baya
	 */
	public void LinkClick(String strField, String strPage, String strContOnFail) {
		try {
			WebElement element;


				element = new ElementUtil().getElement(true,strPage, strField,
						strContOnFail);

			if (element != null) {
				new Button().click(true, strField, element, strContOnFail);
			}

		} catch (Exception e) {
			String strErrorMsg = "Error encountered while trying to click " + strField + " link in " + strPage
					+ " page." + e.getMessage();

			LoggerConfig.log.debug(e);

			new	FailureManagement().manageFailure(strErrorMsg, strContOnFail);
		}

	}


	/**
	 * #Function Name:public void LinkClick(String strField1, String strField2,
	 * 	                  String strPage, String strContOnFail)
	 * #Description:This method is used to click on a link when 2 links are present.
	 *
	 * #Input Parameters:
	 * 			@param strField1,strField2 - Name of the field which is mention in Json file.
	 * 			@param strPage - Json File Name
	 *          @param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 *
	 * #Author: Sourabh Baya
	 */
	public void LinkClick(String strField1, String strField2, String strPage, String strContOnFail) {
		try {
			WebElement element1;
			WebElement element2;

				element1 = new ElementUtil().getElement(true,strPage, strField1, strContOnFail);

			if (element1 != null) {
				new Button().click(true, strField1, element1, strContOnFail);
			}

			new Sync().waitForMilliSeconds(500);

				String strIdentifierType2 = new POMUtil().read_pom(strPage, strField2, 1);
				String strIdentifierValue2 = new POMUtil().read_pom(strPage, strField2, 2);

				String strNew_IdentifierValue = new ElementUtil().getUpdatedIdentifierValue(strIdentifierValue2, strPage,
						strField2);
				element2 = new Sync().waitforElement(true, strField2, strIdentifierType2, strNew_IdentifierValue,
						strContOnFail);


			if (element2 != null) {
				new Button().click(true, strField2, element2, strContOnFail);
			}

		} catch (Exception e) {
			String strErrorMsg = "Error encountered while trying to click " + strField1 + "," + strField2 + " link in "
					+ strPage + " page." + e.getMessage();

			LoggerConfig.log.debug(e);

			new	FailureManagement().manageFailure(strErrorMsg, strContOnFail);

		}

	}

	/**
	 * #Function Name:public void verifyElementPresenceAndValidate(String strField, String strValidateCondition,
	 * 			String strValidateValue, String strPage, String strContOnFail)
	 *
	 * #Description:This method is used to verify the element is present or not and then validate.
	 *
	 * #Input Parameters:
	 * 			@param strValidateCondition -condition of the validation. (present or not present)
	 * 			@param strValidateValue -value of the element which we have to validate.
	 * 	 		@param strField -Name of the field which is mention in Json file.
	 * 	 	    @param strPage - Value of the workbook
	 * 	  		@param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 * #Author: Sourabh Baya
	 */
	public void verifyElementPresenceAndValidate(String strField, String strValidateCondition,
			String strValidateValue, String strPage, String strContOnFail) {
		String strField_Value = "";

		try {
			WebElement element ;


				new Display().isElementDisplayed(true, strField,strPage,	strContOnFail);

				element = new ElementUtil().getElement(true,strPage, strField, strContOnFail);


			strField_Value = new TestDataUtil().getfieldValues(strValidateValue);

			if (strField_Value.equals("")) {
				strField_Value = strValidateValue;
			}

			if (element != null) {
				String strElementValue = new Display().getData(strField, element);

				switch (strValidateCondition) {
				case "starts with":
					if (strElementValue.startsWith(strField_Value)) {
						new TakeScreenshot().getScreenshot();
					} else {
						String strErrorMsg = strField + " is present but does not " + strValidateCondition + " value "
								+ strField_Value + " in " + strPage + " .";

				new FailureManagement().manageFailure(strErrorMsg, strContOnFail);

					}
					break;

				case "contains":
					if (strElementValue.contains(strField_Value)) {
						new TakeScreenshot().getScreenshot();
					} else {
						String strErrorMsg = strField + " is present but does not " + strValidateCondition + " value "
								+ strField_Value + " in " + strPage + " .";

				new FailureManagement().manageFailure(strErrorMsg, strContOnFail);

					}
					break;
				}

			}

		} catch (Exception e) {
			String strErrorMsg = "Error encountered while trying to verify whether " + strField + " "
					+ strValidateCondition + " " + strField_Value + " in " + strPage + " page." + e.getMessage();

			LoggerConfig.log.debug(e);

			new	FailureManagement().manageFailure(strErrorMsg, strContOnFail);

		}

	}

	/**
	 * #Function Name:public void readTestDataFrom(String strJsonFileName, String strTestCaseName)
	 *
	 * #Description:This method is used to read test data from Json file.
	 *
	 * #Input Parameters:
	 *			@param strJsonFileName -  json file name
	 * 	 	    @param strTestCaseName - test case name
	 *
	 * #Author: Sourabh Baya
	 */
	public void readTestDataFrom(String strJsonFileName, String strTestCaseName) {

			new TestDataUtil().loadTestDataInMap(strJsonFileName, strTestCaseName);
	}




	/**
	 * #Function Name:public void clickAndVerify(String strButton, String strField, String strPresence, String strPage, String strContOnFail)
	 *
	 * #Description:  This method is used to verify if the button's stability
	 *
	 * #Input Parameters:
	 *  		@param strField - Name of the field which is mention in Json file.
	 * 			@param strPage - Name of the workbook
	 * 			@param strButton - Button field to be clicked
	 *			@param strPresence - Presence of element
	 * 	  		@param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 *
	 * #Author: Sourabh Baya
	 */
	public void clickAndVerify(String strButton, String strField, String strPresence, String strPage,
			String strContOnFail) {
		try {
			WebElement buttonElement ;
			WebElement fieldElement ;

			int nCount = 0;
			int nMaxvalue = nMaxWaitTime/1000;

			while (nCount < nMaxvalue) {
				buttonElement = new ElementUtil().getElement(true, strPage,strButton, strContOnFail);

				if (buttonElement != null) {
					new Button().click(true, strField, buttonElement, strContOnFail);
				}

				if (buttonElement == null && nCount == 0) {
					String strErrorMsg = "Error encountered while trying to click on " + strButton
							+ " button and wait for " + strField + " field in " + strPage + " page.";
				new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
				}

				if (buttonElement == null && nCount > 1) {
					String strErrorMsg = "Error encountered while trying to click on " + strButton
							+ " button and wait for " + strField + " field in " + strPage + " page.";
				new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
				}

				fieldElement = new ElementUtil().getElement(false, strPage,strField, strContOnFail);

				if (fieldElement != null) {
					new TakeScreenshot().getScreenshot();
					LoggerConfig.log.debug(
							"Button " + strButton + " clicked successfully as " + strField + " field is appearing.");
					break;
				} else {
					LoggerConfig.log.debug("Button is not ready yet, hence waiting for more stability");

					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (Exception e) {
						LoggerConfig.log.debug(e);
					}

					nCount = nCount + 3;
				}
			}

		} catch (Exception e) {
			String strErrorMsg = "Error encountered while trying to click on " + strButton + " button and wait for "
					+ strField + " field in " + strPage + " page." + e.getMessage();

			LoggerConfig.log.debug(e);

			new FailureManagement().manageFailure(strErrorMsg, strContOnFail);

		}
	}

	public void verifyElementPresence(String strField, String strExistence, String strPage, String strContOnFail) {
		String field;
		try {
			String strTestData = (new TestDataUtil()).getfieldValues(strField);
			if (!strTestData.contains(",")) {
				if (strExistence.equals("present")) {
					(new Display()).isElementDisplayed(true, strField, strPage, strContOnFail);
				}

				if (strExistence.equals("not present")) {
					(new Display()).isElementNotDisplayed(true, strField, strPage, strContOnFail);
				}
			} else {
				List<String> fieldList = Arrays.asList(strTestData.split(","));
				Iterator var8 = fieldList.iterator();

				while(var8.hasNext()) {
					field = (String)var8.next();
					if (strExistence.equals("present")) {
						(new Display()).isElementDisplayed(true, field, strPage, strContOnFail);
					}

					if (strExistence.equals("not present")) {
						(new Display()).isElementNotDisplayed(true, field, strPage, strContOnFail);
					}
				}
			}
		} catch (Exception var10) {
			field = "Error encountered while trying to verify whether " + strField + " field is " + strExistence + " in " + strPage + " page." + var10.getMessage();
			LoggerConfig.log.debug(var10);
			(new FailureManagement()).manageFailure(field, strContOnFail);
		}

	}

}
