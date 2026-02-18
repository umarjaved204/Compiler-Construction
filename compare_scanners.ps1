# Regenerate and test both scanners
$ErrorActionPreference = "Stop"
$java = "C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot\bin\java.exe"
$javac = "C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot\bin\javac.exe"

Set-Location "src"

Write-Host "=== Regenerating JFlex Scanner ===" -ForegroundColor Cyan
& $java -jar ..\jflex-1.9.1\lib\jflex-full-1.9.1.jar Scanner.flex

Write-Host "`n=== Compiling Yylex.java ===" -ForegroundColor Cyan
& $javac Yylex.java

Write-Host "`n=== Testing JFlex Scanner on test1.lang ===" -ForegroundColor Cyan
& $java Yylex ..\tests\test1.lang | Select-String "Total tokens:|Comments removed:|Unique identifiers:"

Write-Host "`n=== Testing Manual Scanner on test1.lang ===" -ForegroundColor Cyan
& $java ManualScanner ..\tests\test1.lang | Select-String "Total tokens:|Comments removed:|Unique identifiers:"

Write-Host "`n=== Comparison ===" -ForegroundColor Green
Write-Host "Check if token counts match above"

Set-Location ..
