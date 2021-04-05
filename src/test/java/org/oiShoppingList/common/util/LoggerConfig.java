package org.oiShoppingList.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.oiShoppingList.common.misc.EnvPropVariables;

public class LoggerConfig {
	 
	//Configuring log4j.properties file
	 static 
	{
	   SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss.SSS");
	   System.setProperty("currenttime", dateFormat.format(new Date()));
	   System.setProperty("logPath", (new CLParams().ReadingStringCLParams("LogFilePAth", new EnvPropVariables().getPreConfigPath(),"PreConfig")));
	   String propertyFilePath= (new CLParams().ReadingStringCLParams("Log4j_Properties_Path", new EnvPropVariables().getPreConfigPath(),"PreConfig"));
	   PropertyConfigurator.configure(propertyFilePath);
    }
	 
	 //Initialising the logger 
	 public static Logger log=Logger.getLogger(LoggerConfig.class.getName());
	 
}
