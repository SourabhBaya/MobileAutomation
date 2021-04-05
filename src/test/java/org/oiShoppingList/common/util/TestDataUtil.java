package org.oiShoppingList.common.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.oiShoppingList.common.frameworkSupport.CucumberPrePostSteps;
import org.oiShoppingList.common.misc.EnvPropVariables;
import org.oiShoppingList.common.misc.StaticData;


public class TestDataUtil 
{
	
	//To Load Test Data from .json files
	/**
	 * #Function Name:public void loadTestDataInMap()
	 *
	 * #Description:This method is used to load all the test data json file using map.
	 *
	 * #Input Parameters:
	 *
	 * #Author: Sourabh Baya
	 */
	public void loadTestDataInMap(String strCurrentFeatureName, String strCurrentRunningScenarioName)
	{   
		String strTestDataFolderPath = new CucumberPrePostSteps().extracting_testdata_pom_filepath();
		LoggerConfig.log.debug("Loading all the test data Json file using map");
		HashMap<String, String> mapCommonTestData = new HashMap<String, String>();
		String strEnvironment = System.getProperty("environment");
		
		StaticData.mapTestData = new HashMap<String, String>();
		
		String strJsonFileFolderName = strTestDataFolderPath + strEnvironment;
		String strJsonFileName = fileNameCompareConvert(strJsonFileFolderName, strCurrentFeatureName, ".json");
		
		String strFinalTestDataFolderPath = strTestDataFolderPath + strEnvironment + "/" + strJsonFileName +"";
		String strTCName = strCurrentRunningScenarioName;
		String strTestDataSeparator = new CLParams().ReadingStringCLParams("TestData_Separator",new EnvPropVariables().getPreConfigPath(),"PreConfig");


		try 
		{
			String strTCFilePath = strFinalTestDataFolderPath;
			
			if(strJsonFileName.equals(""))
			{
				LoggerConfig.log.debug("Test Data Json file is not present for feature name : " + strCurrentFeatureName);
				Assert.fail("Test Data Json file is not present for feature name : " + strCurrentFeatureName);
				//System.exit(0);
			}
			
			File fFile = new File(strTCFilePath);
		    String strContent = FileUtils.readFileToString(fFile, "utf-8");
		    
		    JSONObject jsonObject = new JSONObject(strContent);
	    
		    //To handle multiple Test Case referring to same Test Data
		    
		    JSONObject jsonObject2=null;
		    
		    List<String> lList1 = new ArrayList<String>(jsonObject.keySet());

		    String strJsonKey;
		    Boolean bTCFlag=false;
            List<String> lList2 = new ArrayList<String>();

            
            for (String strKey : lList1) 
            {   
               if(strKey.contains("##"))
               {
				   strJsonKey=strKey;
                      lList2.addAll(Arrays.asList(strKey.split("##")));
                      
                      for (String string : lList2) 
                      {
                    	  if(string.trim().equalsIgnoreCase(strTCName))
                    	  {
	                         LoggerConfig.log.debug("Test Case Found");
	                         bTCFlag=true;
	                         jsonObject2=jsonObject.getJSONObject(strJsonKey);
	                         LoggerConfig.log.debug("Test Case JSON Object key value pairs are "+jsonObject2);
	                         break;
                    	  }
                      }
				   //strJsonKey="";
                      lList2.clear();
                }
                else
                {      
                     //jsonKey=str;
                     if(strKey.trim().equalsIgnoreCase(strTCName))
                     {
                    	 bTCFlag=true;
                         jsonObject2=jsonObject.getJSONObject(strKey);
                         LoggerConfig.log.debug("Test Case JSON Object key value pairs are "+jsonObject2);
                         break;
                      }
                     
                 }
            }
            
            if(!bTCFlag)
            {
		    	LoggerConfig.log.error("Test case name \"" +strTCName+"\" not found in the JSON file \""+strJsonFileName+"\"");

				StaticData.scenario.write("Test case name \"" +strTCName+"\" not found in the JSON file \""+strJsonFileName+"\"");

		    }

			if(bTCFlag)
			{
				Iterator<String> keysItr = jsonObject2.keys();
				while (keysItr.hasNext()) {
					String strkey = keysItr.next();
					String strvalue ;

					if (strkey.contains("AddTestData")) {
						strvalue = jsonObject2.get(strkey).toString();

						try {
							JSONObject jsonObject3 = jsonObject.getJSONObject(strvalue);
							Iterator<String> keysItr2 = jsonObject3.keys();

							while (keysItr2.hasNext()) {
								String strkey2 = keysItr2.next();
								String strvalue2 = jsonObject3.get(strkey2).toString();


								if (strvalue2.contains("[")) {
									StringBuilder strNew_Value = new StringBuilder();
									JSONArray objjsonarray = jsonObject3.getJSONArray(strkey2);

									for (int i = 0; i < objjsonarray.length(); i++) {
										strvalue2 = objjsonarray.get(i).toString();
										StringBuilder str = new StringBuilder(strvalue2);

										if (i != (objjsonarray.length() - 1)) {
											str = str.append(strTestDataSeparator);
										}
										strNew_Value = strNew_Value.append(str);
									}

									strvalue2 = strNew_Value.toString();
								}

								if (mapCommonTestData.containsKey(strkey2)) {
									LoggerConfig.log.debug("Mutliple/Same common test data sections have same attribute name " + strkey2 + " in Json file.");
								} else {
									mapCommonTestData.put(strkey2.toLowerCase().trim(), strvalue2);
								}

							}
						} catch (Exception e) {
							LoggerConfig.log.debug("Common Test Data " + strvalue + " is not defined in Json file."+e);
							continue;
						}

						continue;

					}


					strvalue = jsonObject2.get(strkey).toString();

					if (strvalue.contains("[")) {
						StringBuilder strNew_Value = new StringBuilder();
						JSONArray objjsonarray = jsonObject2.getJSONArray(strkey);

						for (int i = 0; i < objjsonarray.length(); i++) {
							strvalue = objjsonarray.get(i).toString();
							StringBuilder str = new StringBuilder(strvalue);

							if (i != (objjsonarray.length() - 1)) {
								str = str.append(strTestDataSeparator);
							}
							strNew_Value = strNew_Value.append(str);
						}

						strvalue = strNew_Value.toString();
					}

					StaticData.mapTestData.put(strkey.toLowerCase().trim(), strvalue);

				}

				if (!(mapCommonTestData == null)) {

					for(Map.Entry<String,String> entry : mapCommonTestData.entrySet()){
						String strKey=entry.getKey();
						if(StaticData.mapTestData.containsKey(strKey)){
							LoggerConfig.log.debug("Common test data have same attribute name " + strKey + " as defined in attribute at Test Case level in Json file");
						}
						else{
							String strKeyValue = entry.getValue();
							StaticData.mapTestData.put(strKey.toLowerCase().trim(), strKeyValue);
						}
					}
				}

				LoggerConfig.log.debug(StaticData.mapTestData);

			}

		}

		catch(Exception e1)
		{
			
			LoggerConfig.log.debug(e1);
			
			String strErrorMsg = e1.getMessage();

			Assert.fail(strErrorMsg);
		}
	}


