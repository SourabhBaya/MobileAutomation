package org.oiShoppingList.common.util;

public class CLParams 
{
	
	/**
	 * #Function Name: public String ReadingStringCLParams(String strAttributeName, String strJsonFileName, String strJSONObjectName)
	 *
	 * #Description: This method is used to read the value of CL Parameters from Pre-Config Json.
	 *
	 * #Input Parameters:
	 * 			@param strAttributeName -Name of the attribute
	 *			@param strJsonFileName - Name of the workbook file
	 * 			@return returns the string value of attribute
	 * 
	 * #Author: Sourabh Baya
	 */
	public String ReadingStringCLParams(String strAttributeName, String strJsonFileName, String strJSONObjectName)
	{
		String strAttributeValue = "";

		try {

			if (System.getProperty(strAttributeName) == null || System.getProperty(strAttributeName).equals("")) {

						strAttributeValue = new POMUtil().getJSONvaluebyName(strAttributeName, strJsonFileName, strJSONObjectName);

			} else {
				strAttributeValue = System.getProperty(strAttributeName);
			}

			if (strAttributeValue == null || strAttributeValue.equals("")) {
				LoggerConfig.log.debug("Attribute Value for " + strAttributeName + " is empty or null");
			}
		} catch (Exception e) {

            String strErrorMsg = strAttributeName +" field is not present in" +strJsonFileName +"Json"+e.getMessage();
            LoggerConfig.log.error(strErrorMsg);

		}

		return strAttributeValue;
	}
	
	
	/**
	 * #Function Name: public Integer ReadingIntegerCLParamsPreConfig(String strAttributeName, String strJsonFileName)
	 *
	 * #Description: This method is used to read the parameters of integer type passed through command line or pre-config.If bith are present command
	 * line takes priority
	 *
	 * #Input Parameters:
	 * 			@param strAttributeName - Name of the attribute.
	 * 			@param strJsonFileName - Name of the workbook file.
	 * 			@return return the attribute value in integer.
	 * 
	 * #Author: Sourabh Baya
	 */
	public Integer ReadingIntegerCLParamsPreConfig(String strAttributeName, String strJsonFileName)
	{
		int nAttributeValue = 0;
		LoggerConfig.log.debug("### Reading Integer CL Params Pre Config ###");
		try
		{
			if(System.getProperty(strAttributeName)==null || System.getProperty(strAttributeName).equals(""))
			{
				nAttributeValue = Integer.parseInt(new POMUtil().getJSONvaluebyName(strAttributeName, strJsonFileName,"PreConfig"));
			}
			else
			{
				String strAttributeValue = System.getProperty(strAttributeName);	
				nAttributeValue = Integer.parseInt(strAttributeValue);
			}
			
			if(nAttributeValue==0)
			{
				LoggerConfig.log.debug("Attribute Value for "+ strAttributeName +" is set as 0");
			}
			
		}
		catch(Exception e)
		{
			LoggerConfig.log.debug("Attribute Value for "+ strAttributeName +" is not set either at Json or at command line.",e);
			
			return nAttributeValue;
		}
		
		return nAttributeValue;
	}

	/**
	 * #Function Name: public String ReadingCLParamsWebMob(String strJsonFileName, String strHeader, String strAttributeName)
	 *
	 * #Description: This method is used to read CL Parameters present in Json of mobile.
	 *
	 * #Input Parameters:
	 * 			@param  strJsonFileName -  Name of the workbook file.
	 * 			@param  strHeader -Pass name of the header.
	 * 		    @param strAttributeName - Pass the name of attribute.
	 * 			@return return the attribute value in string.
	 *
	 * #Author: Sourabh Baya
	 */

	public String ReadingCLParamsWebMob(String strJsonFileName, String strHeader, String strAttributeName)
	{
		String strParamValue = "";
		LoggerConfig.log.debug("### Reading CL Params ###");
		 if(strJsonFileName.equalsIgnoreCase("Mobile"))
		{
			strParamValue = ReadingCLParamsMobile(strHeader, strAttributeName);
		}
		
		return strParamValue;
	}

	/**
	 * #Function Name: public String ReadingCLParamsMobile(String strHeader, String strAttributeName)
	 *
	 * #Description: This method is used to read CL parameters for Mobile device
	 *
	 * #Input Parameters:
	 * 			@param  strHeader -Pass name of the header.
	 * 		    @param strAttributeName - Pass the name of attribute.
	 * 			@return value return in string
	 *
	 * #Author: Sourabh Baya
	 */
	
	public String ReadingCLParamsMobile(String strHeader, String strAttributeName)
	{
		String strParamValue = "";
		
		if(strHeader.equalsIgnoreCase("Physical Real Device"))
		{
			//The variable values are Command Line parameter names			
			String strMobAndroidDeviceName = "MobPhysicalDevice_Android_DeviceName";
			String strMobIOSDeviceName = "MobPhysicalDevice_IOS_DeviceName";
			
			
			if(strAttributeName.equalsIgnoreCase("Android Device Name"))
			{
				strParamValue = System.getProperty(strMobAndroidDeviceName);
			}
			else if(strAttributeName.equalsIgnoreCase("IOS Device Name"))
			{
				strParamValue = System.getProperty(strMobIOSDeviceName);
			}		
			
		}		
						
		return strParamValue;
		
	}	
	
	
}
