package org.oiShoppingList.mobile.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.oiShoppingList.common.misc.EnvPropVariables;
import org.oiShoppingList.common.util.CLParams;
import org.oiShoppingList.common.util.LoggerConfig;


public class AppiumServerConfig
{
	/**
	 * #Function Name: public void start(String strMobileName)
	 *
	 * #Description: This method is the initial method to start Appium server configurations
	 *
	 * #Input Parameters:
	 *			#param strMobileName - Name of the Mobile in which tests are going to run
	 *
	 * #Author: Sourabh Baya
	 */
	public void start(String strMobileName)
	{
		List<String> mobileNameList= new ArrayList<>();
		if(strMobileName.contains(",")) {
			mobileNameList = Arrays.asList(strMobileName.split(","));
		} else {
			mobileNameList.add(strMobileName);
		}
		for(String strMbleName:mobileNameList) {
			List<String> physicalDeviceList = readDevicePropertiesFile(strMbleName);
			LoggerConfig.log.debug("########################");
			LoggerConfig.log.debug("Mobile Name : " + strMbleName);
			LoggerConfig.log.debug(physicalDeviceList.toString());
			readAppiumServerJson(physicalDeviceList, true);
		}
	}
	
	
	/**
	 * #Function Name: public List<String> readDevicePropertiesFile(String strMobileName)
	 *
	 * #Description: This method is used to read the property file for the mobile device
	 *
	 * #Input Parameters:
	 *			 @param strMobileName - Name of the Mobile in which tests are going to run
	 * 			 @return - returns the list of matching devices
	 *
	 * #Author: Sourabh Baya
	 */
	public List<String> readDevicePropertiesFile(String strMobileName)
	{
		List<String> matchingDeviceList = new ArrayList<String>();
		
		try
		{
			Properties props = new Properties();
			File temporaryPhysicalDeviceFile = new File(System.getProperty("user.dir") + "\\target\\temporaryDeviceList.properties");
			
			FileInputStream in = new FileInputStream(temporaryPhysicalDeviceFile);
			props.load(in);
			in.close();

			List<String> mobileNameList = new ArrayList<String>();
			if(strMobileName.contains(",")){
				mobileNameList = Arrays.asList(strMobileName.split(","));
			}
			else {
				mobileNameList.add(strMobileName);
			}
			
			Set<String> keys = props.stringPropertyNames();
			for (String strKey : keys) {
				for(String strMobName : mobileNameList) {
					if (strKey.equalsIgnoreCase(strMobName)) {
						matchingDeviceList.add(strKey);
					}
				}
			}
		}
		catch(Exception e)
		{
			LoggerConfig.log.debug(e.getMessage());
		}
		
		return matchingDeviceList;
	}
	
	
	/**
	 * #Function Name: public List<String> readAppiumServerJson(List<String> physicalDeviceList, Boolean startServer)
	 *
	 * #Description: This method is used to Check whether device name mentioned in Properties file is present in Appium Server json file
	 *
	 * #Input Parameters:
	 * 			@param physicalDeviceList - List of Devices
	 * 			@param bStartServer - True or False (To start Appium server)
	 * 			@return - returns the port list for appium server
	 * 
	 * #Author: Sourabh Baya
	 */
	
	public List<String> readAppiumServerJson(List<String> physicalDeviceList, Boolean bStartServer)
	{
		String strAppiumServerConfigFolderPath = null;
		//Checking whether device name mentioned in Properties file is present in Appium Server json file
		try {
			strAppiumServerConfigFolderPath =  System.getProperty("user.dir") + new CLParams().ReadingStringCLParams("Appium_Config_Json_File", new EnvPropVariables().getPreConfigPath(),"PreConfig");
			File fAppiumServer = new File(strAppiumServerConfigFolderPath);
			String strAppiumServer = FileUtils.readFileToString(fAppiumServer, "utf-8");

			JSONObject jsonObjAppiumServer = new JSONObject(strAppiumServer);
			List<String> lAppiumServerList = new ArrayList<String>(jsonObjAppiumServer.keySet());
		    
		    deviceMatchCheck(physicalDeviceList, lAppiumServerList);
		}
		catch(Exception e) {
			LoggerConfig.log.debug(e.getMessage());
		}

		List<String> appiumServerPortList = new ArrayList<String>();

		String strPort;
		String strBPPort;
		String strAppiumServerHost;
		String strJsonFilePath;
		
		try {
			for(String strDeviceName:physicalDeviceList) {
				strPort = "";
				strBPPort = "";
				strAppiumServerHost = "";
				strJsonFilePath = "";

				File fFile = new File(strAppiumServerConfigFolderPath);
			    String strContent = FileUtils.readFileToString(fFile, "utf-8");
			    
			    JSONObject jsonObject = new JSONObject(strContent);		    
			    List<String> lList1 = new ArrayList<String>(jsonObject.keySet());
			    
			    JSONObject jsonObject2;
			    
	            for (String strKey : lList1) {
	            	if(strKey.equals(strDeviceName)) {
	            		jsonObject2=jsonObject.getJSONObject(strKey);
	            		Iterator<String> keysItr = jsonObject2.keys();
	            		
	            		while(keysItr.hasNext()) {
					        String strkey = keysItr.next();

					        switch (strkey) {
								case "Port":
									strPort = jsonObject2.get(strkey).toString();
									break;

								case "BP Port":
									strBPPort = jsonObject2.get(strkey).toString();
									break;

								case "Appium Server Host":
									strAppiumServerHost = jsonObject2.get(strkey).toString();
									break;

								case "Json File Path":
									strJsonFilePath = jsonObject2.get(strkey).toString();
									break;
							}
					    }
	            	}
	            }
	            appiumServerPortList.add(strPort);
	            if(bStartServer) {
	            	startAppiumServer(strPort,strBPPort,strAppiumServerHost,strJsonFilePath);	           
	            }
			}
		}
		catch(Exception e) {
			LoggerConfig.log.error(e.getMessage());
		}
		return appiumServerPortList;
		
	}
	
