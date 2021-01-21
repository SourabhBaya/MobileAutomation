package org.amazon.common.stepDef;


import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.amazon.common.scripts.GeneralScript;

import java.util.concurrent.TimeUnit;


public class General 
{

	

	@When("^the user waits till the page gets loaded\\.$")
	public void the_user_waits_till_the_page_gets_loaded() throws InterruptedException
	{
		TimeUnit.SECONDS.sleep(6);
	}
	
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

	@Then("^the user may get a \"([^\"]*)\" link in \"([^\"]*)\" page\\.(?: (.*))?$")
	public void the_user_may_get_a_link_in_page(String linkElement, String page, String strContOnFail) 
	{
		new GeneralScript().LinkClick_ifPresent(linkElement, page, strContOnFail);
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

	@When("^the user clicks on \"([^\"]*)\" link in \"([^\"]*)\" page\\.(?: (.*))?$")
	public void the_user_clicks_on_link_in_page(String strField, String strPage, String strContOnFail) 
	{
		new GeneralScript().LinkClick(strField, strPage, strContOnFail);
	}


	@Given("^the user clicks on \"([^\"]*)\" link and then \"([^\"]*)\" link in \"([^\"]*)\" page\\.(?: (.*))?$")
	public void the_user_clicks_on_link_and_then_link_in_page(String strField1, String strField2, String strPage, String strContOnFail)  
	{
		new GeneralScript().LinkClick(strField1, strField2, strPage, strContOnFail);
	}

	@Given("^the user enters \"([^\"]*)\" in \"([^\"]*)\" page and hits enter key\\.(?: (.*))?$")
	public void the_user_enters_in_page_and_hits_enter_key(String strField, String strPage, String strContOnFail) 
	{
		new GeneralScript().searchData(strField, strPage, strContOnFail);
	}

	@Given("^the user verifies whether \"([^\"]*)\" field is \"([^\"]*)\" in \"([^\"]*)\" page\\.(?: (.*))?$")
	public void the_user_verifies_whether_field_is_in_page(String strField, String strExistence, String strPage, String strContOnFail) {
		(new GeneralScript()).verifyElementPresence(strField, strExistence, strPage, strContOnFail);
	}

	@When("^the user verifies whether \"([^\"]*)\" field is present and \"([^\"]*)\" data \"([^\"]*)\" in \"([^\"]*)\" page\\.(?: (.*))?$")
	public void the_user_verifies_whether_field_is_present_and_data_in_page(String strField, String strValidateCondition,
																			String strValidateValue, String strPage, String strContOnFail)
	{
		new GeneralScript().verifyElementPresenceAndValidate(strField, strValidateCondition, strValidateValue, strPage, strContOnFail);
	}

	@Given("^the user reads test data from ([^\"]*) for ([^\"]*)\\.$")
	public void theUserReadsTestDataFromFor(String strJsonFileName, String strTestCaseName)
	{
		new GeneralScript().readTestDataFrom(strJsonFileName, strTestCaseName);
	}

	@And("^the user clicks on \"([^\"]*)\" button and checks whether \"([^\"]*)\" field is \"([^\"]*)\" in \"([^\"]*)\" page\\.(?: (.*))?$")
	public void theUserClicksOnButtonAndChecksWhetherFieldIsInPage(String strButton, String strField, String strPresence, String strPage,
																   String strContOnFail)

	{
		new GeneralScript().clickAndVerify(strButton, strField, strPresence, strPage, strContOnFail);
	}


}
