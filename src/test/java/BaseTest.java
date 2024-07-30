import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import java.io.File;
import java.net.URL;

public abstract class BaseTest {

    private AppiumDriver driver;
    private static AppiumDriverLocalService server;

    @BeforeClass
    public static void startAppiumServer() {
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder();
        //serviceBuilder.usingAnyFreePort();
        serviceBuilder.usingPort(4723);
        serviceBuilder.usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"));

        serviceBuilder.withArgument(GeneralServerFlag.BASEPATH, "/wd/hub/");
        server = AppiumDriverLocalService.buildService(serviceBuilder);
        server.start();
    }

    @AfterSuite
    public void globalTearDown () {
        if (server != null) {
            server.stop();
        }
    }

    public URL getServiceUrl () {
        return server.getUrl();
    }

}