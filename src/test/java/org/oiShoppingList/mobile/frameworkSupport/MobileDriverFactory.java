package org.oiShoppingList.mobile.frameworkSupport;

import com.google.gson.JsonArray;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.Assert;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.oiShoppingList.common.misc.EnvPropVariables;
import org.oiShoppingList.common.misc.StaticData;
import org.oiShoppingList.common.util.CLParams;
import org.oiShoppingList.common.util.LoggerConfig;
import org.oiShoppingList.common.util.POMUtil;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MobileDriverFactory {

    HashMap<String, String> mapMobileExecutionEnvironmentDetailsHM = new HashMap<String, String>();

    public MobileDriverFactory() {
        String strMobileJsonPath = new EnvPropVariables().getMobileJsonPath();
        String strMobileExecutionEnvironment = new EnvPropVariables().getExecutionEnvironment();
        String strMobilePlatform = new EnvPropVariables().getStrMobilePlatform();
        mapMobileExecutionEnvironmentDetailsHM = new POMUtil().getExecutionMobileEnvironmentDetails(strMobileExecutionEnvironment, strMobileJsonPath, strMobilePlatform);
    }

    public void createInstanceForPhysicalDevice(String strUdid, String strMobilePhyDevSystemPort) {

        LoggerConfig.log.debug("#### Creating instance of mobile and setting capabilities for the mobile instance created ####");

        try {

            if (("IOS").equalsIgnoreCase(new EnvPropVariables().getStrMobilePlatform())) {
                DesiredCapabilities cap = new DesiredCapabilities();
                switch (new EnvPropVariables().getStrMobileTestEnvironment()) {
                    case "App":
                        cap.setCapability("deviceName", StaticData.usingDevice.get(0));
                        cap.setCapability("systemPort", strMobilePhyDevSystemPort);
                        cap.setCapability("platformVersion", "");
                        cap.setCapability("bundleId",
                                mapMobileExecutionEnvironmentDetailsHM.get("IOS Bundle ID"));
                        cap.setCapability("udid", strUdid);
                        if(!mapMobileExecutionEnvironmentDetailsHM.get("APP File Name").equals("")) {
                            cap.setCapability("app",
                                    mapMobileExecutionEnvironmentDetailsHM.get("APP File Name"));
                        }
                        cap.setCapability("automationName","XCUITEST");
                        cap.setCapability("platformName","IOS");
                        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "10000");
                        break;

                    case "Web Browser":
                        break;
                }

                try {
                    String strURL = mapMobileExecutionEnvironmentDetailsHM.get("IOS Local HUB URL");
                    StaticData.driver = new IOSDriver(new URL(strURL), cap);
                } catch (MalformedURLException e) {
                    String strErrorMsg = "Exception encountered while opening the IOS APP " + e.getMessage();
                    LoggerConfig.log.debug(strErrorMsg);
                    Assert.fail(strErrorMsg);
                }


            } else if (("Android").equalsIgnoreCase(new EnvPropVariables().getStrMobilePlatform())) {
                DesiredCapabilities cap = new DesiredCapabilities();
                switch (new EnvPropVariables().getStrMobileTestEnvironment()) {
                    case "APP":
                        cap.setCapability("deviceName", StaticData.usingDevice.get(0));
                        cap.setCapability("appPackage",
                                mapMobileExecutionEnvironmentDetailsHM.get("Android APP PACKAGE Name"));

                        int noOfApps =  getNoOfApplication();
                        if(noOfApps != 0)
                        {
                            if(noOfApps>1){

                                JsonArray jsonArray = new JsonArray();

                                for(int i =1; i<=noOfApps ; i++) {

                                    String apkPath = mapMobileExecutionEnvironmentDetailsHM.get("APP File Name"+i);
                                    if(!apkPath.equals("")) {

                                        jsonArray.add(apkPath);
                                    }
                                }
                                if(jsonArray != null) {
                                    System.out.println(jsonArray.toString());
                                    cap.setCapability("otherApps", jsonArray.toString());
                                }
                            }else
                            {

                                String apkPath = mapMobileExecutionEnvironmentDetailsHM.get("APP File Name");
                                if(!apkPath.equals("")) {
                                    File app = new File(apkPath);
                                    cap.setCapability("app", app.getAbsolutePath());

                                    cap.setCapability("fullReset", true);
                                }

                            }
                        }
                        cap.setCapability("udid", strUdid);
                        cap.setCapability("systemPort", strMobilePhyDevSystemPort);
                        cap.setCapability("appActivity",
                                mapMobileExecutionEnvironmentDetailsHM.get("Android APP ACTIVITY Name"));
                        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "10000");
                        cap.setCapability("instrumentApp", true);
                        cap.setCapability("noReset",false);

                        cap.setCapability("platformName", "Android");
                        cap.setCapability("automationName", "UiAutomator2");
                        break;


                    case "Web Browser":
                        break;
                }
                try {
                    String strURL = mapMobileExecutionEnvironmentDetailsHM.get("Android Local HUB URL");
                    StaticData.driver = new AndroidDriver(new URL(strURL), cap);
                } catch (MalformedURLException e) {
                    String strErrorMsg = "Exception encountered while opening the Android APP " + e.getMessage();
                    LoggerConfig.log.debug(strErrorMsg);
                    Assert.fail(strErrorMsg);
                }
            }

        } catch (Exception e) {
            String strErrorMsg = "Exception encountered while opening the APP " + e.getMessage();
            LoggerConfig.log.debug(strErrorMsg);
            Assert.fail(strErrorMsg);
        }

    }
    public  int getNoOfApplication() {

        int noOfApps= 0;

        for (Map.Entry<String,String> entry : mapMobileExecutionEnvironmentDetailsHM.entrySet()) {
            if(entry.getKey().contains("APP File Name")){
                noOfApps++;
            }
        }
        return noOfApps;
    }


}