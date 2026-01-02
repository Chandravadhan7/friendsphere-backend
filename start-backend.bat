@echo off
echo Starting Social Media Backend on localhost:8080...
cd /d %~dp0
mvn spring-boot:run
pause
