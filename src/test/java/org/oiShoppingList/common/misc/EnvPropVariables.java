package org.oiShoppingList.common.misc;

import io.cucumber.core.api.Scenario;
import org.oiShoppingList.common.frameworkSupport.CucumberPrePostSteps;
import org.oiShoppingList.common.util.CLParams;

public class EnvPropVariables {

    private String strPreConfigJson = "";

    public String getStringCurrentRunningScenarioName(Scenario scenario) {
        return scenario.getName();
    }

    public String getStrCurrentRunningFeatureName(Scenario scenario){
        String strCurrentRunningFeatureName = new CucumberPrePostSteps().extractingFeatureName(scenario);
        System.out.println("Current running feature name after extraction is "+ strCurrentRunningFeatureName);
        return strCurrentRunningFeatureName;
    }

    public void setPreConfigPath(){
        this.strPreConfigJson = "./ApplicationData/PreConfigurations.json";
    }
    public String getPreConfigPath(){
        setPreConfigPath();
        return this.strPreConfigJson;
    }

    public String getTestScope(){
        return new CLParams().ReadingStringCLParams("Test_Scope", this.getPreConfigPath(),"PreConfig");
    }

    public String getExecutionEnvironment() {
        String strExecutionEnvironment = "";

        if(this.getTestScope().equalsIgnoreCase("Mobile")) {
            strExecutionEnvironment = new CLParams().ReadingStringCLParams("Execution_Environment", this.getMobileJsonPath() , this.getTestScope());
        }
        return strExecutionEnvironment;
    }


    public String getMobileJsonPath(){
        return new CLParams().ReadingStringCLParams("Mobile_JSON_Path", this.getPreConfigPath(), "PreConfig");
    }

    public String getStrMobilePlatform(){
        return new CLParams().ReadingStringCLParams("Platform", this.getMobileJsonPath(),"Mobile");
    }

    public String getStrMobileTestEnvironment(){
        return new CLParams().ReadingStringCLParams("Test Environment", this.getMobileJsonPath(),"Mobile");
    }
}