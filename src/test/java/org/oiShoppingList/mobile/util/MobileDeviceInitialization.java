package org.oiShoppingList.mobile.util;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.oiShoppingList.common.misc.EnvPropVariables;
import org.oiShoppingList.common.misc.StaticData;
import org.oiShoppingList.common.util.CLParams;
import org.oiShoppingList.common.util.FileLocker;
import org.oiShoppingList.common.util.LoggerConfig;
import org.oiShoppingList.common.util.POMUtil;
import org.oiShoppingList.mobile.frameworkSupport.MobileDriverFactory;

public class MobileDeviceInitialization 
{
	HashMap<String, String> mapMobileExecutionEnvironmentDetailsHM = new HashMap<String, String>();

	public MobileDeviceInitialization() {
		String strMobileJsonPath = new EnvPropVariables().getMobileJsonPath();
		String strMobileExecutionEnvironment = new EnvPropVariables().getExecutionEnvironment();
		String strMobilePlatform = new EnvPropVariables().getStrMobilePlatform();
		mapMobileExecutionEnvironmentDetailsHM = new POMUtil().getExecutionMobileEnvironmentDetails(strMobileExecutionEnvironment, strMobileJsonPath, strMobilePlatform);
	}

	/**
	 * #Function Name: public void triggerPhysicalMobileDevice()
	 *
	 * #Description: This method is used to trigger physical mobile device in the respective platform and environment
	 *
	 * #Author: Sourabh Baya
	 */
	
	public void triggerPhysicalMobileDevice() {

		String strLockerFile = "./target/LockFile_MobilePhysicalDevice.txt";
		FileLocker fl = new FileLocker();

		String strMobileDeviceName = "";
		List<String> mobileDeviceNameList=new ArrayList<>();
		String strTempDeviceFileName="\\target\\temporaryDeviceList.properties";

		if(new EnvPropVariables().getStrMobilePlatform().equalsIgnoreCase("Android")) {
			strMobileDeviceName = mapMobileExecutionEnvironmentDetailsHM.get("Android Device Name");
		}
		else if(new EnvPropVariables().getStrMobilePlatform().equalsIgnoreCase("IOS")) {

		}

		if(strMobileDeviceName.contains(",")) {
			mobileDeviceNameList= Arrays.asList(strMobileDeviceName.split(","));
		} else {
			mobileDeviceNameList.add(strMobileDeviceName);
		}
		
		try
		{
			int nWaitTime = 0;
			while (nWaitTime < 30) {

				if (!fl.isLockedByAnotherTestCase(strLockerFile)) {

					for (String strMobileDevice : mobileDeviceNameList) {

						LoggerConfig.log.debug("##################################################");
						LoggerConfig.log.debug("Mobile device is "+strMobileDevice);
						LoggerConfig.log.debug("##################################################");

						if (!new MobilePropertiesFile().verifyDeviceAvailability(strMobileDevice,strTempDeviceFileName)) {
							LoggerConfig.log.debug("###################### Inside Loop #####################");
							LoggerConfig.log.debug("Device Name inside loop is "+strMobileDevice);
							LoggerConfig.log.debug("######################################## Device Availability #######################################");
							continue;
						}

						StaticData.usingDevice = new MobilePropertiesFile().getMobileData(strMobileDevice);
						new MobilePropertiesFile().deletePropertyFileData(StaticData.usingDevice.get(0));

						LoggerConfig.log.debug("*******************Using Device is :"+StaticData.usingDevice.get(0)+" ************************");

						fl.releaseLockAndChannels();

						break;
					}
					String strSystemPort = toRetrieveSystemPort(StaticData.usingDevice.get(0));
					new MobileDriverFactory().createInstanceForPhysicalDevice(StaticData.usingDevice.get(1), strSystemPort);
					LoggerConfig.log.debug("***************************Instance Created**********************");
					break;

				} else {
					LoggerConfig.log.debug("Waiting for other thread to release Mobile Device Reading from Properties file");
					TimeUnit.SECONDS.sleep(5);
					nWaitTime = nWaitTime + 1;
				}

			}

			if (nWaitTime >= 29) {
				LoggerConfig.log.debug("*****************************Lock Released*******************************");
				fl.releaseLockAndChannels();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			LoggerConfig.log.debug(e);
		}		
	}


	/**
	 * #Function Name: public void toRetrieveSystemPort(String strDeviceName)
	 *
	 * #Description: This method is used to retrieve SystemPort (used to provide as capability)
	 *                for a particular device from AppiumServerConfig.json
	 *
	 * #Input Parameters:
	 * 			@param strDeviceName - Name of Device(Samsung,Pixel etc..)
	 * 			@return returns  port value
	 *
	 * #Author: Sourabh Baya
	 */

	public String toRetrieveSystemPort(String strDeviceName)
	{
		String strSystemPort = "";

		try {
			String strAppiumServerConfigFolderPath = System.getProperty("user.dir") + new CLParams().ReadingStringCLParams("Appium_Config_Json_File", new EnvPropVariables().getPreConfigPath(),"PreConfig");
			File fFile = new File(strAppiumServerConfigFolderPath);
			String strContent ;

			strContent = FileUtils.readFileToString(fFile, "utf-8");

			JSONObject jsonObject = new JSONObject(strContent);
			List<String> lList1 = new ArrayList<String>(jsonObject.keySet());

			JSONObject jsonObject2;


			for (String strKey : lList1) {
				if (strKey.equals(strDeviceName)) {
					jsonObject2 = jsonObject.getJSONObject(strKey);
					Iterator<String> keysItr = jsonObject2.keys();

					while (keysItr.hasNext()) {
						String strkey = keysItr.next();
						if (strkey.equalsIgnoreCase("SystemPort")) {
							strSystemPort = jsonObject2.get(strkey).toString();
						}
					}

				}
			}
		}
		catch(Exception e)
		{
			LoggerConfig.log.debug("Error encountered while trying to Retrieve System Port"+e);
		}

		return strSystemPort;
	}

}
