package org.oiShoppingList.common.stepDef;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.oiShoppingList.common.scripts.GeneralScript;
import org.oiShoppingList.mobile.scripts.GeneralMobileAndroidScript;

import java.util.concurrent.TimeUnit;


public class General 
{

	@When("^the user enters \"([^\"]*)\" in \"([^\"]*)\" page\\.(?: (.*))?$")
	public void the_user_enters_in_page(String strField, String strPage, String strContOnFail) 
	{
		new GeneralScript().enterData(strField, strPage, strContOnFail);
	}

	@Given("^the user clicks on \"([^\"]*)\" button in \"([^\"]*)\" page\\.(?: (.*))?$")
	public void the_user_clicks_on_button_in_page(String strField, String strPage, String strContOnFail) 
	{
		new GeneralScript().ButtonClick(strField, strPage, strContOnFail);
	}

	@When("^the user clicks on \"([^\"]*)\" menu in \"([^\"]*)\" page\\.(?: (.*))?$")
	public void the_user_clicks_on_menu_in_page(String strField, String strPage, String strContOnFail) 
	{
		new GeneralScript().ButtonClick(strField, strPage, strContOnFail);
	}

	@When("^the user clicks on \"([^\"]*)\" tab in \"([^\"]*)\" page\\.(?: (.*))?$")
	public void the_user_clicks_on_tab_in_page(String strField, String strPage, String strContOnFail) 
	{
		new GeneralScript().ButtonClick(strField, strPage, strContOnFail);
	}

	@When("^the user verifies whether \"([^\"]*)\" field is present and \"([^\"]*)\" data \"([^\"]*)\" in \"([^\"]*)\" page\\.(?: (.*))?$")
	public void the_user_verifies_whether_field_is_present_and_data_in_page(String strField, String strValidateCondition,
																			String strValidateValue, String strPage, String strContOnFail)
	{
		new GeneralScript().verifyElementPresenceAndValidate(strField, strValidateCondition, strValidateValue, strPage, strContOnFail);
	}


	@Given("^the user verifies whether \"([^\"]*)\" tab is \"([^\"]*)\" in \"([^\"]*)\" page\\.(?: (.*))?$")
	public void the_user_verifies_whether_tab_is_in_page(String strField, String strExistence, String strPage,
														 String strContOnFail)
	{
		new GeneralScript().verifyElementPresence(strField, strExistence, strPage, strContOnFail);
	}


	@And("the user navigate back to previous page.")
	public void theUserNavigateBackToPreviousPage() {

		new GeneralMobileAndroidScript().HideKeyboard();
	}

	@And("the user verifies sorting of \"([^\"]*)\" in \"([^\"]*)\" page.")
	public void theUserVerifiesSortingOfInPage(String arg0, String arg1) {

		new GeneralMobileAndroidScript().SortingList(arg0, arg1);
	}
}
