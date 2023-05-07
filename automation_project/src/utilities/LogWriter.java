package utilities;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class LogWriter {

    List<String> log4jProperties = Arrays.asList("log4j.appender.FA.layout=org.apache.log4j.PatternLayout", "log4j.appender.FA.Threshold=INFO",
            "log4j.rootLogger=DEBUG, CA, FA", "log4j.appender.FA=org.apache.log4j.FileAppender",
            "log4j.appender.CA=org.apache.log4j.ConsoleAppender", "log4j.appender.CA.Threshold=INFO",
            "log4j.appender.CA.layout=org.apache.log4j.PatternLayout", "log4j.appender.FA.File=",
            "log4j.appender.FA.layout.ConversionPattern=%5p | %d{yyyy-MM-dd hh\\:mm\\:ss a} | %F | %L | %m%n",
            "log4j.appender.CA.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n");

    public enum logLevel {
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL
    }

    /**
     * Implementing write to log file functionality
     *
     * @param level   log level
     * @param message message to write
     */
    public static void writeToLogFile(logLevel level, String message) {
        if (level.equals(logLevel.DEBUG))
            Common.logger.debug(message);
        else if (level.equals(logLevel.INFO))
            Common.logger.info(message);
        else if (level.equals(logLevel.WARN))
            Common.logger.warn(message);
        else if (level.equals(logLevel.ERROR))
            Common.logger.error(message);
        else if (level.equals(logLevel.FATAL))
            Common.logger.fatal(message);
        else
            Common.logger.error("Invalid log level : '" + level + "' provided to log writer. Unable to log the message.");
    }

    /**
     * Implementing generate output directory functionality
     */
    public void generateOutputDirectory() {
        try {
            File currentDirectory = new File("");
            boolean configurationCreateStatus = new File(currentDirectory.getCanonicalPath() + "/Configurations").mkdirs();
            boolean log4jCreateStatus = new File(currentDirectory.getCanonicalPath() + "/Configurations/log4j.properties").createNewFile();
            boolean testConfigurationsCreateStatus = new File(currentDirectory.getCanonicalPath() + "/Configurations/TestConfigurations.properties").createNewFile();
            boolean testngCreateStatus = new File(currentDirectory.getCanonicalPath() + "/Configurations/testng.xml").createNewFile();
            boolean testReportsCreateStatus = new File(currentDirectory.getCanonicalPath() + "/TestReports").mkdirs();

            Writer output = new BufferedWriter(new FileWriter(currentDirectory.getCanonicalPath() + "/Configurations/log4j.properties"));
            for (String line : log4jProperties) {
                output.append(line).append("\n");
            }
            output.close();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_ss_a");
            LocalDateTime now = LocalDateTime.now();
            Common.reportGenerationTimeStamp = dtf.format(now);
            String directoryPath = currentDirectory.getCanonicalPath() + "/TestReports/" + Common.reportGenerationTimeStamp;
            boolean createStatus = new File(directoryPath).mkdirs();
            if (createStatus) {
                Common.outputDirectory = directoryPath;
            }
            if (configurationCreateStatus && log4jCreateStatus && testConfigurationsCreateStatus && testngCreateStatus && testReportsCreateStatus) {
                System.out.println("All files have been created successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Implementing create log file functionality
     */
    public void createLogFile() {
        String log4jPropertyFileName = "./Configurations/log4j.properties";
        String logFilePath = Common.outputDirectory + "\\LogFile.log";
        try {
            logFilePath = logFilePath.replace("/", "\\");
            FileInputStream fileInputStream = new FileInputStream(log4jPropertyFileName);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            properties.setProperty("log4j.appender.FA.File", logFilePath);
            FileOutputStream output = new FileOutputStream(log4jPropertyFileName);
            properties.store(output, "Log file path updated to : " + logFilePath);
            PropertyConfigurator.configure(log4jPropertyFileName);
            output.close();
            fileInputStream.close();
            Common.logger = Logger.getLogger(logFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