	/**
	 * #Function Name:public String getfieldValues(String strFieldName)
	 *
	 * #Description:This method is used to return the value of particular field.
	 *
	 * #Input Parameters:
	 * 			@param strFieldName - Name of the field which is mention in Json file.
	 * 		    @return return the value in string
	 *
	 * #Author: Sourabh Baya
	 */
	public String getfieldValues(String strFieldName)
	{
		String strField = strFieldName.toLowerCase().trim();
		
		String strFieldData = "";
		
		try
		{   
			LoggerConfig.log.debug("Getting the value of Particular field");	
			for (String strKey : StaticData.mapTestData.keySet())
		    {
		    	if(strKey.equals(strField))
		    	{
		    		strFieldData = StaticData.mapTestData.get(strKey);
		    		//LoggerConfig.log.debug(value);
		    		break;
		    	}

		    } 
		}
		catch(Exception e)
		{
			LoggerConfig.log.debug(e);
			
			String strErrorMsg = "Error encountered while reading "+strField+"'s test data from map." +e.getMessage();
			//ElasticSearch.onFailure(errorMsg);
			Assert.fail(strErrorMsg);
		}
		
		try
		{   
	    	while(strFieldData.contains("<") && strFieldData.contains(">"))
	    	{
	    		int nStartindex = strFieldData.indexOf('<');
	    		int nEndindex = strFieldData.indexOf('>');
	    		String strGetStringToReplace = strFieldData.substring(nStartindex, nEndindex+1);
	    		String strTestDataField = strGetStringToReplace.substring(1, strGetStringToReplace.length()-1);
	    		
	    		String strTestDataValue =new TestDataUtil().getfieldValues(strTestDataField);

				LoggerConfig.log.debug("Data before replacement is "+strTestDataValue);
				if(strTestDataValue==null || strTestDataValue.equals("")) {
					LoggerConfig.log.debug("***********************************************************");
					LoggerConfig.log.debug("Test Data is not replaced and the value is "+strFieldData);
					LoggerConfig.log.debug("***********************************************************");
					//strFieldData = strFieldData;
					break;
				}else {
					strFieldData =new ElementUtil().replaceTestData(strFieldData, strGetStringToReplace, strTestDataValue);
				}
	    		
	    		LoggerConfig.log.debug(strFieldData);

	    	}
		}
		catch(Exception e2)
		{
			LoggerConfig.log.debug(e2);
			
			String strErrorMsg = "Error encountered while reading and parsing "+strField+"'s test data from map." +e2.getMessage();
			//ElasticSearch.onFailure(errorMsg);
			Assert.fail(strErrorMsg);
		}
		
		try
		{
			if(strFieldData.startsWith("#") && strFieldData.contains("Today()"))
			{
				String strFormatDate = "";
				int nDaysAddSub = 0;
				boolean bAdd = false;
				boolean bSub = false;
				
				List<String> dateList1 = Arrays.asList(strFieldData.split("@"));
				List<String> dateList2 = null;
				
				if(dateList1.get(0).contains("+") || dateList1.get(0).contains("-"))
				{			
					if(dateList1.get(0).contains("+"))
					{
						bAdd = true;
						dateList2 = Arrays.asList(dateList1.get(0).trim().split("\\+"));
					}
					else if(dateList1.get(0).contains("-"))
					{
						bSub = true;
						dateList2 = Arrays.asList(dateList1.get(0).trim().split("\\-"));
					}
					try
					{
						if(dateList2 != null && !dateList2.get(1).equals(""))
						{
							//If + is present then putting the value
							nDaysAddSub = Integer.parseInt(dateList2.get(1));
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						//If + is added but no value post that, then considering today's date only
						LoggerConfig.log.error(e);
						nDaysAddSub = 0;
					}
				}
				else
				{
					//If + is not present then considering today's date only
					nDaysAddSub = 0;
				}
			
				try
				{
					if(!dateList1.get(1).equals(""))
					{
						//Date Format is provided
						strFormatDate = dateList1.get(1).trim();
					}
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					//No Date Format is provided, hence default date format is set
					LoggerConfig.log.error(e);
					strFormatDate = "dd/MM/yyyy";
				}
				
				DateFormat dateFormat = new SimpleDateFormat(strFormatDate);
				Calendar c = Calendar.getInstance();
				
				if(bAdd)
				{
					c.add(Calendar.DATE, +nDaysAddSub);
				}
				else if(bSub)
				{
					c.add(Calendar.DATE, -nDaysAddSub);
				}
				
				strFieldData = dateFormat.format(c.getTime());
				
			}
		}
		catch(Exception e3)
		{
			LoggerConfig.log.debug(e3);
			
			String strErrorMsg = "Error encountered while parsing data as per defined format of "+strField+"'s test data in Json file." +e3.getMessage();
			//ElasticSearch.onFailure(errorMsg);
			Assert.fail(strErrorMsg);
		}

		LoggerConfig.log.debug(strFieldData);
		
		return strFieldData;
	}

	/**
	 * #Function Name: public String fileNameCompareConvert(String strLocation, String strFileName, String strFileExt)
	 *
	 * #Description: This method is used to compare the file name provided is present in the given location
	 *
	 * #Input Parameters:
	 *           @param strLocation - Name of the folder
	 *           @param strFileName - Name of the file
	 *           @param strFileExt - Extension of file
	 *			 @return return the value in string
	 *
	 * #Author: Sourabh Baya
	 */

	public String fileNameCompareConvert(String strLocation, String strFileName, String strFileExt)
	{		
		String strUpdatedFileName = "";
		
		strFileName = strFileName + strFileExt;
		
		File fFolder = new File(strLocation);
		File[] listOfFilesArr = fFolder.listFiles();

		for (int i = 0; i < listOfFilesArr.length; i++) 
		{
		  if (listOfFilesArr[i].isFile()) 
		  {
		    LoggerConfig.log.debug("Actual File Name " + listOfFilesArr[i].getName());
		    
		    if(listOfFilesArr[i].getName().equalsIgnoreCase(strFileName))
		    {
		    	
		    	strUpdatedFileName = listOfFilesArr[i].getName();
		    	
		    	break;
		    }
		    
		  } 
		  
		  else if (listOfFilesArr[i].isDirectory()) 
		  {
		    LoggerConfig.log.debug("Not considering Directory Names");
		  }
		  
		}
		
		return strUpdatedFileName;
	}

}
