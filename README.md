# Custom Programming Language - Lexical Analyzer

## CS4031 Compiler Construction - Assignment 01

---

## Language Overview

**Language Name:** CustomLang  
**File Extension:** `.lang`

CustomLang is a custom programming language designed for educational purposes in compiler construction. It features a simple, readable syntax with support for basic programming constructs including variables, loops, conditions, functions, and various data types.

---

## Keywords

CustomLang has 12 reserved keywords (case-sensitive, exact match):

| Keyword | Description |
|---------|-------------|
| `start` | Marks the beginning of a program |
| `finish` | Marks the end of a block (program, loop, condition, function) |
| `loop` | Initiates a loop structure |
| `condition` | Conditional statement (if) |
| `else` | Alternative branch in conditional |
| `declare` | Variable declaration |
| `output` | Print output |
| `input` | Read input |
| `function` | Function definition |
| `return` | Return from function |
| `break` | Exit from loop |
| `continue` | Skip to next loop iteration |

---

## Identifiers

**Rules:**
- Must start with an uppercase letter (A-Z)
- Followed by lowercase letters (a-z), digits (0-9), or underscores (_)
- Maximum length: 31 characters
- Case-sensitive

**Regular Expression:** `[A-Z][a-z0-9_]{0,30}`

**Valid Examples:**
- `Count`
- `Variable_name`
- `X`
- `Total_sum_2024`
- `Result123`

**Invalid Examples:**
- `count` (starts with lowercase)
- `Variable` (contains uppercase after first character)
- `2Count` (starts with digit)
- `myVariable` (starts with lowercase)

---

## Literals

### Integer Literals
**Format:** Optional sign followed by one or more digits  
**Regex:** `[+-]?[0-9]+`

**Examples:**
- `42`
- `+100`
- `-567`
- `0`

### Floating-Point Literals
**Format:** Optional sign, digits, decimal point, 1-6 decimal digits, optional exponent  
**Regex:** `[+-]?[0-9]+\.[0-9]{1,6}([eE][+-]?[0-9]+)?`

**Examples:**
- `3.14`
- `+2.5`
- `-0.123456`
- `1.5e10`
- `2.0E-3`

**Invalid:**
- `3.` (no decimal digits)
- `.14` (no digits before decimal)
- `1.2345678` (more than 6 decimal digits)

### String Literals
**Format:** Enclosed in double quotes with support for escape sequences  
**Regex:** `"([^"\\\n]|\\["\\\ntr])*"`

**Escape Sequences:**
- `\"` - Double quote
- `\\` - Backslash
- `\n` - Newline
- `\t` - Tab
- `\r` - Carriage return

**Examples:**
- `"Hello, World!"`
- `"Line 1\nLine 2"`
- `"Path: C:\\Users\\Documents"`
- `"She said \"Hello\""`

### Character Literals
**Format:** Single character enclosed in single quotes  
**Regex:** `'([^'\\\n]|\\['\\\ntr])'`

**Examples:**
- `'A'`
- `'\n'`
- `'\t'`
- `'\''`
- `'\\'`

### Boolean Literals
**Values:** `true`, `false` (case-sensitive)

---

## Operators

### Arithmetic Operators (Left to Right)
| Operator | Description | Precedence |
|----------|-------------|------------|
| `**` | Exponentiation | 1 (Highest) |
| `*` | Multiplication | 2 |
| `/` | Division | 2 |
| `%` | Modulus | 2 |
| `+` | Addition | 3 |
| `-` | Subtraction | 3 |

### Relational Operators
| Operator | Description |
|----------|-------------|
| `==` | Equal to |
| `!=` | Not equal to |
| `<` | Less than |
| `>` | Greater than |
| `<=` | Less than or equal to |
| `>=` | Greater than or equal to |

### Logical Operators
| Operator | Description |
|----------|-------------|
| `&&` | Logical AND |
| `||` | Logical OR |
| `!` | Logical NOT |

### Assignment Operators
| Operator | Description |
|----------|-------------|
| `=` | Simple assignment |
| `+=` | Add and assign |
| `-=` | Subtract and assign |
| `*=` | Multiply and assign |
| `/=` | Divide and assign |

### Increment/Decrement Operators
| Operator | Description |
|----------|-------------|
| `++` | Increment by 1 |
| `--` | Decrement by 1 |

---

## Punctuators

| Symbol | Usage |
|--------|-------|
| `(` `)` | Function calls, expressions |
| `{` `}` | Reserved for future use |
| `[` `]` | Array indexing, function parameters |
| `,` | Separator |
| `;` | Statement terminator (optional) |
| `:` | Reserved for future use |

---

## Comments

### Single-Line Comments
**Syntax:** `## comment text`  
Everything from `##` to end of line is ignored.

**Example:**
```
declare X = 10  ## This is a comment
```

### Multi-Line Comments
**Syntax:** `#* comment text *#`  
Everything between `#*` and `*#` is ignored.

**Example:**
```
#*
   This is a multi-line comment
   spanning multiple lines
*#
```

---

## Sample Programs

### Sample 1: Hello World
```
## Simple Hello World program
start
    output "Hello, World!"
finish
```

