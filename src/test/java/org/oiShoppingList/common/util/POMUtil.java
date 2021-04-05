package org.oiShoppingList.common.util;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.oiShoppingList.common.misc.EnvPropVariables;

public class POMUtil
{

	boolean skipRecursivePathCheck = false;
	String strFinalPomFilePath = "";

	/**
	 * #Function Name: public String getPomPath(String strPage)
	 *
	 * #Description: This method is used to get the path of POM file.
	 *
	 * #Input Parameters:
	 *			@param strPage -  name of the workbook
	 * 	        @return  return the POM path in string
	 *
	 * #Author: Sourabh Baya
	 */

	public String getPomPath(String strPage)
	{
		try {

			LoggerConfig.log.debug("Started Retrieving POM File Path");

			String pomFilePath = new CLParams().ReadingStringCLParams("POM_Files_Path", new EnvPropVariables().getPreConfigPath(), "PreConfig");
			String strUserdir = System.getProperty("user.dir");
			String strPomBasePath = strUserdir + pomFilePath;

			searchPOMDir(strPomBasePath, strPage);

			if(!strFinalPomFilePath.equals("")) {
				LoggerConfig.log.debug("POM File Path is :"+strFinalPomFilePath);
			}
			else {
				Assert.fail("POM JSON file associated to "+strPage+" page is not present in POM Directory..");
			}
		}

		catch (Exception e)
		{
			String strErrorMsg = "Error encountered while reading POM Path from MAP"+e.getMessage();

			LoggerConfig.log.error(strErrorMsg,e);
			Assert.fail(strErrorMsg);
		}

		return this.strFinalPomFilePath;

	}


	public void searchPOMDir(String strDirectoryName, String strPage) {

		String strPomFilePath = "";

		try {

			File fPOMDir = new File(strDirectoryName);
			File[] fileDirList = fPOMDir.listFiles();

			for(File fileDir : fileDirList) {

				if(!skipRecursivePathCheck) {

					if (!fileDir.isDirectory()) {
						strPomFilePath = checkIfPOMFilePresent(fileDir, strPage);

						if (!strPomFilePath.equals("")) {
							strFinalPomFilePath = strPomFilePath;
							skipRecursivePathCheck = true;
							break;
						}
					} else if (fileDir.isDirectory()) {

						searchPOMDir(fileDir.getAbsolutePath(), strPage);
					}

				}

			}

		}
		catch(Exception e) {
			Assert.fail("Exception encountered while trying to retrieve POM JSON File..");
		}

	}


	public String checkIfPOMFilePresent(File file, String page) {

		String strPOMFilePath = "";

		try {
			String fileName = file.getName();
			String finalFileName = fileName.substring(0, fileName.length() - 5);

			if(finalFileName.equalsIgnoreCase(page)) {

				strPOMFilePath = file.getAbsolutePath();
			}
		}
		catch(Exception e) {
			Assert.fail("Exception encountered while trying to retrieve POM JSON File..");
		}

		return strPOMFilePath;
	}



	/**
	 * #Function Name: public String read_pom(String strPage, String strField, int nFlag)
	 *
	 * #Description: This method is used to read the JSON file.
	 *
	 * #Input Parameters:
	 *			@param strPage -  name of the workbook
	 *		    @param strField - Name of the field which is mention in Json file.
	 *		    @param nFlag - value of the column.
	 * 	        @return  return the POM path in string
	 *
	 * #Author: Sourabh Baya
	 */
	public String read_pom(String strPage, String strField, int nFlag) throws IOException, JSONException {

		String strFinalPomPath = getPomPath(strPage);

		File fFile = new File(strFinalPomPath);

		String strContent = FileUtils.readFileToString(fFile, "utf-8");

		JSONObject jsonObject = new JSONObject(strContent);

		String strPom = "";

		JSONObject jsonObject1 = jsonObject.getJSONObject("WebElement");

		try
		{
			Iterator<String> keysItr = jsonObject1.keys();
			while (keysItr.hasNext()) {

				String strKey = keysItr.next();

				String[] element = strKey.split(",");
				int length = element.length;

				for (int i = 0; i < length; i++) {

					String str = element[i];
					str = str.trim();
					if (str.equals(strField)) {
						strPom = jsonObject1.get(strKey).toString();
						break;
					}

				}

			}
		}
		catch(Exception e)
		{
			LoggerConfig.log.debug("Error reading POM JSON"+e);
			String strErrorMsg = "Error reading POM JSON"+e.getMessage();

			Assert.fail(strErrorMsg);
		}

		if(strPom.equals(""))
		{
			LoggerConfig.log.debug("FieldName "+strField+ " is not present in POM "+strPage+" JSON");
			String strErrorMsg = "FieldName "+strField+ " is not present in POM "+strPage+" JSON";

			Assert.fail(strErrorMsg);
		}

		if(nFlag ==1){
			try {
				String[] str = strPom.split("=>");
				String identifierType = str[0];
				strPom = identifierType;
			}catch (Exception e){

				LoggerConfig.log.debug(e);
				String strErrorMessage = "Error encountered xpath identifier is not mentioned for "+strField + " field in" +strPage+ " JSON" ;
				Assert.fail(strErrorMessage);

			}
		}else{
			try {
				String[] str = strPom.split("=>");
				String identifierValue = str[1];
				strPom = identifierValue;
			}catch (Exception e){

				LoggerConfig.log.debug(e);
				String strErrorMessage = "Error encountered xpath identifier is not mentioned for "+strField + " field ";
				Assert.fail(strErrorMessage);

			}
		}

		return strPom.trim();
	}


