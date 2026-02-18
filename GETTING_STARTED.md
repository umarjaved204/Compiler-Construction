# Getting Started Guide

## Prerequisites Installation

### 1. Install Java Development Kit (JDK)

JFlex requires Java to run. Follow these steps to install Java:

#### Option A: Download from Oracle
1. Visit: https://www.oracle.com/java/technologies/downloads/
2. Download JDK 17 or later for Windows
3. Run the installer
4. Accept the license agreement
5. Follow the installation wizard

#### Option B: Download from Adoptium (Recommended)
1. Visit: https://adoptium.net/
2. Download OpenJDK 17 or later
3. Run the installer
4. Add Java to PATH when prompted

#### Verify Java Installation
Open PowerShell and run:
```powershell
java -version
javac -version
```

You should see version information for both commands.

### 2. JFlex is Already Downloaded

JFlex 1.9.1 has been downloaded and extracted to the `jflex-1.9.1` directory.

---

## Quick Start

### Step 1: Compile the Manual Scanner

```powershell
# Navigate to src directory
cd src

# Compile all Java files
javac *.java

# Run Manual Scanner on test1.lang
java ManualScanner ../tests/test1.lang
```

### Step 2: Generate JFlex Scanner

```powershell
# Still in src directory

# Generate scanner from Scanner.flex
java -jar ../jflex-1.9.1/lib/jflex-full-1.9.1.jar Scanner.flex

# Compile all Java files (including generated Yylex.java)
javac *.java

# Run JFlex Scanner on test1.lang
java Yylex ../tests/test1.lang
```

### Step 3: Run All Tests

```powershell
# Test Manual Scanner on all files
foreach ($file in Get-ChildItem ../tests/*.lang) {
    Write-Host "`n=== Testing $($file.Name) with ManualScanner ==="
    java ManualScanner $file.FullName
}

# Test JFlex Scanner on all files
foreach ($file in Get-ChildItem ../tests/*.lang) {
    Write-Host "`n=== Testing $($file.Name) with JFlex ==="
    java Yylex $file.FullName
}
```

---

## Helper Scripts

### compile.ps1 - Compile Both Scanners

```powershell
# Navigate to project root
cd "c:\Users\umar_\Desktop\Compiler Assignment 01"

# Run the helper script
.\compile.ps1
```

### test.ps1 - Run All Tests

```powershell
# Navigate to project root
cd "c:\Users\umar_\Desktop\Compiler Assignment 01"

# Run the test script
.\test.ps1
```

---

## Project Structure

```
Compiler Assignment 01/
├── src/
│   ├── ManualScanner.java       # Manual DFA scanner
│   ├── Token.java               # Token class
│   ├── TokenType.java           # Token types enum
│   ├── SymbolTable.java         # Symbol table
│   ├── ErrorHandler.java        # Error handling
│   ├── Scanner.flex             # JFlex specification
│   └── Yylex.java               # Generated JFlex scanner
├── tests/
│   ├── test1.lang               # All valid tokens
│   ├── test2.lang               # Complex expressions
│   ├── test3.lang               # Strings/chars
│   ├── test4.lang               # Errors
│   ├── test5.lang               # Comments
│   └── TestResults.txt          # Test results
├── docs/
│   ├── Automata_Design.txt      # NFA/DFA designs
│   ├── LanguageGrammar.txt      # Grammar spec
│   └── Comparison.txt           # Scanner comparison
├── jflex-1.9.1/                 # JFlex installation
└── README.md                    # Main documentation
```

---

## Common Issues and Solutions

### Issue: "java is not recognized"
**Solution:** Java is not installed or not in PATH. Install JDK and add to PATH.

### Issue: "javac is not recognized"
**Solution:** JDK is not installed. Install JDK, not just JRE.

### Issue: "Scanner.flex: file not found"
**Solution:** Make sure you're in the src directory when running JFlex.

### Issue: "Class not found" when running
**Solution:** Compile all .java files first with `javac *.java`

### Issue: JFlex generation fails
**Solution:** Check Scanner.flex syntax. Common issues:
- Missing %% separators
- Invalid regex patterns
- Unclosed actions {}

---

## Testing Individual Files

### Test a Specific File

```powershell
# Manual Scanner
cd src
java ManualScanner ../tests/test1.lang

# JFlex Scanner
cd src
java Yylex ../tests/test1.lang
```

### Redirect Output to File

```powershell
# Save Manual Scanner output
java ManualScanner ../tests/test1.lang > output_manual.txt

# Save JFlex Scanner output
java Yylex ../tests/test1.lang > output_jflex.txt

# Compare outputs
Compare-Object (Get-Content output_manual.txt) (Get-Content output_jflex.txt)
```

---

## Understanding the Output

### Token Format
```
<TOKEN_TYPE, "lexeme", Line: X, Col: Y>
```

Example:
```
<KEYWORD, "start", Line: 1, Col: 1>
<IDENTIFIER, "Count", Line: 2, Col: 13>
<ASSIGNMENT_OP, "=", Line: 2, Col: 19>
```

### Statistics Section
Shows:
- Total tokens generated
- Lines processed
- Comments removed
- Token counts by type

### Symbol Table Section
Shows all identifiers with:
- First occurrence (line, column)
- Frequency (number of uses)

### Error Section
Shows any lexical errors with:
- Error type
- Location (line, column)
- Lexeme causing error
- Reason/description

---

## Next Steps

1. **Install Java** if not already installed
2. **Compile** the Manual Scanner
3. **Generate** JFlex Scanner
4. **Run tests** on all .lang files
5. **Review** outputs and compare results
6. **Create** Automata_Design.pdf from the .txt file
7. **Create** Comparison.pdf from the .txt file
8. **Update** team member information in README.md

---

## Additional Resources

- JFlex Manual: https://jflex.de/manual.html
- Java Tutorial: https://docs.oracle.com/javase/tutorial/
- Regex Reference: https://www.regular-expressions.info/
- Compiler Design: Dragon Book (Aho, Sethi, Ullman)

---

## Contact

For questions or issues, please refer to the course materials or contact:
- Course: CS4031 Compiler Construction
- Assignment: 01 - Lexical Analyzer

---

*Last Updated: February 18, 2026*
