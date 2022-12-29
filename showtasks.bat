call runcrud
if "%ERRORLEVEL%" == "0" goto browser
echo.
echo RUNCRUD has errors – breaking work
goto fail

:browser
start chrome http://localhost:8080/crud/v1/tasks
goto end

:fail
echo.
echo There was errors.

:end
echo.
echo Go to your website :)