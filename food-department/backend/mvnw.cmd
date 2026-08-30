@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------

@IF "%OPTION_SET%"=="1" GOTO Execution

@SETLOCAL
@SET OPTION_SET=1

@SET MAVEN_CMD_LINE_ARGS=%*

@IF NOT "%JAVA_HOME%"=="" GOTO OkJHome
@FOR %%i IN (java.exe) DO @SET "JAVACMD=%%~$PATH:i"
@IF NOT "%JAVACMD%"=="" GOTO checkJavaCmd
@ECHO JAVA_HOME is not set and no 'java' command could be found in your PATH.
@GOTO error

:OkJHome
@SET "JAVACMD=%JAVA_HOME%\bin\java.exe"

:checkJavaCmd
@IF EXIST "%JAVACMD%" GOTO chkMHome

@ECHO JAVA_HOME is set to an invalid directory: %JAVA_HOME%
@GOTO error

:chkMHome
@SET "MAVEN_PROJECTBASEDIR=%~dp0"

@IF EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" GOTO runWrapper

@REM Download wrapper jar if missing
@SET WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
@SET WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('%WRAPPER_URL%', '%WRAPPER_JAR%')"

:runWrapper
SET "STRIP_DIR=%MAVEN_PROJECTBASEDIR:~0,-1%"
"%JAVACMD%" -classpath "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" "-Dmaven.home=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper" "-Dmaven.multiModuleProjectDirectory=%STRIP_DIR%" org.apache.maven.wrapper.MavenWrapperMain %*

:error
