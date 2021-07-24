package OrgSiva.Sample;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MongoDBTest {

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
	WebDriver driver;
    MongoCollection<Document> WebDocument;

    @BeforeSuite
    public void ConnectToMongoDb()
    {
        Logger MongodbLogger =Logger.getLogger("org.mongodb.driver");

        //To create MongoDB client
        com.mongodb.client.MongoClient mongoClient =  MongoClients.create("mongodb://127.0.0.1:27017");

        //Create a database
        MongoDatabase Mongo_Database = mongoClient.getDatabase("Sivakumar");

        //Create collection
        WebDocument = Mongo_Database.getCollection("Siva");
    }


    @BeforeTest
    public void Setup()
    {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chrome_options = new ChromeOptions();
        chrome_options.addArguments("--headless");
        driver = new ChromeDriver(chrome_options);
    }



    @Test()
    public void WebScrapeTest()
    {
        driver.get("https://www.google.co.in/");
        String Url = driver.getCurrentUrl();
        String Title = driver.getTitle();
        driver.findElement(By.name("q")).sendKeys("Testing" + Keys.ENTER);
        List<WebElement> LinksList = driver.findElements(By.tagName("a"));

        List<String> HrefList = new ArrayList<String>();
        
       



        //We can only add data in mongoDB in form of document
        Document document = new Document();
        document.append("Url", Url);
        document.append("Title", Title);


        for(WebElement it: LinksList)
        {
            String HrefValue = it.getAttribute("href");
            if(HrefValue != null )
            {
                HrefList.add(HrefValue);
            }
        }
        document.append("Href Value", HrefList);


        List<Document> DocsList = new ArrayList<Document>();
        DocsList.add(document);


        WebDocument.insertMany(DocsList);

    }

    @AfterTest
    public void TearDown()
    {
        driver.quit();
    }
}


