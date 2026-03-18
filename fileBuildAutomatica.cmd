for /f "delims=" %%i in (.env) do set %%i
call mvn clean compile
call mvn exec:java