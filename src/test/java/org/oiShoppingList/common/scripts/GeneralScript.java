package org.oiShoppingList.common.scripts;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebElement;
import org.oiShoppingList.common.misc.EnvPropVariables;
import org.oiShoppingList.common.misc.FailureManagement;
import org.oiShoppingList.common.misc.Sync;
import org.oiShoppingList.common.toolbox.*;
import org.oiShoppingList.common.util.*;


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
