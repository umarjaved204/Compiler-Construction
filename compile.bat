@echo off
REM Compilation Script for Lexical Analyzer (Windows CMD version)

echo ========================================
echo   Lexical Analyzer Compilation Script
echo ========================================
echo.

REM Check if Java is installed
echo Checking Java installation...
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java not found. Please install JDK first.
    echo Visit: https://adoptium.net/
    pause
    exit /b 1
)
echo [OK] Java found
echo.

REM Navigate to src directory
cd /d "%~dp0src"
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] src directory not found!
    pause
    exit /b 1
)

echo [1/3] Compiling Manual Scanner...
javac TokenType.java Token.java ErrorHandler.java SymbolTable.java ManualScanner.java
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Manual Scanner compilation failed
    pause
    exit /b 1
)
echo [OK] Manual Scanner compiled
echo.

echo [2/3] Generating JFlex Scanner...
java -jar "%~dp0jflex-1.9.1\lib\jflex-full-1.9.1.jar" Scanner.flex
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] JFlex Scanner generation failed
    pause
    exit /b 1
)
echo [OK] JFlex Scanner generated
echo.

echo [3/3] Compiling JFlex Scanner...
javac Yylex.java
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] JFlex Scanner compilation failed
    pause
    exit /b 1
)
echo [OK] JFlex Scanner compiled
echo.

echo ========================================
echo   Compilation Complete!
echo ========================================
echo.
echo Next steps:
echo   1. Run: test.bat to test all scanners
echo   2. Or test manually:
echo      cd src
echo      java ManualScanner ..\tests\test1.lang
echo      java Yylex ..\tests\test1.lang
echo.
pause
