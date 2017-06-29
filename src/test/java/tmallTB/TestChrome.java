package tmallTB;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestChrome {
    public static void main(String[] args) {
        //        ChromeOptions options = new ChromeOptions();
        //        options.addExtensions(new File(
        //            "C:\\Users\\swang\\AppData\\Local\\Google\\Chrome\\UserData\\Default\\Extensions\\ijaobnmmgonppmablhldddpfmgpklbfh\\1.6.0_0.crx"));//添加扩展的方法，将crx文件所在的路径添加进去WebDriver driver=new ChromeDriver(options);
        ///Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome
        System.setProperty("webdriver.chrome.driver",
            "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
        WebDriver webDriver = new ChromeDriver();
        //webDriver.get("http://www.baidu.com");
        webDriver.navigate().to("http://www.baidu.com");
        System.out.println("1 Page title is: " + webDriver.getTitle());
        // WebElement webElement = webDriver.findElement(By.xpath("/html"));
        System.out.println(webDriver.getPageSource());
        // System.out.println(webElement.getAttribute("outerHTML"));
        webDriver.close();
    }

}
