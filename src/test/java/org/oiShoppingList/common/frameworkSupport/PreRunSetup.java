package org.oiShoppingList.common.frameworkSupport;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.Assert;
import org.oiShoppingList.common.misc.EnvPropVariables;
import org.oiShoppingList.common.util.CLParams;
import org.oiShoppingList.common.util.LoggerConfig;
import org.oiShoppingList.common.util.POMUtil;
import org.oiShoppingList.mobile.util.AppiumServerConfig;
import org.oiShoppingList.mobile.util.MobilePropertiesFile;



public class PreRunSetup
{

	public static void main(String[] args) {

		PreRunSetup preRunObject = new PreRunSetup();
		CLParams clParams = new CLParams();


		String strMobileExecutionEnvironment = "";
		String strMobileJsonPath = new EnvPropVariables().getMobileJsonPath();

		String strTestScope = clParams.ReadingStringCLParams("Test_Scope", new EnvPropVariables().getPreConfigPath(),"PreConfig");

		 if(strTestScope.equalsIgnoreCase("Mobile")) {
			strMobileExecutionEnvironment = clParams.ReadingStringCLParams("Execution_Environment", strMobileJsonPath,"Mobile");
		}

		preRunObject.deleteReportingDirectory();

		LoggerConfig.log.debug("###################### Pre-Execution Setup Started ######################");

		preRunObject.startExecutionTime();

		//Mobile - Copying the Mobile Device Properties so that the original is not impacted
		if((strTestScope.trim().equalsIgnoreCase("Mobile")) &&
				(strMobileExecutionEnvironment.trim().equalsIgnoreCase("Physical Real Device"))) {
			preRunObject.MobileDevicePropertyFileBackup();
			String strMobilePlatform = clParams.ReadingStringCLParams("Platform", strMobileJsonPath, "Mobile");
			preRunObject.checkMobileDeviceNameInPropertyFile(strMobilePlatform);
		}

		//Mobile - To start selenium and appium servers automatically
		String strToStartSeleniumAppiumServers = clParams.ReadingStringCLParams("To_start_Selenium_and_Appium_Servers_automatically", new EnvPropVariables().getPreConfigPath(), "PreConfig");
		if((strTestScope.trim().equalsIgnoreCase("Mobile")) &&
				(strToStartSeleniumAppiumServers.equalsIgnoreCase("Yes"))) {
			preRunObject.mobileStartSeleniumAppiumServerAutomatic();
		}

		LoggerConfig.log.debug("###################### Pre-Execution Setup Completed ######################");
	}

	public void MobileDevicePropertyFileBackup() {
		try
		{
			File file1 = new File(System.getProperty("user.dir") + new CLParams().ReadingStringCLParams("Mobile_Device_Properties_File", new EnvPropVariables().getPreConfigPath(),"PreConfig"));
			File temporaryFile = new File(System.getProperty("user.dir") + "\\target\\temporaryDeviceList.properties");
			File countFile = new File(System.getProperty("user.dir") + "\\target\\DeviceListCount.properties");
			FileUtils.copyFile(file1,temporaryFile);
			FileUtils.copyFile(file1,countFile);
		}
		catch(Exception e) {
			LoggerConfig.log.debug("Exception encountered in MobileDevicePropertyFileBackup : "+e.getMessage());
		}
	}


	//To start Selenium and Appium Servers automatically
	public void mobileStartSeleniumAppiumServerAutomatic() {
		try {
			CLParams clParams = new CLParams();
			AppiumServerConfig appiumServerConfig = new AppiumServerConfig();
			PreRunSetup preRunObject = new PreRunSetup();

			String strMobileJsonPath = new EnvPropVariables().getMobileJsonPath();
			String strMobilePlatform = clParams.ReadingStringCLParams("Platform", strMobileJsonPath, "Mobile");
			String strMobileExecutionEnvironment = clParams.ReadingStringCLParams("Execution_Environment", strMobileJsonPath, "Mobile");

			if ((strMobileExecutionEnvironment.trim().equalsIgnoreCase("Physical Real Device"))) {
				HashMap<String, String> mapExecutionEnvironmentDetailsHM ;
				mapExecutionEnvironmentDetailsHM = new POMUtil().getExecutionMobileEnvironmentDetails(strMobileExecutionEnvironment, strMobileJsonPath, strMobilePlatform);

				String strMobileName = "";

				switch (strMobilePlatform) {
					case "Android":
						strMobileName = mapExecutionEnvironmentDetailsHM.get("Android Device Name");
						break;

					case "IOS":
						strMobileName = mapExecutionEnvironmentDetailsHM.get("IOS Device Name");
						break;

					default:
						Assert.fail("Mobile Device Name not mentioned either in Pre-Config Json nor in Commandline");
				}

				if (strMobilePlatform.trim().equalsIgnoreCase("Android")) {
					appiumServerConfig.startADBServer();
				}

				appiumServerConfig.startSeleniumHub(strMobilePlatform);
				appiumServerConfig.start(strMobileName);
			}
		}
		catch (Exception e){
			LoggerConfig.log.debug("Exception encountered in mobileStartSeleniumAppiumServerAutomatic : "+e.getMessage());
		}
	}

