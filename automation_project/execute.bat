echo off
Title Automation_Execution
md Bin
dir /s /B *.java > Bin\JavaSourcesFiles.txt

set VALUE=CurrentVersion
set KEY_1="HKLM\SOFTWARE\JavaSoft\Java Development Kit"
set KEY_2="HKLM\SOFTWARE\JavaSoft\JDK"
set REG_1=reg.exe
set REG_2="C:\Windows\sysnative\reg.exe"
set REG_3="C:\Windows\syswow64\reg.exe"

set KEY=%KEY_1%
set REG=%REG_1%
%REG% QUERY %KEY% /v %VALUE% 2>nul
IF %ERRORLEVEL% EQU 0 GOTO _set_value

set KEY=%KEY_2%
set REG=%REG_1%
%REG% QUERY %KEY% /v %VALUE% 2>nul
IF %ERRORLEVEL% EQU 0 GOTO _set_value

::- %REG_2% is for 64-bit installations, using "C:\Windows\sysnative"
set KEY=%KEY_1%
set REG=%REG_2%
%REG% QUERY %KEY% /v %VALUE% 2>nul
IF %ERRORLEVEL% EQU 0 GOTO _set_value

set KEY=%KEY_2%
set REG=%REG_2%
%REG% QUERY %KEY% /v %VALUE% 2>nul
IF %ERRORLEVEL% EQU 0 GOTO _set_value

::- %REG_3% is for 32-bit installations on a 64-bit system, using "C:\Windows\syswow64"
set KEY=%KEY_1%
set REG=%REG_3%
%REG% QUERY %KEY% /v %VALUE% 2>nul
IF %ERRORLEVEL% EQU 0 GOTO _set_value

set KEY=%KEY_2%
set REG=%REG_3%
%REG% QUERY %KEY% /v %VALUE% 2>nul
IF %ERRORLEVEL% EQU 0 GOTO _set_value

:_set_value
FOR /F "tokens=2,*" %%a IN ('%REG% QUERY %KEY% /v %VALUE%') DO (set JDK_VERSION=%%b)

set KEY=%KEY%\%JDK_VERSION%
set VALUE=JavaHome
FOR /F "tokens=2,*" %%a IN ('%REG% QUERY %KEY% /v %VALUE%') DO (set JAVAHOME=%%b)

"%JAVAHOME%/bin/javac.exe" -d Bin -sourcepath src -classpath ./Ext/Jars/*;./Ext/Jars/poi-4.1.2/*; @Bin\JavaSourcesFiles.txt

"%JAVAHOME%/bin/java.exe" -classpath ./Bin;./Ext/Jars/*;./Ext/Jars/poi-4.1.2/*; Execute