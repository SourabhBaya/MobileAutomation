package org.oiShoppingList.common.frameworkSupport;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import org.oiShoppingList.common.misc.EnvPropVariables;
import org.oiShoppingList.common.util.CLParams;
import org.oiShoppingList.common.util.LoggerConfig;
import org.oiShoppingList.common.util.POMUtil;
import org.oiShoppingList.mobile.util.AppiumServerConfig;

public class PostRunSetup
{

	public static void main(String[] args) {
		CLParams clParams = new CLParams();
		PostRunSetup postrunObject = new PostRunSetup();


		LoggerConfig.log.debug("***************************************************************************");
		LoggerConfig.log.debug("##################### Post-Execution Setup Started #####################");
		LoggerConfig.log.debug("***************************************************************************");

		String strTestScope = clParams.ReadingStringCLParams("Test_Scope", new EnvPropVariables().getPreConfigPath(),"PreConfig");


		////Mobile - To stop selenium and appium servers automatically
		String strToStartSeleniumAppiumServers = clParams.ReadingStringCLParams("To_start_Selenium_and_Appium_Servers_automatically", new EnvPropVariables().getPreConfigPath(),"PreConfig");
		if((("Mobile").equalsIgnoreCase(strTestScope.trim()))
				&& (("Yes").equalsIgnoreCase(strToStartSeleniumAppiumServers))) {
			postrunObject.mobileStopSeleniumAppiumServerAutomatic();
		}

		LoggerConfig.log.debug("##################### Post-Execution Setup Completed #####################");

	}

	public void mobileStopSeleniumAppiumServerAutomatic() {
		CLParams clParams = new CLParams();

		String strMobileJsonPath = new EnvPropVariables().getMobileJsonPath();
		String strMobileExecutionEnvironment = clParams.ReadingStringCLParams("Execution_Environment", strMobileJsonPath,"Mobile");

		AppiumServerConfig appiumServerConfig = new AppiumServerConfig();

		if((("Physical Real Device").equalsIgnoreCase(strMobileExecutionEnvironment.trim())))
		{
			String strMobilePlatform = clParams.ReadingStringCLParams("Platform", strMobileJsonPath,"Mobile");
			HashMap<String, String> mapExecutionEnvironmentDetailsHM = new HashMap<String, String>();
			mapExecutionEnvironmentDetailsHM =new POMUtil().getExecutionMobileEnvironmentDetails(strMobileExecutionEnvironment, strMobileJsonPath,strMobilePlatform);

			String strMobileName = "";

			switch (strMobilePlatform){
				case "Android":
					strMobileName = mapExecutionEnvironmentDetailsHM.get("Android Device Name");
					break;

				case "IOS":
					strMobileName = mapExecutionEnvironmentDetailsHM.get("IOS Device Name");
					break;
			}

			List<String> physicalDeviceList = appiumServerConfig.readDevicePropertiesFile(strMobileName);

			LoggerConfig.log.debug("##################################");
			LoggerConfig.log.debug("physicalDeviceList : "+physicalDeviceList);

			List<String> appiumServerPortList = appiumServerConfig.readAppiumServerJson(physicalDeviceList, false);

			LoggerConfig.log.debug("##################################");
			LoggerConfig.log.debug("AppiumServerPortList : "+appiumServerPortList);

			if (!appiumServerPortList.isEmpty()) {
				for (String strPort : appiumServerPortList) {
					LoggerConfig.log.debug("###########################################");
					LoggerConfig.log.debug("Port is : " + strPort);
					appiumServerConfig.StopAppiumAtPort(strPort);
				}
			}
			appiumServerConfig.StopSeleniumHubAtPort("4444");
		}
	}
}