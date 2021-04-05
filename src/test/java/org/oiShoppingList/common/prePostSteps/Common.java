package org.oiShoppingList.common.prePostSteps;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.oiShoppingList.common.frameworkSupport.CucumberPrePostSteps;

public class Common {
	CucumberPrePostSteps cucumberPrePostSteps = new CucumberPrePostSteps();

	@Before(order=0)
    public void beforeAfterAll() {

    }

	@Before(order=1)
	public void beforeEachScenario(Scenario scenario) {
		cucumberPrePostSteps.beforeEachScenario(scenario);
	}
	
	@After
	public void afterEachScenario() {
		cucumberPrePostSteps.afterEachScenario();
	}
}