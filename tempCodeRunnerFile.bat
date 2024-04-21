@echo off
setlocal

rem Set the default output file and option to display statistics
set "output_file=output.hzip"
set "show_stats=0"

rem Parse command line arguments
set "input_file=test.txt"
for /f "tokens=1,2 delims=:" %%a in ('%*') do (
    if /i "%%a" == "-f" set "input_file=%%b"
    if /i "%%a" == "-o" set "output_file=%%b"
    if /i "%%a" == "-s" set "show_stats=1"
)