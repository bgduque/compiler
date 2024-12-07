# Java Mini Compiler

## Overview
The Java Mini Compiler is a simple Java application that allows users to write, analyze, and execute basic Java code. It features a graphical user interface (GUI) built using Java Swing, providing functionalities for lexical, syntax, and semantic analysis of Java code.

## Project Structure
```
JavaMiniCompiler
├── src
│   ├── JavaMiniCompiler.java        # Main entry point of the application
│   ├── HomePage.java                # GUI for the home page
│   ├── MainPage.java                # Main functionality of the compiler
│   ├── actions
│   │   ├── OpenFile.java            # Handles file opening functionality
│   │   ├── PerformLexicalAnalysis.java # Performs lexical analysis on the code
│   │   ├── PerformSemanticAnalysis.java # Performs semantic analysis on the code
│   │   └── PerformSyntaxAnalysis.java   # Performs syntax analysis on the code
│   └── utils
│       ├── Token.java               # Represents a token in lexical analysis
│       └── ZoomUtils.java           # Utility methods for zooming functionality
├── .gitignore                       # Specifies files to be ignored by Git
└── README.md                        # Documentation for the project
```

## Features
- **Lexical Analysis**: Analyzes the code and generates tokens.
- **Syntax Analysis**: Checks the syntax of the code and identifies errors.
- **Semantic Analysis**: Validates variable assignments and checks for type mismatches.
- **File Operations**: Open and read Java files into the code editor.
- **Zoom Functionality**: Adjust the font size in the code editor for better readability.

## Setup Instructions
1. Clone the repository to your local machine.
2. Navigate to the project directory.
3. Compile the Java files using a Java compiler.
4. Run the `JavaMiniCompiler` class to start the application.

## Usage
- Upon launching, the home page will display a welcome message and a button to start the compiler.
- Users can open Java files, write code in the editor, and perform various analyses using the provided buttons.
- Results of the analyses will be displayed in the results area of the GUI.

## Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue for any suggestions or improvements.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.