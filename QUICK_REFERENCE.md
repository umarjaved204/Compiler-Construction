# Quick Reference Card

## Lexical Analyzer - Quick Commands

### Most Important Commands

```powershell
# 1. Compile everything (PowerShell)
.\compile.ps1

# 2. Run all tests (PowerShell)
.\test.ps1

# 3. Test single file with Manual Scanner
cd src
java ManualScanner ..\tests\test1.lang

# 4. Test single file with JFlex Scanner
cd src
java Yylex ..\tests\test1.lang
```

### Batch File Alternatives (CMD)

```cmd
compile.bat
test.bat
```

---

## File Locations

| Component | Location |
|-----------|----------|
| Java Source Files | `src/` |
| Test Files | `tests/*.lang` |
| Documentation | `docs/` |
| JFlex Tool | `jflex-1.9.1/lib/` |
| Build Scripts | Root directory |

---

## Token Format

```
<TOKEN_TYPE, "lexeme", Line: X, Col: Y>
```

Example: `<KEYWORD, "start", Line: 1, Col: 1>`

---

## Key Files

### Implementation
- `ManualScanner.java` - DFA-based scanner
- `Scanner.flex` - JFlex specification
- `Token.java` - Token class
- `TokenType.java` - Token types
- `ErrorHandler.java` - Error handling
- `SymbolTable.java` - Symbol table

### Testing
- `test1.lang` - Valid tokens
- `test2.lang` - Complex expressions
- `test3.lang` - Strings/escape sequences
- `test4.lang` - Error cases
- `test5.lang` - Comments

### Documentation
- `README.md` - Main documentation
- `GETTING_STARTED.md` - Setup guide
- `PROJECT_SUMMARY.md` - Complete overview
- `SUBMISSION_CHECKLIST.md` - Pre-submission checklist

---

## Language Basics

### Keywords (12)
```
start finish loop condition else
declare input output function return
break continue
```

### Identifiers
- Start with uppercase: `Count`, `Variable_name`
- Max 31 characters

### Operators
- Arithmetic: `+ - * / % **`
- Relational: `== != < > <= >=`
- Logical: `&& || !`
- Assignment: `= += -= *= /=`
- Inc/Dec: `++ --`

### Comments
- Single: `## comment`
- Multi: `#* comment *#`

---

## Common Tasks

### Generate JFlex Scanner
```powershell
cd src
java -jar ..\jflex-1.9.1\lib\jflex-full-1.9.1.jar Scanner.flex
```

### Compile All Java Files
```powershell
cd src
javac *.java
```

### Compare Outputs
```powershell
cd src
java ManualScanner ..\tests\test1.lang > manual.txt
java Yylex ..\tests\test1.lang > jflex.txt
Compare-Object (Get-Content manual.txt) (Get-Content jflex.txt)
```

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Java not found | Install JDK from adoptium.net |
| Class not found | Run `javac *.java` in src/ |
| JFlex not working | Check path to jflex jar file |
| Tests failing | Verify compilation succeeded |

---

## Statistics Output Includes

- Total tokens
- Lines processed  
- Comments removed
- Token counts by type
- Symbol table entries
- Error count (if any)

---

## Before Submission

1. ☐ Install Java
2. ☐ Run `compile.ps1`
3. ☐ Run `test.ps1`
4. ☐ Create PDFs for Automata_Design and Comparison
5. ☐ Update team names in README.md
6. ☐ Verify folder structure
7. ☐ Create ZIP file

---

## Need Help?

1. Read `GETTING_STARTED.md`
2. Check `SUBMISSION_CHECKLIST.md`
3. Review `PROJECT_SUMMARY.md`
4. Look at test outputs for examples

---

**Quick Start:** Run `compile.ps1` then `test.ps1`

**Most Useful:** `GETTING_STARTED.md` and `SUBMISSION_CHECKLIST.md`
