package org.oiShoppingList.common.frameworkSupport;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import io.cucumber.core.api.Scenario;
import org.oiShoppingList.common.misc.DataSetup;
import org.oiShoppingList.common.misc.EnvPropVariables;
import org.oiShoppingList.common.misc.StaticData;
import org.oiShoppingList.common.util.CLParams;
import org.oiShoppingList.common.util.FileLocker;
import org.oiShoppingList.common.util.LoggerConfig;
import org.oiShoppingList.common.util.TestDataUtil;
import org.oiShoppingList.mobile.util.MobileDeviceInitialization;
import org.oiShoppingList.mobile.util.MobilePropertiesFile;

public class CucumberPrePostSteps 
{
	/**
	 * #Function:public void beforeEachScenario(Scenario scenario)
	 *
	 * #Description: This method will execute certain functionality (like Creating Driver Instance and loading Test Data in Hashmap) before each test case
	 *
	 * #Input Parameters:
	 *			 @param scenario - Scenario object
	 *
	 * #Author: Sourabh Baya
	 */
	
	public void beforeEachScenario(Scenario scenario)
	{
		try {
			new DataSetup().DataLoad_BeforeAll();

			//This is for Continue on Failure
			StaticData.bIsFailed = false;

			StaticData.scenario = scenario;

			LoggerConfig.log.debug("################################################");
			String strTagName = scenario.getSourceTagNames().toString();
			LoggerConfig.log.debug("Tag Name is : " + strTagName);
			LoggerConfig.log.debug("################################################");

			LoggerConfig.log.debug("################################################################################################################");
			LoggerConfig.log.debug("*************Current Running Feature is : " + new EnvPropVariables().getStrCurrentRunningFeatureName(scenario) + "*******************");
			LoggerConfig.log.debug("*************Current Running Scenario is : " + new EnvPropVariables().getStringCurrentRunningScenarioName(scenario) + "*******************");
			LoggerConfig.log.debug("################################################################################################################");

			//To load all test data in HashMap for the executing test case
			LoggerConfig.log.debug("#### Loading Test Data in Map ####");
			new TestDataUtil().loadTestDataInMap(new EnvPropVariables().getStrCurrentRunningFeatureName(scenario),
					new EnvPropVariables().getStringCurrentRunningScenarioName(scenario));


			if (("Mobile").equalsIgnoreCase(new EnvPropVariables().getTestScope())){
						new MobileDeviceInitialization().triggerPhysicalMobileDevice();

				}
		} catch (Exception e) {
			LoggerConfig.log.debug("Exception occurred in beforeEachScenario method :"+e.getMessage());
			e.printStackTrace();
		}
		
	}
	

	/**
	 * #Function: public void afterEachScenario()
	 *
	 * #Description: This method will execute after each test case, certain functionalities (like clearing the test data Map containing
	 * test data for that test case, closing all the browser instances and failing the test case 
	 * if there is any assertion failure for steps where Continue on Failure is mentioned in Gherkin)
	 * 
	 * #Author: Sourabh Baya
	 */
	
	public void afterEachScenario()
	{
		String strLockerFile = "./target/LockFile_MobilePhysicalDevice.txt";
		FileLocker fl = new FileLocker();

		try
		{
			LoggerConfig.log.debug("Clearing Test data after Scenario");
			//After execution of test case is over, deleting the map.
			StaticData.mapTestData.clear();
			

			//For Mobile
			//To write device details back to Property File
			if((("Mobile").equalsIgnoreCase(new EnvPropVariables().getTestScope())) && (("Physical Real Device").equalsIgnoreCase(new EnvPropVariables().getExecutionEnvironment()))) {
				int nWaitTime = 0;

				while(nWaitTime<30) {

					if(!fl.isLockedByAnotherTestCase(strLockerFile)) {
						new MobilePropertiesFile().writePropertyFileData(StaticData.usingDevice);
						fl.releaseLockAndChannels();
						
						break;
					}
					
					else {
						LoggerConfig.log.debug("Waiting for other thread to release Mobile Device Reading from Properties file");
						TimeUnit.SECONDS.sleep(5);
						nWaitTime=nWaitTime+1;
					}
				}
				
				if(nWaitTime>=29) {
					fl.releaseLockAndChannels();
				}
								

			}

			closeDriverInstance();

			//This is for Continue on Failure
			if(StaticData.bIsFailed)
			{
				Assert.fail();
			}
			
		}
		catch(Exception e)
		{
			LoggerConfig.log.debug(e);
		}
	}


