package org.oiShoppingList.common.util;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.oiShoppingList.common.misc.EnvPropVariables;
import org.oiShoppingList.common.misc.StaticData;
import org.oiShoppingList.common.misc.Sync;



public class ElementUtil {

	/**
	 * #Function Name: public WebElement fetchElement(String strIdentifierType, String strIdentifiervalue)
	 *
	 * #Description: This method is used to fetch an element using there locators.
	 *
	 * #Input Parameters:
	 *			@param strIdentifierType - Type of the locators.
	 * 	        @param strIdentifierValue - Value of the locators.
	 * 	        @return return the webelement
	 *
	 * #Author: Sourabh Baya
	 */

	public WebElement fetchElement(String strIdentifierType, String strIdentifierValue) {
		WebElement objElement = null;

		try {
			LoggerConfig.log.debug("### Fetching the element with respect to locator ###");

			By locator = getByLocator(strIdentifierType,strIdentifierValue);

			objElement = StaticData.driver.findElement(locator);

		} catch (Exception e) {
			LoggerConfig.log.error(e);
		}

		return objElement;
	}
	/**
	 * #Function Name: public By getByLocator(String identifierType, String identifierValue)
	 *
	 * #Description: This method is used to fetch an element using there locators.
	 *
	 * #Input Parameters:
	 * 	        @return return the By Locator
	 *
	 * #Author:Sourabh Baya
	 */

	public By getByLocator(String identifierType, String identifierValue){

		try {

			switch (identifierType) {
				case "id":
					return By.id(identifierValue);

				case "name":
					return By.name(identifierValue);

				case "className":
					return By.className(identifierValue);

				case "xpath":
					return By.xpath(identifierValue);

				case "linkText":
					return By.linkText(identifierValue);

				case "partialLinkText":
					return By.partialLinkText(identifierValue);

				case "cssSelector":
					return By.cssSelector(identifierValue);

				case "tagName":
					return By.tagName(identifierValue);
			}
		}catch (Exception e) {
			LoggerConfig.log.error(e);
		}

		return null;
	}



	/**
	 * #Function Name: public WebElement getElement(Boolean bWillAssert, String strIdentifierType, String strIdentifierValue,
	 * 			String strPage, String strField, String strContOnFail)
	 *
	 * #Description: This method is used to find and return the web element.
	 *
	 * #Input Parameters:
	 * 	        @param bWillAssert - pass the boolean value is true.
	 * 	        @param strPage - Name of the workbook page.
	 * 	        @param strField - Name of the field which is mention in Json file.
	 *          @param strContOnFail - Pass the "continue on failure" string. when the method will get failed still the execution will continue.
	 * 	        @return return the web element
	 *
	 * #Author: Sourabh Baya
	 */

	public WebElement getElement(Boolean bWillAssert,String strPage, String strField, String strContOnFail) throws IOException, JSONException {

		String	strIdentifierType = new POMUtil().read_pom(strPage, strField, 1);
		String strIdentifierValue = new POMUtil().read_pom(strPage, strField, 2);

		String strNew_IdentifierValue = getUpdatedIdentifierValue(strIdentifierValue, strPage, strField);
	
		// Storing to use for StaleElement Exception
		StaticData.strIdentifierType = strIdentifierType;
		StaticData.strIdentifiervalue = strNew_IdentifierValue;

		WebElement objElement = null;
		
		LoggerConfig.log.debug("Getting the web element Object using locator");
		try {

			objElement = new Sync().waitforElement(bWillAssert, strField, strIdentifierType, strNew_IdentifierValue,
					strContOnFail);

		} catch (Exception e){
			LoggerConfig.log.debug(e);
			e.printStackTrace();
		}

		return objElement;

	}

	/**
	 * #Function Name: public String getUpdatedIdentifierValue(String strIdentifierValue, String strPage, String strField)
	 *
	 * #Description: This method is used to identify and update the identifier value "{}<>" and return it.
	 *
	 * #Input Parameters:
	 * 	        @param strIdentifierValue - Value of the locators.
	 * 	        @param strPage - Name of the workbook page.
	 * 	        @param strField - Name of the field which is mention in Json file.
	 * 	        @return return the value in string
	 *
	 * #Author: Sourabh Baya
	 */

