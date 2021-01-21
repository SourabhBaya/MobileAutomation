package org.amazon.common.toolbox;


import org.amazon.common.misc.StaticData;
import org.amazon.common.util.LoggerConfig;

public class PopupHandle {

    public void clickOnOK(){

        try {
            StaticData.driver.switchTo().alert().accept();
        }catch (Exception e){
            LoggerConfig.log.debug("Exception occured:"+e.getMessage());
        }
    }

    public void clickOnCancel(){
        try {
            StaticData.driver.switchTo().alert().dismiss();
        }catch (Exception e){
            LoggerConfig.log.debug("Exception occured:"+e.getMessage());
        }
    }

    public void getTextFromPopup(){
        try{
            String textValue = StaticData.driver.switchTo().alert().getText();
            System.out.println("Popup text: " +textValue);
        }
        catch (Exception e){
            LoggerConfig.log.debug("Exception occured:"+e.getMessage());
        }
    }

    public void enterTextIntoPopup(String enterText){
        try{
            StaticData.driver.switchTo().alert().sendKeys(enterText);
        }
        catch (Exception e){
            LoggerConfig.log.debug("Exception occured:"+e.getMessage());
        }
    }
}
