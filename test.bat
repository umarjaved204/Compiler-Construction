@echo off
REM Test Script for Lexical Analyzer (Windows CMD version)

echo ========================================
echo   Lexical Analyzer Test Script
echo ========================================
echo.

REM Check if compiled
if not exist "%~dp0src\ManualScanner.class" (
    echo [ERROR] ManualScanner not compiled. Run compile.bat first.
    pause
    exit /b 1
)

cd /d "%~dp0src"

echo ========================================
echo   MANUAL SCANNER TESTS
echo ========================================
echo.

for %%f in ("%~dp0tests\*.lang") do (
    echo Testing: %%~nxf
    echo ----------------------------------------
    java ManualScanner "%%f"
    echo.
    echo [OK] Test completed
    echo.
    echo.
)

if exist Yylex.class (
    echo ========================================
    echo   JFLEX SCANNER TESTS
    echo ========================================
    echo.
    
    for %%f in ("%~dp0tests\*.lang") do (
        echo Testing: %%~nxf
        echo ----------------------------------------
        java Yylex "%%f"
        echo.
        echo [OK] Test completed
        echo.
        echo.
    )
) else (
    echo [WARNING] Yylex not compiled. Only Manual Scanner tested.
    echo.
)

echo ========================================
echo   ALL TESTS COMPLETED
echo ========================================
echo.
pause
