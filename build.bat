@echo off
setlocal enabledelayedexpansion
title SIREN Home Proxy - Build Debug APK

:start_build
echo.
echo  ====================================================
echo   SIREN Home Proxy - Build Debug APK
echo  ====================================================
echo.
echo  Starting Android Gradle build process...
echo.

:: Use 'call' so the script continues running after gradlew finishes
call .\gradlew.bat assembleDebug

echo.
echo  ====================================================
echo   Build process completed.
echo  ====================================================
echo.

if exist ".\app\build\outputs\apk\debug\app-debug.apk" (
    echo   APK Location: app\build\outputs\apk\debug\app-debug.apk
    echo.
)

:ask_restart
set "RESTART_CHOICE="
set /p "RESTART_CHOICE=  Do you want to go again? [1 = Go Again, 2 = Exit]: "
if /i "!RESTART_CHOICE!"=="1" (
    cls
    goto :start_build
) else if /i "!RESTART_CHOICE!"=="2" (
    exit
) else (
    echo   [ERROR] Invalid choice. Please enter 1 or 2.
    goto :ask_restart
)