package org.oiShoppingList.mobile.util;

import org.oiShoppingList.common.util.FileLocker;
import org.oiShoppingList.common.util.LoggerConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class MobilePropertiesFile 
{
	private File temporaryFile;
	private Properties props = new Properties();

	public MobilePropertiesFile() {
		temporaryFile = new File(System.getProperty("user.dir") + "\\target\\temporaryDeviceList.properties");
		props = null;
		props = loadPropertyFiles();
	}

	/**
	 * #Function Name: public Boolean verifyDeviceAvailability(String strMobileDeviceName)
	 *
	 * #Description: This method is used to check if the mobile device is available or not
	 *
	 * #Input Parameters:
	 *		   @param strMobileDeviceName - Name of the mobile device
	 * 		   @param strFileName- Path of Property file
	 * 		   @returns true if device is present in property file,else false
	 *
	 * #Author: Sourabh Baya
	 */
	public Boolean verifyDeviceAvailability(String strMobileDeviceName,String strFileName)
	{
		boolean bFoundMatchingDevice = false;
		
		try 
		{

			Properties props = new Properties();
			
			File file1 = new File(System.getProperty("user.dir") + strFileName);

			FileInputStream in = new FileInputStream(file1);
			props.load(in);
			in.close();

			Set<String> keys = props.stringPropertyNames();
		    for (String strKey : keys) 
		    {
		     // String strExtractedDeviceName = strKey.replaceAll("[^A-Za-z]","");
		      if(strKey.equalsIgnoreCase(strMobileDeviceName))
		      {
				  LoggerConfig.log.debug("***************Value Matched***************");
		    	  bFoundMatchingDevice = true;
		    	  break;
		      }
		      
		    }
		    
		} 
		catch (FileNotFoundException e) 
		{
			LoggerConfig.log.debug(e);
			e.printStackTrace();
		} 
		catch (Exception e)
		{
			LoggerConfig.log.debug(e);
			e.printStackTrace();
		}

		
		return bFoundMatchingDevice;
	}

	/**
	 * #Function Name: public Properties loadPropertyFiles()
	 *
	 * #Description: This method is used to load the property file for the mobile device
	 *
	 * #Input Parameters:
	 *         @return :returns the property file which is loaded
	 *
	 * #Author: Sourabh Baya
	 */
	public Properties loadPropertyFiles()
	{
		Properties properties = new Properties();

		try 
		{
			properties.clear();

			FileInputStream in = new FileInputStream(temporaryFile);
			properties.load(in);
			in.close();
		} 
		catch (FileNotFoundException e) 
		{
			LoggerConfig.log.debug(e);
			e.printStackTrace();
		} 
		catch (Exception e)
		{
			LoggerConfig.log.debug(e);
			e.printStackTrace();
		}
		return properties;
	}



	/**
	 * #Function Name: public List<String> getData(String strMobileName)
	 *
	 * #Description: This method is used to fetch data for the mobile device
	 *
	 * #Input Parameters:
	 * 		   @param strMobileName - name of the mobile device
	 *         @return :returns the list of keys and values in the property file
	 *
	 * #Author: Sourabh Baya
	 */
	
	public List<String> getMobileData(String strMobileName)
	{
		List<String> usingDeviceList = new ArrayList<String>();
		String strAvailableDevice ;
		String strAvailableDeviceValue ;
		
		boolean bFoundMatchingDevice = false;
	
		Set<String> keys = props.stringPropertyNames();
	    for (String strKey : keys) 
	    {
	      if(strKey.equalsIgnoreCase(strMobileName))
	      {
	    	  strAvailableDevice = strKey;
	    	  strAvailableDeviceValue = props.getProperty(strKey);
	    	  
	    	  LoggerConfig.log.debug("###################################################");
	    	  LoggerConfig.log.debug("###################################################");	    	  
	  	      LoggerConfig.log.debug("Available Device is : "+strAvailableDevice);
		      LoggerConfig.log.debug("Available Device Value is : "+strAvailableDeviceValue);
	    	  LoggerConfig.log.debug("###################################################");
	    	  LoggerConfig.log.debug("###################################################");			      
		      
			  usingDeviceList.add(strAvailableDevice);
			  usingDeviceList.add(strAvailableDeviceValue);
		      
		      bFoundMatchingDevice = true;
		    
	    	  break;
	      }
	          
	    }
	    
	    if(!bFoundMatchingDevice)
	    {
	    	usingDeviceList = waitingForMobileDevice(strMobileName);
	    }
		    
		
		return usingDeviceList;	
	}

	/**
	 * #Function Name: List<String> waitingForMobileDevice(String strMobileName)
	 *
	 * #Description: This method is used to enable wait for the mobile device for stability
	 *
	 * #Input Parameters:
	 * 		   @param strMobileName - name of the mobile device
	 *         @return :returns the list of keys and values in the property file
	 *
	 * #Author: Sourabh Baya
	 */
	
	public List<String> waitingForMobileDevice(String strMobileName) {

		String strLockerFile = "./target/LockFile_MobilePhysicalDevice.txt";
		FileLocker fl = new FileLocker();
				
		List<String> usingDeviceList = new ArrayList<String>();
		
		try {
			
			fl.releaseLockAndChannels();
			
			String strAvailableDevice = "";
			String strAvailableDeviceValue = "";
			
			boolean bFoundMatchingDevice = false;
			
			int nWaitTime = 0;
			while(!bFoundMatchingDevice) {

				int nWaitTime2 = 0;
				while(nWaitTime2<30) {

					if(!fl.isLockedByAnotherTestCase(strLockerFile)) {

							props = new MobilePropertiesFile().loadPropertyFiles();
							Set<String> keys = props.stringPropertyNames();
							
							LoggerConfig.log.debug("##########################################");
							LoggerConfig.log.debug("##########################################");
							LoggerConfig.log.debug("Available Devices are : "+keys);
							LoggerConfig.log.debug("##########################################");
							LoggerConfig.log.debug("##########################################");
							
						    for (String strKey : keys) {
							      if(strKey.equalsIgnoreCase(strMobileName)) {
							    	  strAvailableDevice = strKey;
							    	  strAvailableDeviceValue = props.getProperty(strKey);
							    	  
							  	      LoggerConfig.log.debug("Available Device is : "+strAvailableDevice);
								      LoggerConfig.log.debug("Available Device Value is : "+strAvailableDeviceValue);
								      
								      bFoundMatchingDevice = true;
								    
							    	  break;
							      }
						      
						    }
						    
						    break;
					    
					}
					else {
						LoggerConfig.log.debug("Waiting for other thread to release Properties file to read available device..");
						TimeUnit.SECONDS.sleep(5);
						nWaitTime2=nWaitTime2+1;
					}
				}
					
				    
				if(bFoundMatchingDevice)
				{
					break;
				}

				LoggerConfig.log.debug("Waiting for the availability of the "+strMobileName+" Device..");

				try
				{
					fl.releaseLockAndChannels();

					TimeUnit.SECONDS.sleep(5);
				}
				catch(Exception e)
				{
					LoggerConfig.log.debug(e);
				}

				nWaitTime++;

			}

		    usingDeviceList.add(strAvailableDevice);
		    usingDeviceList.add(strAvailableDeviceValue);
		    
		}
		catch(Exception e)
		{
			LoggerConfig.log.debug(e);
		}
		
		return usingDeviceList;	
	}

	/**
	 * #Function Name: public void deletePropertyFileData(String strDeviceName)
	 *
	 * #Description:  This method is used to delete the property file datas already loaded
	 *
	 * #Input Parameters:
	 * 		   @param strDeviceName - name of the mobile device
	 *
	 * #Author: Sourabh Baya
	 */

	public void deletePropertyFileData(String strDeviceName) throws IOException
	{
		try
		{
			LoggerConfig.log.debug("###############################################");
			LoggerConfig.log.debug("Deleting properties file data : "+strDeviceName);
			LoggerConfig.log.debug("###############################################");
			
			//StaticData.properties.remove(strDeviceName);
			props.remove(strDeviceName);
			FileOutputStream out = new FileOutputStream(temporaryFile);
			//StaticData.properties.store(out, null);
			props.store(out, null);
			out.close();
			
		}
		catch(Exception e)
		{
			LoggerConfig.log.debug("Error encountered while deleting data from property file "+e);
		}
	}


	/**
	 * #Function Name: public void writePropertyFileData(List<String> mobileDevice)
	 *
	 * #Description:  This method is used write the available device and its respective ID to a temporary property file
	 *
	 * #Input Parameters:
	 * 		   @param mobileDevice - list of  available mobile devices
	 *
	 * #Author: Sourabh Baya
	 */

	public void writePropertyFileData(List<String> mobileDevice)
	{
		try
		{
			LoggerConfig.log.debug("###############################################");
			LoggerConfig.log.debug("Writing back the device details into properties file : "+mobileDevice.get(0));
			LoggerConfig.log.debug(mobileDevice);
			LoggerConfig.log.debug("###############################################");
			
			//StaticData.properties.setProperty(mobileDevice.get(0), mobileDevice.get(1));
			props.setProperty(mobileDevice.get(0), mobileDevice.get(1));
			FileOutputStream out = new FileOutputStream(temporaryFile);
			//StaticData.properties.store(out, null);
			props.store(out, null);
			out.close();
		}
		catch(Exception e)
		{
			LoggerConfig.log.debug(e);
		}
	}
}