	public String getJSONvaluebyName(String strField,String strJsonFileName, String strJSONObjectName) {

		String strFieldValue="";

		try
		{
			File file = new File(strJsonFileName);
			String strContent = FileUtils.readFileToString(file, "utf-8");

			JSONObject jsonObject = new JSONObject(strContent);
			JSONObject PreConfigJsonObject = null;

			switch (strJSONObjectName){
				case "PreConfig":
					 PreConfigJsonObject = jsonObject.getJSONObject("PreConfig");
					 break;

				case "Mobile":
					 PreConfigJsonObject = jsonObject.getJSONObject("Mobile");
					 break;

			}

			if(PreConfigJsonObject != null) {
				strFieldValue = PreConfigJsonObject.getString(strField);
			}

		}

		catch (Exception e)
		{
		String strErrorMsg = strField +" value is not present in" +strJsonFileName +" file";
		LoggerConfig.log.error(strErrorMsg,e);
		e.printStackTrace();
		}

		return strFieldValue;

	}

	/**
	 * #Function Name:  public HashMap<String, String> getExecutionEnvironmentDetails(String strHeader, String strJsonFileName)
	 *
	 * #Description: This method is used to get the data of environment details from preconfig file.
	 *
	 * #Input Parameters:
	 *			@param strJsonFileName - Name of the workbook file.
	 * 			@param strPlatformName - value of the header which is mention in preconfig file.
	 * 	        @return return the hash map value in key and object
	 *
	 * #Author: Sourabh Baya
	 */
	public HashMap<String, String> getExecutionMobileEnvironmentDetails(String strExecutionEnvironment, String strJsonFileName, String strPlatformName)
	{
		HashMap<String, String> mapExecutionEnvironmentMobileDetailsHM1 = new HashMap<String, String>();

		String strParamValue = "";

		LoggerConfig.log.debug("Getting the data of environment details from PreConfiguration file");
		try
		{

			File file = new File(strJsonFileName);
			String strContent = FileUtils.readFileToString(file, "utf-8");

			JSONObject jsonObject = new JSONObject(strContent);
			JSONObject jsonObject1= jsonObject.getJSONObject(strExecutionEnvironment).getJSONObject(strPlatformName);

			Iterator<String> keysItr = jsonObject1.keys();

			while (keysItr.hasNext()) {
				String strKey = keysItr.next();

				String strCLIParamValue = new CLParams().ReadingCLParamsWebMob("Mobile", strExecutionEnvironment, strKey);

				if(strCLIParamValue==null || strCLIParamValue.equals(""))
				{
					strParamValue = jsonObject1.get(strKey).toString();

					if (strParamValue.contains("[")) {
						StringBuilder strNew_Value = new StringBuilder();
						JSONArray objjsonarray = jsonObject1.getJSONArray(strKey);

						for (int i = 0; i < objjsonarray.length(); i++) {
							strParamValue = objjsonarray.get(i).toString();
							StringBuilder str = new StringBuilder(strParamValue);

							if (i != (objjsonarray.length() - 1)) {
								str = str.append(",");
							}
							strNew_Value = strNew_Value.append(str);
						}

						strParamValue = strNew_Value.toString();
					}
				}
				else
				{
					strParamValue = strCLIParamValue;
				}

				mapExecutionEnvironmentMobileDetailsHM1.put(strKey,strParamValue);
			}

			LoggerConfig.log.debug("########################################### mapExecutionEnvironmentDetailsHM #####"+mapExecutionEnvironmentMobileDetailsHM1);



		}
		catch(Exception e)
		{
			LoggerConfig.log.debug("Error reading PreConfiguration " + strJsonFileName + " file"+e);
			String strErrorMsg = "Error reading PreConfiguration " + strJsonFileName + " file"+e.getMessage();

			Assert.fail(strErrorMsg);
		}

		return mapExecutionEnvironmentMobileDetailsHM1;

	}
}