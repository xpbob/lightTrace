@echo off
SET curPath=%~dp0
java -Xbootclasspath/a:"%JAVA_HOME%\lib\tools.jar" -jar trace-0.0.1-SNAPSHOT-loadagent.jar  %curPath% %1 %2
pause