package org.amazon.mobile.scripts;

import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.MobileElement;
import org.amazon.common.misc.FailureManagement;
import org.amazon.common.misc.StaticData;
import org.amazon.common.scripts.GeneralScript;
import org.amazon.common.toolbox.Display;
import org.amazon.common.util.LoggerConfig;
import org.amazon.common.util.TakeScreenshot;
import java.util.concurrent.TimeUnit;

public class GeneralMobileAndroidScript {

    public static void scrollAndClick(String strButton, String strPage) {

        try {

            MobileElement element = (MobileElement) ((FindsByAndroidUIAutomator) StaticData.driver)
                    .findElementByAndroidUIAutomator("new UiScrollable("
                            + "new UiSelector().scrollable(true)).scrollIntoView("
                            + "new UiSelector().textContains(\""+strButton+"\"));");
            new TakeScreenshot().getScreenshot();
            element.click();

        }
        catch(Exception e) {
            LoggerConfig.log.debug("Exception encountered while trying to scroll.."+e.getMessage());
        }
    }

    public  void hideKeyboardAndClick(String strField, String strPage, String strContOnFail) {
        try {

            HideKeyboard();
            new GeneralScript().ButtonClick(strField, strPage, strContOnFail);

        }
        catch(Exception e){
            String strErrorMsg = "Error encountered while hideKeyboardAndClick" + e.getMessage();

            new FailureManagement().manageFailure(strErrorMsg, strContOnFail);
        }
    }

    public  void HideKeyboard() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        StaticData.driver.navigate().back();
    }

    public  void validateApplication(String strField, String strPage, String strContOnFail)  {
        try {
            (new Display()).isElementDisplayed(true, strField, strPage, strContOnFail);
        }catch (Exception e){
            strField = "Error encountered while trying to verify whether " + strField + " field is in " + strPage + " page." + e.getMessage();
            LoggerConfig.log.debug(e);
            (new FailureManagement()).manageFailure(strField, strContOnFail);
        }
    }


}
