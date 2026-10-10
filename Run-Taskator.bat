@echo off
cd /d "%~dp0"
if not exist bin mkdir bin
javac -cp "lib\mariadb-java-client-3.5.10.jar" -d bin src\util\DatabaseConnection.java src\interfaces\*.java src\model\*.java src\dao\*.java src\service\*.java src\exceptions\*.java src\threads\*.java src\gui\*.java src\App.java
if errorlevel 1 (
  echo.
  echo Build failed. Please check the error above.
  pause
  exit /b 1
)
java -cp "bin;lib\mariadb-java-client-3.5.10.jar" App
pause
