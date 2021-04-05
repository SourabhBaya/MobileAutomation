package org.oiShoppingList.mobile.stepDef;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.oiShoppingList.mobile.scripts.GeneralMobileAndroidScript;

public class GeneralMobileAndroid {


    @And("the user hides the keyboard if present and then clicks on \"([^\"]*)\" button in \"([^\"]*)\" page\\.(?: (.*))?$")
    public void theUserHidesTheKeyboardIfPresentAndThenClicksOnButtonInPage(String arg0, String arg1, String arg2) {
        new GeneralMobileAndroidScript().hideKeyboardAndClick(arg0,arg1,arg2);
    }

    @Given("the user has Launched \"([^\"]*)\" application in \"([^\"]*)\" page\\.(?: (.*))?$")
    public void theUserHasLaunchedApplicationInPage(String arg0, String arg1, String arg2) {

        new GeneralMobileAndroidScript().validateApplication(arg0,arg1,arg2);
    }
}
