@echo off
cd /d "%~dp0src"

echo Regenerating JFlex scanner...
"C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot\bin\java.exe" -jar ..\jflex-1.9.1\lib\jflex-full-1.9.1.jar Scanner.flex

echo.
echo Compiling Yylex.java...
"C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot\bin\javac.exe" Yylex.java

echo.
echo Testing on test1.lang...
"C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot\bin\java.exe" Yylex ..\tests\test1.lang > ..\jflex_output.txt

echo.
echo Testing ManualScanner on test1.lang...
"C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot\bin\java.exe" ManualScanner ..\tests\test1.lang > ..\manual_output.txt

echo.
echo Done! Check jflex_output.txt and manual_output.txt
pause
