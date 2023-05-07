## Tools & Technologies : ##
Following tools and technologies have been used in this framework

| Sl # | Tools & Technology | Version | 
| ---- | ------------------ | ------- 
| 1 | JAVA | 14 |
| 2	| Selenium web driver |
| 3	| TestNG | 7.0.0 |
| 4 | Extant report | 4.1.6 |

## Environment Setup : ##
1. Install Java 14 if not already installed.
2. Verify Java 14 is installed properly. Open cmd and type java -version and then press enter. if the java version is shown that means java installed successful.
Troubleshoot: Set java path in the system environment variable. (if java version doesn't show in console)

## Run Automation Script : ##
Before initiating the automation script execution you can change some configurations. Open data folder in the following path:
../automation_project/Data

1. Find 'TestCaseSettings.xlsx' file to set the parameter of Execution Flag column. This column can handle the test cases need to be run. 
   If a cell value of this column is Yes that means that particular test case will be run.
2. Find 'TestConfiguration.xlsx' file. Here from the 'Browser' dropdown, you will be able to select your desired browser to run the execution.
3. In the 'automation_project' folder, double-click on the 'execute.bat' file.
4. After clicking on the batch file the execution will be initiated and after completion of the execution, the test reports can be found in 'automation_project\TestReports' folder with current date and time based on the test run.