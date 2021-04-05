package org.oiShoppingList.OIShoppingListProject.testRunner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith(Cucumber.class)
@CucumberOptions(features = { "target/parallel/features/[CUCABLE:FEATURE].feature" }, 
				 glue = {"org/oiShoppingList/common/prePostSteps", "org/oiShoppingList/common/stepDef","org/oiShoppingList/mobile/stepDef","org/oiShoppingList/OIShoppingListProject/stepDef"},
				 plugin = { "json:./Reports/test-report/cucumber-json-report/[CUCABLE:RUNNER].json"})

public class RunnerFile {

}
