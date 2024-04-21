@echo off

rem Default output file
set "output_file=output.hzip"

rem Parse command line arguments
set "input_file=test.txt"
set "show_stats=0"
setlocal enabledelayedexpansion
for /l %%i in (1,2,%#%) do (
    if "%~1"=="-f" (
        set "input_file=%~2"
        shift
        shift
    ) else if "%~1"=="-o" (
        set "output_file=%~2"
        shift
        shift
    ) else if "%~1"=="-s" (
        set "show_stats=1"
        shift
    ) else (
        echo "Invalid option: %~1"
        echo "Usage: compress.bat -f myfile.txt [-o myfile.hzip -s]"
        endlocal
        exit /b 1
    )
)

rem Check if the input file was specified
if not defined input_file (
    echo "You must specify an input file using the -f option."
    echo "Usage: compress.bat -f myfile.txt [-o myfile.hzip -s]"
    endlocal
    exit /b 1
)

rem Compile Compress.java if necessary
javac Compress.java

rem Run the Compress program with the specified input and output files
java Compress -f %input_file% -o %output_file%

rem Optionally display statistics if -s is specified
if %show_stats%==1 (
    echo "Displaying statistics..."
    rem Add code here to display statistics if needed
)

endlocal
