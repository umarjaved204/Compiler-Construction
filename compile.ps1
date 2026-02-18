# Compilation Script for Lexical Analyzer
# This script compiles both Manual and JFlex scanners

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Lexical Analyzer Compilation Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Java is installed
Write-Host "Checking Java installation..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "✓ Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Java not found. Please install JDK first." -ForegroundColor Red
    Write-Host "  Visit: https://adoptium.net/" -ForegroundColor Yellow
    exit 1
}

# Navigate to src directory
$srcPath = Join-Path $PSScriptRoot "src"
if (-not (Test-Path $srcPath)) {
    Write-Host "✗ src directory not found!" -ForegroundColor Red
    exit 1
}

Set-Location $srcPath
Write-Host "Changed directory to: $srcPath" -ForegroundColor Gray
Write-Host ""

# Step 1: Compile Manual Scanner
Write-Host "[1/3] Compiling Manual Scanner..." -ForegroundColor Yellow
$manualFiles = @(
    "TokenType.java",
    "Token.java",
    "ErrorHandler.java",
    "SymbolTable.java",
    "ManualScanner.java"
)

try {
    javac $manualFiles 2>&1 | Out-String | Write-Host
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ Manual Scanner compiled successfully" -ForegroundColor Green
    } else {
        Write-Host "✗ Manual Scanner compilation failed" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "✗ Error compiling Manual Scanner: $_" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Step 2: Generate JFlex Scanner
Write-Host "[2/3] Generating JFlex Scanner..." -ForegroundColor Yellow
$jflexJar = Join-Path $PSScriptRoot "jflex-1.9.1\lib\jflex-full-1.9.1.jar"

if (-not (Test-Path $jflexJar)) {
    Write-Host "✗ JFlex not found at: $jflexJar" -ForegroundColor Red
    Write-Host "  JFlex should be in jflex-1.9.1/lib/ directory" -ForegroundColor Yellow
    exit 1
}

try {
    java -jar $jflexJar Scanner.flex 2>&1 | Out-String | Write-Host
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ JFlex Scanner generated successfully" -ForegroundColor Green
    } else {
        Write-Host "✗ JFlex Scanner generation failed" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "✗ Error generating JFlex Scanner: $_" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Step 3: Compile JFlex Scanner
Write-Host "[3/3] Compiling JFlex Scanner..." -ForegroundColor Yellow
try {
    javac Yylex.java 2>&1 | Out-String | Write-Host
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ JFlex Scanner compiled successfully" -ForegroundColor Green
    } else {
        Write-Host "✗ JFlex Scanner compilation failed" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "✗ Error compiling JFlex Scanner: $_" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Compilation Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "  1. Run: .\test.ps1 to test all scanners" -ForegroundColor White
Write-Host "  2. Or test manually:" -ForegroundColor White
Write-Host "     cd src" -ForegroundColor Gray
Write-Host "     java ManualScanner ..\tests\test1.lang" -ForegroundColor Gray
Write-Host "     java Yylex ..\tests\test1.lang" -ForegroundColor Gray
Write-Host ""