	/**
	 * #Function Name: public void startAppiumServer(String strPort, String strBPPort, String strAppiumServerHost, String strJsonFilePath)
	 *
	 * #Description: This method is used to start Appium Server
	 *
	 * #Input Parameters:
	 * 			@param strPort - Port in which server has to run
	 * 			@param strAppiumServerHost - Host name detail
	 * 			@param strJsonFilePath- Path of JSON file
	 * 
	 * #Author: Sourabh Baya
	 */
	
	public void startAppiumServer(String strPort, String strBPPort, String strAppiumServerHost, String strJsonFilePath)
	{
		String strMobileJsonPath = new EnvPropVariables().getMobileJsonPath();

		try {
			String strMobilePlatform = new CLParams().ReadingStringCLParams("Platform", strMobileJsonPath,"Mobile");

			if (strMobilePlatform.trim().equalsIgnoreCase("Android")) {
				boolean bServerRunning = killExistingRunningServerWin("Appium", strPort);

				if (bServerRunning) {
					Assert.fail("Appium Server is already running at port : " + strPort + " hence exiting...");
				} else {
					try {
						String strFile_Path = System.getProperty("user.dir") + strJsonFilePath;
						String strCommand = "cmd /C start cmd.exe /C \"cd " + '"' + System.getProperty("user.dir") + '"' + " && appium --nodeconfig " + '"' + strFile_Path + '"' + " -a " + '"' + strAppiumServerHost + '"' + " -p " + '"' + strPort + '"' + " -bp " + '"' + strBPPort + '"' + "--session-override -dc \"{\"\"noReset\"\": \"\"false\"\"}\"\"";

						LoggerConfig.log.debug(strCommand);
						Runtime.getRuntime().exec(strCommand);

						int nWaitTime = 0;
						while (nWaitTime <= 100) {
							if (checkIfServerIsRunnning("Appium", strPort)) {
								break;
							} else {
								nWaitTime++;
								TimeUnit.SECONDS.sleep(1);
							}
						}


					} catch (IOException e) {
						LoggerConfig.log.debug(e.getMessage());
						e.printStackTrace();
					} catch (Exception e) {
						LoggerConfig.log.debug(e.getMessage());
					}
				}
			} else if (strMobilePlatform.trim().equalsIgnoreCase("IOS")) {

			}
		}catch (Exception e){
			LoggerConfig.log.debug(e.getMessage());
		}
	}
	
	
	/**
	 * #Function Name: static boolean checkIfServerIsRunnning(String strPort)
	 *
	 * #Description: This method is used to check if the server is running or not
	 * 
	 * #Input Parameters:
	 * 			@param strPort - Port in which server has to run
	 *          @return true - if server is running, else false
	 *
	 * #Author: Sourabh Baya
	 */
	public boolean checkIfServerIsRunnning(String strServerName, String strPort)
	{
		boolean bServerRunning = false;

		ServerSocket serverSocket;
		try {
			int running = Integer.parseInt(strPort);
			serverSocket = new ServerSocket(running);
			serverSocket.close();
			LoggerConfig.log.debug("Launching "+strServerName+" Server...");
		}
		catch (Exception e) {

			bServerRunning = true;
			LoggerConfig.log.debug("The "+strServerName+" server has started..."+e.getMessage());

		}

		return bServerRunning;
	}


