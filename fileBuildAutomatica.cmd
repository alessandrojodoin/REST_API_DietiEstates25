for /f "delims=" %%i in (.env) do set %%i
mvn exec:java