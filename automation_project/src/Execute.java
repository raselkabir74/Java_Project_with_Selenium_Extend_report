import org.testng.TestNG;
import utilities.Common;

import java.util.ArrayList;
import java.util.List;

public class Execute {

    public static void main(String[] args) {
        List<String> suites = new ArrayList<>();
        Common common = new Common();
        TestNG testNG = new TestNG();

        common.performStartUpActivity();
        common.initializeExtentReport();
        testNG.setUseDefaultListeners(false);
        suites.add("./Configurations/testng.xml");
        testNG.setTestSuites(suites);
        testNG.run();
    }
}