	/**
	 * #Function Name: public void deviceMatchCheck(List<String> propertiesFileList, List<String> AppiumServerJsonFileList)
	 *
	 * #Description: This method is used to check if the device in property file is present in Appium server JSON file
	 *
	 * #Input Parameters:
	 *         @param propertiesFileList - Property file list
	 *         @param AppiumServerJsonFileList - List of JSON File
	 * 
	 * #Author: Sourabh Baya
	 */
	public void deviceMatchCheck(List<String> propertiesFileList, List<String> AppiumServerJsonFileList) {
		List<String> missingDeviceList = new ArrayList<String>();
		
		for (String strPropFile:propertiesFileList) {
			boolean bFound = false;
			
			for (String strAppiumServerFile:AppiumServerJsonFileList) {
				if(strPropFile.equals(strAppiumServerFile)) {
					bFound = true;
				}					
			}
			if(!bFound) {
				missingDeviceList.add(strPropFile);
			}
		}
		if(!missingDeviceList.isEmpty()) {
			Assert.fail("Following Devices specified in MobilePhysicalDevice Properties file are not present in Appium Server Json file : "+missingDeviceList.toString());
		}
	}
	
	/**
	 * #Function Name: public void StopAppiumAtPort(String strPort)
	 *
	 * #Description: This method is used to stop the appium server running  in port
	 *
	 * #Input Parameters:
	 * 			@param strPort - Port Number
	 * 
	 * #Author: Sourabh Baya
	 */
    public void StopAppiumAtPort(String strPort) {
    	String strPID = "";
    	
		try {
			 String strDir = System.getProperty("user.dir");
			 String[] strArrCommandAndParameters = {"cmd.exe","/C","cd " + strDir," & dir & netstat -ano | findstr "+strPort};
			
			 ProcessBuilder builder = new ProcessBuilder();
			 builder.redirectErrorStream(true); 
			 builder.command(strArrCommandAndParameters);
	
			 Process process = builder.start();
	
			 InputStream is = process.getInputStream();
			 BufferedReader stdInput = new BufferedReader(new InputStreamReader(is));
			 
			// Read the output from the command
			LoggerConfig.log.debug("Here is the standard output of the command:\n");
			String str;
			while ((str = stdInput.readLine()) != null) {
				if(str.contains("LISTENING")) {
					String strArrPortNumber[] = str.split(" ");
					LoggerConfig.log.debug("Port number is : "+strArrPortNumber[strArrPortNumber.length-1].trim());
					strPID = strArrPortNumber[strArrPortNumber.length-1].trim();
					LoggerConfig.log.debug(strPID);
				}
			}
			Runtime.getRuntime().exec("cmd /C cmd.exe /C \"cd "+strDir+" && Taskkill /PID "+strPID+" /T /F");
	    } 
	    catch (Exception e) {
	        LoggerConfig.log.debug(e.getMessage());
	    } 
               
    }
    
    /**
	 * #Function Name: public void startSeleniumHub()
	 *
	 * #Description: This method is used to start the selenium hub node
	 * 
	 * #Author: Sourabh Baya
	 */
    public void startSeleniumHub(String strMobilePlatform) {
		if(strMobilePlatform.trim().equalsIgnoreCase("Android")) {
			killExistingRunningServerWin("SeleniumHub", "4444");
		}
		else if(strMobilePlatform.trim().equalsIgnoreCase("IOS")) {

		}
    	try {
	    	String strSeleniumHub = new CLParams().ReadingStringCLParams("Selenium_Hub_Path",new EnvPropVariables().getPreConfigPath(),"PreConfig");
	    	
	    	int nFirstpart = strSeleniumHub.lastIndexOf('/');  
	    	String strSeleniumHub_Path = System.getProperty("user.dir")+strSeleniumHub.substring(0, nFirstpart);
	    	String strHubJarName = strSeleniumHub.substring(nFirstpart + 1);
	    	System.out.println("#####################################");

			System.out.println("#####################################");

            String strCommand = "";
            if(strMobilePlatform.trim().equalsIgnoreCase("Android")) {
                strCommand = "cmd /C start cmd.exe /C \"java -jar " + strSeleniumHub_Path + "\\" + strHubJarName + " -role hub";
            }
            else if(strMobilePlatform.trim().equalsIgnoreCase("IOS")) {

            }
	    	
			LoggerConfig.log.debug(strCommand);
			Runtime.getRuntime().exec(strCommand);

            if(strMobilePlatform.trim().equalsIgnoreCase("Android")) {
                int nWaitTime = 0;
                while (nWaitTime <= 100) {
                    if (checkIfServerIsRunnning("SeleniumHub", "4444")) {
                        break;
                    } else {
                        nWaitTime++;
                        TimeUnit.SECONDS.sleep(1);
                    }
                }
            }
    	}
    	catch(Exception e) {
    		Assert.fail("Error encountered while starting Selenium Hub : "+e.getMessage());
    	}
    }
    
    
    /**
	 * #Function Name: public void StopSeleniumHubAtPort(String strPort)
	 *
   	 * #Description: This method is used to stop the selenium hub node at specific port
   	 *
	 * #Input Parameters:
   	 *         @param strPort - Port name
   	 * 
   	 * #Author: Sourabh Baya
   	 */
    