	public String getUpdatedIdentifierValue(String strIdentifierValue, String strPage, String strField) throws IOException, JSONException {
		String strNew_IdentifierValue = "";

			if (strIdentifierValue.contains("{") && strIdentifierValue.contains("<")) {
				strNew_IdentifierValue = replaceXpath_POM(strIdentifierValue, strPage, strField);
				strNew_IdentifierValue = replaceXpath_TestData(strNew_IdentifierValue, strPage, strField);
			} else if (strIdentifierValue.contains("{") && !strIdentifierValue.contains("<")) {
				strNew_IdentifierValue = replaceXpath_POM(strIdentifierValue, strPage, strField);
			} else if (!strIdentifierValue.contains("{") && strIdentifierValue.contains("<")) {
				strNew_IdentifierValue = replaceXpath_TestData(strIdentifierValue, strPage, strField);
			} else if (!strIdentifierValue.contains("{") && !strIdentifierValue.contains("<")) {
				strNew_IdentifierValue = strIdentifierValue;
			}
			LoggerConfig.log.debug("Updated Identifier value is " + strNew_IdentifierValue);
			return strNew_IdentifierValue;

	}

	/**
	 * #Function Name: static String replaceXpath_POM(String strXpathValue, String strPage, String strField)
	 * *
	 * #Description: This method is used to replace the xpath value of "{ }".
	 *
	 * #Input Parameters:
	 * 	        @param strXpathValue - value of the xpath which will be mention in workbook
	 * 	        @param strPage - Name of the workbook page.
	 * 	        @param strField - Name of the field which is mention in Json file.
	 * 	        @return return the value in string
	 *
	 * #Author: Sourabh Baya
	 */

	public String replaceXpath_POM(String strXpathValue, String strPage, String strField) throws IOException {
		String updatedxpathValue = "";
		try {
			String strFinalPomPath = new POMUtil().getPomPath(strPage);

			File fFile = new File(strFinalPomPath);

			String strContent = FileUtils.readFileToString(fFile, "utf-8");

			JSONObject jsonObject = new JSONObject(strContent);

			int nDynamicElementsize_xpath = 0;
			List<Integer> listDynamicPlaceHolder_xpath = new LinkedList<Integer>();

			for (int i = 0; i < strXpathValue.length(); i++) {
				if (strXpathValue.charAt(i) == '{') {

					if (listDynamicPlaceHolder_xpath.isEmpty() || (!listDynamicPlaceHolder_xpath.contains(Character.getNumericValue(strXpathValue.charAt(i + 1))))) {
						listDynamicPlaceHolder_xpath.add(Character.getNumericValue(strXpathValue.charAt(i + 1)));

						nDynamicElementsize_xpath++;
					}
				}
			}

			Collections.sort(listDynamicPlaceHolder_xpath);

			String strValue;
			ArrayList<String> objValue = new ArrayList<>();
			JSONObject jsonObject2 = jsonObject.getJSONObject("dynamic Xpath");
			Iterator<String> keysItr = jsonObject2.keys();
			while (keysItr.hasNext()) {
				String strKey = keysItr.next();
				if (strKey.equals(strField)) {
					strValue = jsonObject2.get(strKey).toString();
					if (strValue.startsWith("[")) {
						JSONArray objjsonarray = jsonObject2.getJSONArray(strKey);

						for (int i = 0; i < objjsonarray.length(); i++) {


							objValue.add(objjsonarray.getString(i).toString());

						}

					} else {
						if (strKey.equals(strField)) {
							objValue.add(strValue);
							break;
						}
					}
				}


			}
			if (objValue.isEmpty() && nDynamicElementsize_xpath ==1) {
				objValue.add(strField);
			}
			if(objValue.isEmpty()){
				String strErrorMessage = "Dynamic Value is not present in JSON file for " + strField + " Field";
				Assert.fail(strErrorMessage);
			}

			if (nDynamicElementsize_xpath != objValue.size()) {
				String strErrorMsg = "No of dynamic value placeholder in xpath is "
						+ nDynamicElementsize_xpath + " whereas no of dynamic value defined in JSON for " + strField
						+ " under Dynamic Xpath is " + objValue.size() + " in " + strPage
						+ " JSON.";
				Assert.fail(strErrorMsg);
			}
			updatedxpathValue = updateIdentifierValue_DynamicXpath(objValue,strXpathValue,listDynamicPlaceHolder_xpath);

		} catch (Exception e) {
			LoggerConfig.log.debug(e);
			e.printStackTrace();
		}
		return  updatedxpathValue;
	}

