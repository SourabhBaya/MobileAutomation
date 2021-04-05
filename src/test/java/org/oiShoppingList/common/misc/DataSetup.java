package org.oiShoppingList.common.misc;

import org.junit.Assert;
import org.oiShoppingList.common.util.CLParams;
import org.oiShoppingList.common.util.LoggerConfig;
import org.oiShoppingList.common.util.POMUtil;


public class DataSetup 
{
	/**
	 * #Function Name: public void DataLoad_BeforeAll()
	 *
	 * #Description:This method will initialize all static data value.
	 * 
	 * #Author: Sourabh Baya
	 */
	public  void DataLoad_BeforeAll()
	{
		POMUtil pomUtil = new POMUtil();
		CLParams clParams = new CLParams();
		try {
			LoggerConfig.log.debug("######## Reading Pre-Configuration data from Json file ########");
			StaticData.strCaptureScreenshotOnFailureOnly = clParams.ReadingStringCLParams("Capture_Screenshot_on_Failure_Only",new EnvPropVariables().getPreConfigPath(),"PreConfig");
			if(new EnvPropVariables().getTestScope().equalsIgnoreCase("Mobile")) {
				pomUtil.getExecutionMobileEnvironmentDetails(new EnvPropVariables().getExecutionEnvironment(), new EnvPropVariables().getMobileJsonPath(), new EnvPropVariables().getStrMobilePlatform());
			}
		}
		catch(Exception e) {
			String strErrorMsg = "Error Encountered while reading Pre-Configuration data from Json : "+e.getMessage();
			LoggerConfig.log.error(strErrorMsg);
			Assert.fail(strErrorMsg);
		}
	}
}