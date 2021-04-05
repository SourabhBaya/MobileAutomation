package org.oiShoppingList.mobile.scripts;

import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.MobileElement;
import org.oiShoppingList.common.misc.FailureManagement;
import org.oiShoppingList.common.misc.StaticData;
import org.oiShoppingList.common.scripts.GeneralScript;
import org.oiShoppingList.common.toolbox.Display;
import org.oiShoppingList.common.util.LoggerConfig;
import org.oiShoppingList.common.util.TakeScreenshot;
import java.util.concurrent.TimeUnit;

public class GeneralMobileAndroidScript {

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

    public  void HideKeyboard(){
        try {
            TimeUnit.SECONDS.sleep(5);
            StaticData.driver.navigate().back();
        }catch (Exception e){

        }
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


    public void SortingList(String strItemList, String strPage) {



    }
}