    public void StopSeleniumHubAtPort(String strPort) {
    	String strPID = "";
    	
		try {
			 String strDir = System.getProperty("user.dir");
			 String[] strArrCommandAndParameters = {"cmd.exe","/C","cd " + strDir," & dir & netstat -ano | findstr "+strPort};
			
			 ProcessBuilder builder = new ProcessBuilder();
			 builder.redirectErrorStream(true); 
			 builder.command(strArrCommandAndParameters);
	
			 Process process = builder.start();
	
			 InputStream is = process.getInputStream();
			 BufferedReader stdInput = new BufferedReader(new InputStreamReader(is));
			 
			// Read the output from the command
			LoggerConfig.log.debug("Here is the standard output of the command:\n");
			String str;
			while ((str = stdInput.readLine()) != null) {
				if(str.contains("LISTENING")) {
					String strArrPortNumber[] = str.split(" ");
					LoggerConfig.log.debug("Port number is : "+strArrPortNumber[strArrPortNumber.length-1].trim());
					strPID = strArrPortNumber[strArrPortNumber.length-1].trim();
					LoggerConfig.log.debug(strPID);
				}
			}
			Runtime.getRuntime().exec("cmd /C cmd.exe /C \"cd "+strDir+" && Taskkill /PID "+strPID+" /T /F");
	    } 
	    catch (Exception e) {
	        LoggerConfig.log.debug(e.getMessage());
	    } 
               
    }


    /**
	 * #Function Name: public boolean killExistingRunningServerWin(String strServer, String strPort)
	 *
	 * #Description: This method is used to kill the running server in Windows
	 *
	 * #Input Parameters:
	 *         @param strPort - Port name
	 *		   @param strServer- server name
	 * 		   @return true -if server is still running else false
	 *
	 * #Author: Sourabh Baya
	 */

	public boolean killExistingRunningServerWin(String strServer, String strPort) {
		boolean bServerRunning = false;

		ServerSocket serverSocket;
		ServerSocket serverSocket1 = null;
		try {
			int nRunning = Integer.parseInt(strPort);
			serverSocket = new ServerSocket(nRunning);
			serverSocket.close();
			LoggerConfig.log.debug("Checking whether the "+strServer+" server is already running...");
			LoggerConfig.log.debug("No "+strServer+" servers are running...Hence starting a new "+strServer+" Server...");
		}
		catch (IOException e) {
			//If control comes here, then it means that the port is in use
			bServerRunning = true;
			LoggerConfig.log.debug("The "+strServer+" server is already running hence closing and starting again..."+e.getMessage());

			if(strServer.equalsIgnoreCase("Appium")) {
				StopAppiumAtPort(strPort);
			}
			else if(strServer.equalsIgnoreCase("SeleniumHub")) {
				StopSeleniumHubAtPort("4444");
			}

			try {
				TimeUnit.SECONDS.sleep(6);
			}
			catch(Exception e2)
			{
				LoggerConfig.log.debug(e2.getMessage());
			}
			int nRunning1 = Integer.parseInt(strPort);
			try {
				serverSocket1 = new ServerSocket(nRunning1);
			} catch (IOException e1) {
				LoggerConfig.log.debug("Though tried to kill the existing running "+strServer+" server but it cannot be closed..."+e1.getMessage());
			}
			try {
				if(serverSocket1 != null) {
					serverSocket1.close();
				}
				bServerRunning = false;
			}
			catch (IOException e1) {
				LoggerConfig.log.debug("Though tried to kill the existing running "+strServer+" server but it cannot be closed..."+e1.getMessage());
			}
		}
		return bServerRunning;
	}


	/**
	 * #Function Name: public void startADBServer()
	 *
	 * #Description: This method is used to start ADB Server
	 *
	 * #Input Parameters:
	 *
	 * #Author: Sourabh Baya
	 */

	public void startADBServer() {
		try {
			System.out.println("Starting ADB server if not already started...");
			String strCommand = "cmd /C start cmd.exe /C \"adb start-server";
			LoggerConfig.log.debug(strCommand);
			Runtime.getRuntime().exec(strCommand);
			Thread.sleep(4000);
		}
		catch(Exception e) {
			Assert.fail("Error encountered while starting Selenium Hub : "+e.getMessage());
		}
	}
}