### Sample 2: Simple Calculator
```
## Calculator with basic operations
start
    declare Num1 = 10
    declare Num2 = 5
    
    declare Sum = Num1 + Num2
    declare Diff = Num1 - Num2
    declare Prod = Num1 * Num2
    declare Quot = Num1 / Num2
    
    output "Sum: "
    output Sum
    output "Difference: "
    output Diff
    output "Product: "
    output Prod
    output "Quotient: "
    output Quot
finish
```

### Sample 3: Factorial Function
```
## Factorial calculation using recursion
start
    function Factorial[N]
        condition N <= 1
            return 1
        finish
        return N * Factorial[N - 1]
    finish
    
    declare Number = 5
    declare Result = Factorial[Number]
    output "Factorial of "
    output Number
    output " is "
    output Result
finish
```

### Sample 4: Loop Example
```
## Sum of first N natural numbers
start
    declare N = 10
    declare Sum = 0
    declare I = 1
    
    loop I <= N
        Sum += I
        I++
    finish
    
    output "Sum of first "
    output N
    output " numbers is "
    output Sum
finish
```

### Sample 5: Conditional Example
```
## Check if number is positive, negative, or zero
start
    declare Number = -5
    
    condition Number > 0
        output "Positive"
    else
        condition Number < 0
            output "Negative"
        else
            output "Zero"
        finish
    finish
finish
```

---

## Compilation and Execution

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- JFlex 1.8.2 or higher

### Compiling the Manual Scanner

```powershell
# Navigate to src directory
cd src

# Compile all Java files
javac *.java

# Run Manual Scanner
java ManualScanner ../tests/test1.lang
```

### Generating and Running JFlex Scanner

```powershell
# Navigate to src directory
cd src

# Generate JFlex scanner
jflex Scanner.flex

# Compile generated and existing Java files
javac *.java

# Run JFlex scanner
java Yylex ../tests/test1.lang
```

### Running All Tests

```powershell
# Test Manual Scanner on all test files
cd src
foreach ($file in Get-ChildItem ../tests/*.lang) {
    Write-Host "Testing $($file.Name) with ManualScanner"
    java ManualScanner $file.FullName
    Write-Host "---"
}

# Test JFlex Scanner on all test files
foreach ($file in Get-ChildItem ../tests/*.lang) {
    Write-Host "Testing $($file.Name) with JFlex"
    java Yylex $file.FullName
    Write-Host "---"
}
```

---

## Project Structure

```
22i1234-22i5678-A/
├── src/
│   ├── ManualScanner.java      # Manual DFA-based scanner
│   ├── Token.java              # Token class
│   ├── TokenType.java          # Token type enumeration
│   ├── SymbolTable.java        # Symbol table
│   ├── ErrorHandler.java       # Error handling
│   ├── Scanner.flex            # JFlex specification
│   └── Yylex.java              # Generated JFlex scanner
├── docs/
│   ├── Automata_Design.pdf     # NFA/DFA diagrams
│   ├── README.md               # This file
│   ├── LanguageGrammar.txt     # Grammar specification
│   └── Comparison.pdf          # Scanner comparison
├── tests/
│   ├── test1.lang              # All valid tokens
│   ├── test2.lang              # Complex expressions
│   ├── test3.lang              # Strings/chars with escapes
│   ├── test4.lang              # Lexical errors
│   ├── test5.lang              # Comments
│   └── TestResults.txt         # Test results summary
└── README.md                   # Root readme
```

---

## Team Members

- Student 1: [Roll Number - e.g., 22i-1234]
- Student 2: [Roll Number - e.g., 22i-5678]

---

## Features Implemented

### Manual Scanner (Part 1)
- ✓ DFA-based token recognition
- ✓ All token types from specification
- ✓ Longest match principle
- ✓ Operator precedence
- ✓ Pre-processing (whitespace removal)
- ✓ Line and column tracking
- ✓ Token output with details
- ✓ Statistics generation
- ✓ Symbol table

### JFlex Scanner (Part 2)
- ✓ Complete JFlex specification
- ✓ Macro definitions
- ✓ All lexical rules
- ✓ Pattern matching priority
- ✓ Compatible Token class

### Error Handling (Part 3)
- ✓ Invalid character detection
- ✓ Malformed literal detection
- ✓ Invalid identifier detection
- ✓ Unclosed comment detection
- ✓ Detailed error reporting
- ✓ Error recovery

---

## Testing

The project includes 5 comprehensive test files:

1. **test1.lang**: Tests all valid token types
2. **test2.lang**: Tests complex expressions and nested structures
3. **test3.lang**: Tests strings and characters with escape sequences
4. **test4.lang**: Tests error detection and recovery
5. **test5.lang**: Tests comment handling

Run all tests to verify scanner correctness.

---

## Known Limitations

- Multi-line comments cannot be nested
- String literals cannot span multiple lines
- Maximum identifier length is 31 characters
- Floating-point decimals limited to 6 digits

---

## References

- JFlex Manual: https://jflex.de/manual.html
- Compiler Construction Principles
- Course Materials CS4031

---

## License

This project is submitted as part of CS4031 Compiler Construction course assignment.

---

*Last Updated: February 18, 2026*
