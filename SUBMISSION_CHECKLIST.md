# Submission Checklist

## Before Submitting - Complete These Tasks

### 1. Install Java ☐
- [ ] Download and install JDK 8 or higher
- [ ] Verify installation: `java -version` and `javac -version`
- [ ] Add Java to system PATH if needed

### 2. Test Compilation ☐
- [ ] Run `compile.ps1` or `compile.bat`
- [ ] Verify no compilation errors
- [ ] Check that both ManualScanner.class and Yylex.class are created

### 3. Run All Tests ☐
- [ ] Run `test.ps1` or `test.bat`
- [ ] Verify all 5 tests pass
- [ ] Check that outputs look correct
- [ ] Confirm error detection in test4.lang

### 4. Manual Testing ☐
- [ ] Test ManualScanner on each test file individually
- [ ] Test Yylex on each test file individually
- [ ] Verify outputs are identical
- [ ] Check symbol table generation
- [ ] Verify error reporting

### 5. Update Documentation ☐
- [ ] Edit README.md - add team member names and roll numbers
- [ ] Edit PROJECT_SUMMARY.md - add team information
- [ ] Review all documentation for accuracy
- [ ] Check for any placeholder text that needs updating

### 6. Create PDF Documents ☐
- [ ] Convert `docs/Automata_Design.txt` to PDF
  - Draw NFA diagrams using JFLAP, draw.io, or hand-drawn
  - Include all 7 token types
  - Add DFA transition tables
  - Format professionally
  
- [ ] Convert `docs/Comparison.txt` to PDF
  - Format tables nicely
  - Include output comparisons
  - Add performance graphs if possible
  - Professional formatting

### 7. Organize Folder Structure ☐
- [ ] Create submission folder: `22iXXXX-22iYYYY-A/`
  (Replace XXXX and YYYY with actual roll numbers)
  
- [ ] Copy/Move these folders into submission folder:
  - [ ] src/ (with all .java files and Scanner.flex)
  - [ ] tests/ (with all .lang files and TestResults.txt)
  - [ ] docs/ (with PDFs: Automata_Design.pdf, Comparison.pdf, LanguageGrammar.txt)
  - [ ] README.md at root
  - [ ] jflex-1.9.1/ (optional, for convenience)

### 8. Final Structure Verification ☐

Expected structure:
```
22iXXXX-22iYYYY-A/
├── src/
│   ├── ManualScanner.java
│   ├── Token.java
│   ├── TokenType.java
│   ├── SymbolTable.java
│   ├── ErrorHandler.java
│   ├── Scanner.flex
│   └── Yylex.java (generated)
├── docs/
│   ├── Automata_Design.pdf      ← Must be PDF!
│   ├── README.md
│   ├── LanguageGrammar.txt
│   └── Comparison.pdf           ← Must be PDF!
├── tests/
│   ├── test1.lang
│   ├── test2.lang
│   ├── test3.lang
│   ├── test4.lang
│   ├── test5.lang
│   └── TestResults.txt
└── README.md
```

Verify:
- [ ] All required files present
- [ ] PDFs are properly formatted
- [ ] No extra/unnecessary files
- [ ] Folder named correctly with both roll numbers

### 9. Code Quality Check ☐
- [ ] All Java files compile without warnings
- [ ] Code is properly commented
- [ ] No debugging print statements left in code
- [ ] Code follows Java naming conventions
- [ ] Indentation is consistent

### 10. Documentation Review ☐
- [ ] README.md is complete and accurate
- [ ] All sample programs are tested
- [ ] Grammar specification is correct
- [ ] Automata diagrams are clear and correct
- [ ] Comparison document shows thorough analysis

### 11. Test Coverage Verification ☐

Verify each test file works correctly:

- [ ] **test1.lang**: All valid tokens recognized
  - Expected: ~287 tokens, 0 errors
  
- [ ] **test2.lang**: Complex expressions handled
  - Expected: ~198 tokens, 1 intentional error
  
- [ ] **test3.lang**: Escape sequences working
  - Expected: ~156 tokens, 0 errors
  
- [ ] **test4.lang**: Errors detected correctly
  - Expected: ~45 tokens, 18 errors
  
- [ ] **test5.lang**: Comments handled properly
  - Expected: ~89 tokens, 23 comments removed

### 12. Comparison Verification ☐
- [ ] Run both scanners on each test file
- [ ] Verify outputs are identical (or document differences)
- [ ] Compare token counts
- [ ] Compare error detection
- [ ] Compare symbol tables

### 13. Performance Testing ☐
- [ ] Measure execution time for both scanners
- [ ] Document performance metrics
- [ ] Include in Comparison.pdf

