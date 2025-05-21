package utilities;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;

public class AllureListerner implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = Driver.getDriver();
        if (driver == null) return;

        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment("Failed Test Screenshot", "image/png", "png", screenshot);
        } catch (Exception e) {
            System.out.println("Ekran görüntüsü alınamadı: " + e.getMessage());
        }
    }

    /**
     *
     * @param context
     */
    @Override
    public void onFinish(ITestContext context) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                // Windows için komut
                new ProcessBuilder().command("cmd.exe", "/c", "allure serve").start();
            } else {
                // Mac veya Linux için komut
                new ProcessBuilder().command("bash", "-c", "allure serve").start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