	/**
	 * #Function Name: public void deleteReportingDirectory()
	 *
	 * #Description: This method is used to delete reporting directory.
	 * 
	 * #Author: Sourabh Baya
	 */

	public void deleteReportingDirectory() {

		String strSRC_FOLDER = "./Reports/test-report";
		File fDirectory = new File(strSRC_FOLDER);

		// make sure directory exists
		if (!fDirectory.exists()) {
			fDirectory.mkdirs();
		}
		else {
			try {
				cleanDirectory(fDirectory);
			} catch (IOException e) {
				LoggerConfig.log.debug(e);
				e.printStackTrace();
				Assert.fail("Exception Occured "+e);
			}
			LoggerConfig.log.debug(
					"Existing Reporting Directory at " + fDirectory.getAbsolutePath() + " cleaned successfully.");
		}

	}

	/**
	 *  #Function Name:public void startExecutionTime()
	 *
	 * #Description: This method is used to get execution start time.
	 * 
	 * #Author Sourabh Baya
	 */
	public void startExecutionTime() {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dtNow = new Date();
		String strTestRunExecutionStartTime = formatter.format(dtNow);

		Properties prop = new Properties();
		OutputStream osOutput = null;

		try {
			osOutput = new FileOutputStream("./target/ExecutionRunTime.properties");
			prop.setProperty("testRunExecStartTime", strTestRunExecutionStartTime);
			prop.store(osOutput, null);
		} catch (Exception e) {
			LoggerConfig.log.debug("Exception encountered in startExecutionTime method : "+e.getMessage());
		} finally {
		    try{
		    if (osOutput != null){
		        osOutput.close();
            }
		    } catch (Exception e){
		        LoggerConfig.log.debug(e);
            }
        }

		LoggerConfig.log.debug("######################################################################################");
		LoggerConfig.log.debug("######################################################################################");
		LoggerConfig.log.debug("####################### Execution Started At " + strTestRunExecutionStartTime
				+ " ###########################");
		LoggerConfig.log.debug("######################################################################################");
		LoggerConfig.log.debug("######################################################################################");

	}

	/**
	 * #Function Name: void checkMobileDeviceNameInPropertyFile(String strMobilePlatform){
	 *
	 * #Description: This method is used to check if the mobile devices mentioned in  pre-config file is present in the property file.
	 * 					Also this method checks if the number of threads provided in pom.xml is equal to the mobile devices available.
	 *
	 * #Input Parameters:
	 *            @param strMobilePlatform:either Android or IOS(input from Pre-Config File Nobile Json)
	 *
	 * #Author: Sourabh Baya
	 */