	/**
	 * #Function Name: public String updateIdentifierValue_DynamicXpath(ArrayList<String> strValue, String locatorValue, List<Integer> listDynamicPlaceHolder_xpath)
	 *
	 * #Description: This method is used to replace the xpath "<>" with test data.
	 *
	 * #Input Parameters:
	 * 	        @param strValue - dynamic  value of the xpath in Arraylist
	 * 	        @param locatorValue - xpath value
	 * 	        @param listDynamicPlaceHolder_xpath - List of the place holder in xpath.
	 * 	        @return return the value in string
	 *
	 * #Author: Sourabh Baya
	 */

	public String updateIdentifierValue_DynamicXpath(ArrayList<String> strValue, String locatorValue, List<Integer> listDynamicPlaceHolder_xpath) {

		int nDynamicElementsize_json = strValue.size();
		String updatedLocatorValue = locatorValue;

		for(int i=0;i<nDynamicElementsize_json;i++){
			String toReplace = "{" + listDynamicPlaceHolder_xpath.get(i) + "}";
			System.out.println(strValue.get(i));
			updatedLocatorValue = updatedLocatorValue.replace(toReplace, strValue.get(i));
		}

		return updatedLocatorValue;
	}

	/**
	 * #Function Name: public String replaceXpath_TestData(String strXpathValue, String strPage, String strField)
	 *
	 * #Description: This method is used to replace the xpath "<>" with test data.
	 *
	 * #Input Parameters:
	 * 	        @param strXpathValue - value of the xpath which will be mention in workbook
	 * 	        @param strPage - Name of the workbook page.
	 * 	        @param strField - Name of the field which is mention in Json file.
	 * 	        @return return the value in string
	 *
	 * #Author: Sourabh Baya
	 */

	public String replaceXpath_TestData(String strXpathValue, String strPage, String strField) throws IOException, JSONException {

		String strFinalPomPath = null;
		try {
			strFinalPomPath = new POMUtil().getPomPath(strPage);
		} catch (Exception e) {
			LoggerConfig.log.debug(e);
			e.printStackTrace();
		}
		File fFile = new File(strFinalPomPath);

		String strContent = FileUtils.readFileToString(fFile, "utf-8");

		JSONObject jsonObject = new JSONObject(strContent);

		int nDynamicElementsize_xpath = 0;
		List<Integer> listDynamicPlaceHolder_xpath = new LinkedList<Integer>();

		for (int i = 0; i < strXpathValue.length(); i++) {
			if (strXpathValue.charAt(i) == '<') {

				if(listDynamicPlaceHolder_xpath.isEmpty() || (!listDynamicPlaceHolder_xpath.contains(Character.getNumericValue(strXpathValue.charAt(i+1))))){
					listDynamicPlaceHolder_xpath.add(Character.getNumericValue(strXpathValue.charAt(i+1)));

					nDynamicElementsize_xpath++;
				}
			}
		}

		Collections.sort(listDynamicPlaceHolder_xpath);


		String strValue;
		ArrayList<String> objValue = new ArrayList<>();
		JSONObject jsonObject2 = jsonObject.getJSONObject("dynamic Xpath Test Data");
		Iterator<String> keysItr = jsonObject2.keys();
		while (keysItr.hasNext()) {
			String strKey = keysItr.next();
			if(strKey.equals(strField)) {
				strValue = jsonObject2.get(strKey).toString();
				if (strValue.startsWith("[")) {
					JSONArray objjsonarray = jsonObject2.getJSONArray(strKey);

					for (int i = 0; i < objjsonarray.length(); i++) {


						objValue.add(objjsonarray.getString(i).toString());

					}

				} else {
					if (strKey.equals(strField)) {
						objValue.add(strValue);
						break;
					}
				}
			}


		}
		if(objValue.isEmpty()){
			String strErrorMessage = "Dynamic Xpath Test Data Value is not present in JSON file for "+  strField +" Field";
			Assert.fail(strErrorMessage);
		}

		if (nDynamicElementsize_xpath != objValue.size()) {
			String strErrorMsg = "No of dynamic value placeholder in xpath for Test Data is "
					+ nDynamicElementsize_xpath + " whereas no of dynamic value defined in JSON for " + strField
					+ " under Dynamic Xpath Test Data is " + objValue.size() + " in " + strPage
					+ " JSON.";
			Assert.fail(strErrorMsg);
		}

		String updatedxpathValue =   updateIdentifierValue_TestData(objValue,strXpathValue,listDynamicPlaceHolder_xpath);
		return  updatedxpathValue;
	}

