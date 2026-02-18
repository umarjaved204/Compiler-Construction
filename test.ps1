# Test Script for Lexical Analyzer
# This script runs both Manual and JFlex scanners on all test files

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Lexical Analyzer Test Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if compiled
$srcPath = Join-Path $PSScriptRoot "src"
$testsPath = Join-Path $PSScriptRoot "tests"

if (-not (Test-Path "$srcPath\ManualScanner.class")) {
    Write-Host "✗ ManualScanner not compiled. Run .\compile.ps1 first." -ForegroundColor Red
    exit 1
}

if (-not (Test-Path "$srcPath\Yylex.class")) {
    Write-Host "⚠ Yylex not compiled. Only testing Manual Scanner." -ForegroundColor Yellow
    $testJFlex = $false
} else {
    $testJFlex = $true
}

Set-Location $srcPath

# Get all test files
$testFiles = Get-ChildItem "$testsPath\*.lang" | Sort-Object Name

if ($testFiles.Count -eq 0) {
    Write-Host "✗ No test files found in tests directory" -ForegroundColor Red
    exit 1
}

Write-Host "Found $($testFiles.Count) test files" -ForegroundColor Green
Write-Host ""

# Test Manual Scanner
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  MANUAL SCANNER TESTS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

foreach ($file in $testFiles) {
    Write-Host "Testing: $($file.Name)" -ForegroundColor Yellow
    Write-Host "----------------------------------------" -ForegroundColor Gray
    
    try {
        java ManualScanner $file.FullName
        Write-Host ""
        Write-Host "✓ Test completed" -ForegroundColor Green
    } catch {
        Write-Host "✗ Test failed: $_" -ForegroundColor Red
    }
    
    Write-Host ""
    Write-Host ""
}

# Test JFlex Scanner
if ($testJFlex) {
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "  JFLEX SCANNER TESTS" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    
    foreach ($file in $testFiles) {
        Write-Host "Testing: $($file.Name)" -ForegroundColor Yellow
        Write-Host "----------------------------------------" -ForegroundColor Gray
        
        try {
            java Yylex $file.FullName
            Write-Host ""
            Write-Host "✓ Test completed" -ForegroundColor Green
        } catch {
            Write-Host "✗ Test failed: $_" -ForegroundColor Red
        }
        
        Write-Host ""
        Write-Host ""
    }
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  ALL TESTS COMPLETED" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Optional: Run comparison
Write-Host "To compare outputs, run individual tests and redirect to files:" -ForegroundColor Yellow
Write-Host "  java ManualScanner ..\tests\test1.lang > manual_output.txt" -ForegroundColor Gray
Write-Host "  java Yylex ..\tests\test1.lang > jflex_output.txt" -ForegroundColor Gray
Write-Host "  Compare-Object (Get-Content manual_output.txt) (Get-Content jflex_output.txt)" -ForegroundColor Gray
Write-Host ""