### 14. Create Archive ☐
- [ ] Compress folder to ZIP: `22iXXXX-22iYYYY-A.zip`
- [ ] Verify ZIP file can be extracted correctly
- [ ] Test extracted files to ensure everything works

### 15. Final Review ☐
- [ ] All team members reviewed the submission
- [ ] Tested on a clean machine if possible
- [ ] All requirements from assignment document met
- [ ] No plagiarism or copied code
- [ ] Proper citations if any external resources used

### 16. Submission ☐
- [ ] Submit through designated platform
- [ ] Verify submission was successful
- [ ] Keep a backup copy
- [ ] Note submission timestamp

---

## Quick Commands for Testing

### Compile Everything
```powershell
.\compile.ps1
```

### Run All Tests
```powershell
.\test.ps1
```

### Test Individual File
```powershell
cd src
java ManualScanner ..\tests\test1.lang
java Yylex ..\tests\test1.lang
```

### Compare Outputs
```powershell
cd src
java ManualScanner ..\tests\test1.lang > manual.txt
java Yylex ..\tests\test1.lang > jflex.txt
Compare-Object (Get-Content manual.txt) (Get-Content jflex.txt)
```

---

## Common Issues and Solutions

### Issue: Java not found
**Solution:** Install JDK and add to PATH
- Download from: https://adoptium.net/
- Add to PATH: System Properties → Environment Variables

### Issue: JFlex not working
**Solution:** Use full path to JFlex
```powershell
java -jar "C:\Path\To\jflex-1.9.1\lib\jflex-full-1.9.1.jar" Scanner.flex
```

### Issue: Class not found errors
**Solution:** Compile all files
```powershell
cd src
javac *.java
```

### Issue: Different outputs between scanners
**Solution:** Check pattern priority and regex patterns
- Verify Scanner.flex matches ManualScanner.java logic
- Check for off-by-one errors in line/column numbers

---

## Assignment Grading Rubric Checklist

### Part 1: Manual Scanner (60 marks)
- [ ] Automata Design (15 marks)
  - [ ] Regular expressions documented
  - [ ] 7 NFA diagrams with clear states
  - [ ] 7 minimized DFAs with transition tables
  
- [ ] Token Recognition (25 marks)
  - [ ] All token types recognized
  - [ ] DFA-based matching
  - [ ] Longest match principle
  - [ ] Operator precedence correct
  
- [ ] Pre-processing (5 marks)
  - [ ] Whitespace removal
  - [ ] Line/column tracking
  
- [ ] Token Output (5 marks)
  - [ ] Correct format
  - [ ] All information included
  
- [ ] Statistics (5 marks)
  - [ ] Token counts
  - [ ] Lines processed
  - [ ] Comments counted
  
- [ ] Symbol Table (5 marks)
  - [ ] Identifiers stored
  - [ ] All required information

### Part 2: JFlex Implementation (30 marks)
- [ ] JFlex Specification (20 marks)
  - [ ] Complete specification
  - [ ] Macro definitions
  - [ ] All lexical rules
  - [ ] Pattern priority
  
- [ ] Token Class (5 marks)
  - [ ] Compatible with both scanners
  
- [ ] Comparison (5 marks)
  - [ ] Side-by-side outputs
  - [ ] Performance comparison
  - [ ] Analysis of differences

### Part 3: Error Handling (10 marks)
- [ ] Error Types (5 marks)
  - [ ] Invalid characters
  - [ ] Malformed literals
  - [ ] Invalid identifiers
  - [ ] Unclosed comments
  
- [ ] Error Reporting (3 marks)
  - [ ] Proper format
  - [ ] Clear messages
  
- [ ] Error Recovery (2 marks)
  - [ ] Continues scanning
  - [ ] Reports all errors

---

## Before Final Submission

### Double-Check These Critical Items:
1. ✓ Team member names and roll numbers updated
2. ✓ Both Automata_Design and Comparison are PDFs (not .txt)
3. ✓ Folder named correctly: 22iXXXX-22iYYYY-A
4. ✓ All 5 test files present and working
5. ✓ Both scanners produce identical output
6. ✓ Error handling works correctly
7. ✓ Symbol table populates correctly
8. ✓ Documentation is complete
9. ✓ Code compiles without errors
10. ✓ Submission package is complete

---

## Estimated Time for Final Preparation

- Installing Java: 15-30 minutes
- Testing compilation: 10 minutes
- Running all tests: 15 minutes
- Creating PDFs: 1-2 hours
- Updating documentation: 30 minutes
- Final review: 30 minutes
- **Total: ~3-4 hours**

Plan accordingly!

---

## Contact Information

If you encounter issues:
1. Review GETTING_STARTED.md
2. Check PROJECT_SUMMARY.md
3. Review error messages carefully
4. Test on a different machine
5. Contact course instructor/TA

---

**Good luck with your submission! 🎓**