	/**
	 * #Function Name: String updateIdentifierValue_TestData(ArrayList<String> strValue, String locatorValue, List<Integer> listDynamicPlaceHolder_xpath)
	 *
	 * #Description: This method is used to replace the xpath "<>" with test data.
	 *
	 * #Input Parameters:
	 * 	        @param strValue - dynamic  value of the xpath in Arraylist
	 * 	        @param locatorValue - xpath value
	 * 	        @param listDynamicPlaceHolder_xpath - List of the place holder in xpath.
	 * 	        @return return the value in string
	 *
	 * #Author: Sourabh Baya
	 */

	public String updateIdentifierValue_TestData(ArrayList<String> strValue, String locatorValue, List<Integer> listDynamicPlaceHolder_xpath) {

		int nDynamicElementsize_json = strValue.size();
		String updatedLocatorValue = locatorValue;
		String strTestDataSeparator = new CLParams().ReadingStringCLParams("TestData_Separator",new EnvPropVariables().getPreConfigPath(),"PreConfig");

		try {
			for (int i = 0; i < nDynamicElementsize_json; i++) {
				String strField_Value = new TestDataUtil().getfieldValues(strValue.get(i));

				if (strField_Value.contains(strTestDataSeparator)) {
					List<String> field_value_List = Arrays.asList(strField_Value.split(strTestDataSeparator));
					strField_Value = field_value_List.get(StaticData.nMutipleTestData_ForOneField_Counter);
				}

				String toReplace = "<" + listDynamicPlaceHolder_xpath.get(i) + ">";

				updatedLocatorValue = updatedLocatorValue.replace(toReplace, strField_Value);
			}
		} catch (Exception e) {
			LoggerConfig.log.debug(e);
			e.printStackTrace();
		}

		return updatedLocatorValue;

	}

	/**
	 * #Function Name:  public String replaceTestData(String strOriginalString, String strToReplaceValue, String strWithValue)
	 *
	 * #Description: This method is used to replace the dynamic test data value.
	 *
	 * #Input Parameters:
	 * 	        @param strOriginalString - value of the string
	 * 	        @param strToReplaceValue - value of the replace
	 * 	        @param strWithValue - pass the value which we want after replace.
	 * 	        @return return the value in string
	 *
	 * #Author: Sourabh Baya
	 */

	public String replaceTestData(String strOriginalString, String strToReplaceValue, String strWithValue) {

		String strNew_Identifiervalue;
		
		LoggerConfig.log.debug("Replacing the test data value");

		strNew_Identifiervalue = strOriginalString.replace(strToReplaceValue, strWithValue);


		LoggerConfig.log.debug("The value after replacement is "+strNew_Identifiervalue);

		return strNew_Identifiervalue;
	}

}
