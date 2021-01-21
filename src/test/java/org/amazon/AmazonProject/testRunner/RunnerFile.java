package org.amazon.AmazonProject.testRunner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;


@RunWith(Cucumber.class)
@CucumberOptions(features = { "target/parallel/features/[CUCABLE:FEATURE].feature" }, 
				 glue = {"org/amazon/common/prePostSteps", "org/amazon/common/stepDef","org/amazon/mobile/stepDef","org/amazon/AmazonProject/stepDef"},
				 plugin = { "json:./Reports/test-report/cucumber-json-report/[CUCABLE:RUNNER].json"})

public class RunnerFile {

}
