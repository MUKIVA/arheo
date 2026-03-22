@echo off
if not exist "%~2" mkdir "%~2"
tar -xzf "%~1" -C "%~2"
