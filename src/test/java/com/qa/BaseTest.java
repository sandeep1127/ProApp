package com.qa;

import org.testng.annotations.Test;

import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;

import org.testng.annotations.BeforeTest;

import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;

public class BaseTest {

protected static AppiumDriver driver;
	
	
  
  @BeforeTest                      // @BeforeTest runs only once before running any test classes under <test> tag in testNG xml. We use @BeforeTest instead of @BeforeMethod used in web because in Mobile we will install our app only once so that all cases can be run on it and not install it after very test case.
  public void beforeTest(String platformName ,String deviceName, String platformVersion  , String udid , String emulator   ) throws Exception {   // Initialize driver in this method so that driver is available for all TEST classes. We just need to initiate it once and install app once since all test cases will be executed on 1 device only. In SELENIUM we used to initialize it @BefireMethod level coz in there we initiated driver after every test case.,
	  URL url;
	   platform= platformName;   // To get the platformName which we'll get from TestNG. Its done for the method "getText" created below
	   try {
		  props = new Properties();   // Created Properties Object which will be used to initlaize the Config properties file.
		  String propFileName= "config.properties";  //  complete file path is not  needed since the files is in classpath [ie src/main/resources]		 		  
		  inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);     //Since we created CONFIG file inside src/test/resouces, we can use "Input stream" to load the Config File. 
		  props.load(inputStream);  // We're loading Properies file here. Now inotrder to get properties, all we need to do is use "getProperty()" method
		  
		  String xmlFileName = "ExpectedResultsData/expetedResults.xml";   // saving our ExpectedResusults xml file
		  stringsis = getClass().getClassLoader().getSystemResourceAsStream(xmlFileName);   // Read the expectedResults xml file as InputStream.
		  utils = new TestUtils();
		  strings = utils.parseStringXML(stringsis);  // This will return HASHMAP having our XML key & Pair. So Now we can use strings object in our test class but we need to close both the InputStreams "inputStream and 'stringsis' by using Finally block below in CATCH Block
		  
		  DesiredCapabilities caps = new DesiredCapabilities();                  // There is another way to set the capabilities which is using "OPTIONS" Class
			
		  // 3 capabilities "platformName" ,"deviceName" and "UDID"  will be mentioned in testNG XML file asthe
	 	  // 3 capabilities "automationName" , "appPackage" , "appActivity" & "appiumServer URL" are global comfig paramters so will store them in config properties file
			
			caps.setCapability("platformName",platformName );   
			caps.setCapability("deviceName",deviceName );
			
			
			switch(platformName) {   // Using Switch Case fro Platform ie Android/IOS             
			
			case "Android":  
				//caps.setCapability("automationName", "UiAutomator2");          // Automation name, App Activity, App Package and Appium Server are global config parameters so we need them in CONFIG FILE
				caps.setCapability("automationName", props.getProperty("androidAutomationName"));  // using Properties file method to use Capability stored in properties File
				
				
				//caps.setCapability("appPackage", "com.swaglabsmobileapp");    // Its SWAG LABS app
				caps.setCapability("appPackage", props.getProperty("androidAppPackage"));   
				
				//caps.setCapability("appActivity", "com.swaglabsmobileapp.SplashActivity");  // Its SWAG LABS app
				caps.setCapability("appActivity", props.getProperty("androidAppActivity"));  
				
				
				
				
				if(emulator.equalsIgnoreCase("true")) {  // If our device is REAL, it won't use this capability and if it is then it will run this emulator. it depends on the value of "TRUE" or "FALSE" in testNG xml key <parameter name= "emulator" value ="false"/ which i will need to update.
					caps.setCapability("appActivity", platformVersion); 
					caps.setCapability("avd", deviceName);  // used this if we have EMULTOR for ANDROID
					
				}
				else {
					
					caps.setCapability("udid", udid);  // If its not EMULTAOR we use this for REAL device . Appium will use UDID if its real, otherwise PLATFORMVERSION if its  emulator . Later its found UDID can be used for both emulator and real device and is always preferred over platform version
				}
				
				// URL appUrl1 = getClass().getClassLoader().getResource(props.getProperty("androidAppLocation"));   // // Its a way to get complete file path of app. We do that by using "GetResource" Method which will return URL object and then we can pass it as Capability value in next line. This method basically will get the complete path of SRC/test/Resources Package and will append the Relative path of app given in Config file. BUT IT WOn't work and will gove NULL as the value because of using getClassLoader() method.
				String androidAppUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
				
				System.out.println("Url of the app location is:" + androidAppUrl);  // THIS WILL SHOW NULL because of using "classLoader()' method above
				
				
				caps.setCapability("app", androidAppUrl);   // This is the URLwhere apk file is located so that App is installed whenever driver is initalized.
				
				
				//URL url = new URL("http://0.0.0.0:4723/");
			    url = new URL(props.getProperty("appiumURL"));   // variable created above TRY block
				
			
				driver = new AppiumDriver(url, caps);
				break;
				
			case "iOS":
				//caps.setCapability("automationName", "UiAutomator2");          // Automation name, App Activity, App Package and Appium Server are global config parameters so we need them in CONFIG FILE
				caps.setCapability("automationName", props.getProperty("iOSAutomationName"));  // using Properties file method to use Capability stored in properties File
				caps.setCapability("platformVersion", platformVersion);
					
				// URL appUrl1 = getClass().getClassLoader().getResource(props.getProperty("androidAppLocation"));   // // Its a way to get complete file path of app. We do that by using "GetResource" Method which will return URL object and then we can pass it as Capability value in next line. This method basically will get the complete path of SRC/test/Resources Package and will append the Relative path of app given in Config file. BUT IT WOn't work and will gove NULL as the value because of using getClassLoader() method.
				String iOSAppUrl = getClass().getResource(props.getProperty("iOSAppLocation")).getFile();
				
				System.out.println("Url of the app location is:" + iOSAppUrl);  // THIS WILL SHOW NULL because of using "classLoader()' method above
				
				caps.setCapability("bundleId", props.getProperty("iOSBundleId"));  // USED in IOS if APP IS ALREADY INSTALLED
				//caps.setCapability("app", iOSAppUrl);   // This is the URL where apk file is located so that App is installed whenever driver is initalized. ITS USED TO INSTALL THE APP from the given Location.
				
				
				//URL url = new URL("http://0.0.0.0:4723/");
				url = new URL(props.getProperty("appiumURL"));
				driver = new IOSDriver(url, caps);
				break;	
				
				
				default:
					throw new Exception("Inavlid Platform"  + platformName);   // It will throw an exception if platform name is mentioned dfferent than IOS or Android
					
					
			}
			
			
			
			//String sessionId = driver.getSessionId().toString();
			//driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));    //  We will use EXPLICITWAITS and not use this
	  }
	 
		  
	
	  
	  
	  catch(Exception e){
		 e.printStackTrace();
		 throw e;   // If driver initialization fails, the program should immediately exit and report the error, preventing further execution.
	  }
	finally {      // we're closing both the InputStreams "inputStream and 'stringsis' by using Finally block which we used above in TRY  block. Now we go to TEST class and replace all Expected Results with our STRINGS used in XML of Expected results.
		if(inputStream !=null) {    
			inputStream.close();
			
		}
		if (stringsis !=null) {
			stringsis.close();
		}
	}
  }
			  
	 
  }

  @AfterTest                      // runs only once after running all TEST CLASSES under <test> tag in testNG xml.
  public void afterTest() {
  }

}
