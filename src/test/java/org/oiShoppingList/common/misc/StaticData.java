package org.oiShoppingList.common.misc;

import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.WebDriver;
import io.cucumber.core.api.Scenario;

public class StaticData 
{   
	public static WebDriver driver;
	public static Scenario scenario;
	public static String strCaptureScreenshotOnFailureOnly = "";
	public static HashMap<String, String> mapTestData;
	public static String strIdentifierType = "";
	public static String strIdentifiervalue = "";
	public static int nMutipleTestData_ForOneField_Counter;
	public static boolean bIsFailed;
	public static List<String> usingDevice = null;
}