	public void checkMobileDeviceNameInPropertyFile(String strMobilePlatform) {
		HashMap<String, String> mapMobileExecutionEnvironmentDetailsHM = new HashMap<String, String>();
		String strMobileJsonPath = new EnvPropVariables().getMobileJsonPath();
		String strMobileExecutionEnvironment = new EnvPropVariables().getExecutionEnvironment();
		mapMobileExecutionEnvironmentDetailsHM = new POMUtil().getExecutionMobileEnvironmentDetails(strMobileExecutionEnvironment, strMobileJsonPath, strMobilePlatform);

		String strMobileDeviceName = "";
		int nForkCount=getForkCount();
		List<String> mobileDeviceNameList=new ArrayList<>();
		String strDevicePropFileName= "\\target\\DeviceListCount.properties";

		if(strMobilePlatform.equalsIgnoreCase("Android")) {
			strMobileDeviceName = mapMobileExecutionEnvironmentDetailsHM.get("Android Device Name");
		}
		else if(strMobilePlatform.equalsIgnoreCase("IOS")) {
			strMobileDeviceName = mapMobileExecutionEnvironmentDetailsHM.get("IOS Device Name");
		}

		int nMobileDeviceNameSize_PreConfig ;

		if(strMobileDeviceName == null || strMobileDeviceName.equals("")) {
			Assert.fail("Mobile Device Name is not provided in Mobile Json File for "+strMobilePlatform);
		}

		if(strMobileDeviceName != null && strMobileDeviceName.contains(",")) {
			mobileDeviceNameList= Arrays.asList(strMobileDeviceName.split(","));
			nMobileDeviceNameSize_PreConfig = mobileDeviceNameList.size();
		}
		else {
			mobileDeviceNameList.add(strMobileDeviceName);
			nMobileDeviceNameSize_PreConfig = 1;
		}

		if(nMobileDeviceNameSize_PreConfig!=nForkCount) {
			Assert.fail("No of Mobile Devices mentioned in Mobile Json File is "+nMobileDeviceNameSize_PreConfig+" whereas fork Count is "+nForkCount+" ,it should be equal");
		}

		try{
			for (String strMobileDevice : mobileDeviceNameList) {
				if (!new MobilePropertiesFile().verifyDeviceAvailability(strMobileDevice, strDevicePropFileName)) {
					Assert.fail("Mobile Device : " + strMobileDevice + " which is passed in Mobile Json File/command line is not defined in MobileDevice Properties File..");
				} else {
					continue;
				}
			}
		} catch(Exception e) {
			LoggerConfig.log.debug("Exception encountered in checkMobileDeviceNameInPropertyFile method : "+e.getMessage());
		}
	}

	/**
	 * #Function Name: public int getForkCount()
	 *
	 * #Description: This method is used to get fork count provided in pom.xml.
	 *
	 * #Input Parameters:
	 * 			@return : returns value of forkcount mentioned in pom.xml
	 *
	 * #Author: Sourabh Baya
	 */


	public int getForkCount() {
		int nForkCount=0;
		try {
			MavenXpp3Reader reader = new MavenXpp3Reader();
			Model model = reader.read(new FileReader("./pom.xml"));
			List<Object> pluginsList = new ArrayList<Object>();
			pluginsList.addAll(model.getBuild().getPlugins());
			int nIndex = 0;
			for (int i = 0; i < pluginsList.size(); i++) {
				if (pluginsList.get(i).toString().contains("maven-failsafe-plugin")) {
					nIndex = i;
				}
			}

			String str = model.getBuild().getPlugins().get(nIndex).getConfiguration().toString();
			String strStartTag = "<forkCount>";
			String strEndTag = "</forkCount>";
			LoggerConfig.log.debug(str.indexOf(strEndTag));
			LoggerConfig.log.debug(str.indexOf(strStartTag) + strStartTag.length() - 1);
			int nStartIndex = str.indexOf(strStartTag) + strStartTag.length() - 1;
			int nEndIndex = str.indexOf(strEndTag);
			nForkCount=Integer.parseInt(str.substring(nStartIndex + 1, nEndIndex));
			LoggerConfig.log.debug("ForkCount is " + nForkCount);
		}
		catch(Exception e)
		{
			LoggerConfig.log.debug("Exception encountered in getForkCount method : "+e.getMessage());
		}
		return nForkCount;
	}

	/**
	 * #Function Name:public void cleanDirectory(File fDirectory)
	 *
	 * #Description: This method is used to clean the given directory
	 *
	 * #Input Parameters:
	 *			@param fDirectory - directory to delete
	 *
	 * #Author :Sourabh baya
	 */
	public void cleanDirectory(File fDirectory) throws IOException {
		File[] files = fDirectory.listFiles();

		if(files != null){
			for (File f : files){
					if(f.isFile()){

						FileDeleteStrategy.FORCE.delete(f);

					}else {
						FileUtils.deleteQuietly(f);
						f.delete();
					}
			}
		}
	}

}