	/**
	 * #Function:public void extractingFeatureName(Scenario scenario)
	 *
	 * #Description: This method will extract the respective feature name for scenario being passed
	 *
	 * #Input Parameters:
	 *		@param scenario - Scenario object
	 * 
	 * #Author: Sourabh Baya
	 */
	
	public String extractingFeatureName(Scenario scenario)
	{
		String strScenarioFilePath = scenario.getId().split(":")[1];
		
		BufferedReader br = null;
		FileReader fr = null;
		String str = "";
		
		try 
		{
			File file = new File(System.getProperty("user.dir") + "/" +strScenarioFilePath); 
			  fr = new FileReader(file);
			  br = new BufferedReader(fr);

				while ((str = br.readLine()) != null) 
				{
				   if(str.contains("Feature:"))
				   {
					   str = str.split(":")[1].trim();
					   break;
				   }
				}
		} 
	    catch (IOException e) 
	    {
		  LoggerConfig.log.debug("Exception encountered while trying to read Feature Name.."+e);
	    }
		finally
		{
			try {
				if(br != null) {
					br.close();
				}
				if(fr != null) {
					fr.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LoggerConfig.log.debug("Exception Occured :"+e);
				e.printStackTrace();
			}
		}

		return str;
	}


	/**
	 * #Function:public void extracting_testdata_pom_filepath(String strTagName)
	 *
	 * #Description: This method will extract the respective test data folder path with respect to Tag name provided
	 *
	 * #Input Parameters:
	 *
	 * #Author: Sourabh Baya
	 */


	public String extracting_testdata_pom_filepath()
	{
		String strTagName = StaticData.scenario.getSourceTagNames().toString();
		String strExtractedTagName;
		strExtractedTagName = extractTagNameWithPath(strTagName);
		String strTestdatafolder_path = null;

		try {
			strTestdatafolder_path =new CLParams().ReadingStringCLParams("Test_Data_Folder_Path", new EnvPropVariables().getPreConfigPath(),"PreConfig");
			if (strTestdatafolder_path.contains("##TagName")) {
				strTestdatafolder_path = strTestdatafolder_path.replace("##TagName", strExtractedTagName);
			}
		} catch (Exception e){
			LoggerConfig.log.debug("Exception Occured:"+e);
			e.printStackTrace();
		}
		LoggerConfig.log.debug("#########################################");
		LoggerConfig.log.debug("#########################################");
		LoggerConfig.log.debug(strTestdatafolder_path);
		LoggerConfig.log.debug("#########################################");
		LoggerConfig.log.debug("#########################################");
		return strTestdatafolder_path;
	}


	/**
	 * #Function:public String extractTagNameWithPath(String tagName)
	 *
	 * #Description: This method will extract the respective Path with tag name and replaces all "##" with "/" to form final path
	 *
	 * #Input Parameters:
	 * 			@param strTagName: Tag Name
	 *          @return strFinaltagWithPath: replacing all "##" with "/"
	 *
	 * #Author: Sourabh Baya
	 */


	public String extractTagNameWithPath(String strTagName)
	{
		String strFinaltagWithPath = "";

		String strTagNameExtract = strTagName.substring(1);
		strTagNameExtract = strTagNameExtract.substring(0, strTagNameExtract.length()-1);

		List<String> tagNameList = Arrays.asList(strTagNameExtract.split(","));
		for(String strEachTagName : tagNameList)
		{
			if(strEachTagName.contains("##"))
			{
				strFinaltagWithPath = strEachTagName.trim().substring(1).trim();
				break;
			}
		}
		strFinaltagWithPath = strFinaltagWithPath.replaceAll("##", "/");
		LoggerConfig.log.debug(strFinaltagWithPath);

		return strFinaltagWithPath;
	}


	public void closeDriverInstance() {
		LoggerConfig.log.debug("### Closing the browser/mobile Instance ### ");
		try {
			if (StaticData.driver != null) {
				StaticData.driver.quit();
			}
		} catch (Exception e) {
			String strErrorMsg = "Error encountered while closing the browser/mobile Instance";

			LoggerConfig.log.error(e);
			Assert.fail(strErrorMsg);
		}

	}
}