@echo off
setlocal

REM Change the current directory to the script's directory
cd /d "%~dp0"

REM Check if bin directory exists, if not, create it.
if not exist "bin" (
    echo Creating bin directory...
    mkdir "bin"
)

REM Compile the java files
echo Compiling source files...
javac -encoding UTF-8 -d bin src/*.java

REM Check if compilation was successful
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b %errorlevel%
)

echo Starting game...
java -cp bin AvoidGame

endlocal