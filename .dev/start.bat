@echo off
setlocal enabledelayedexpansion

:: Initialize variables
set "SCRIPT_DIR=%~dp0"
set "DOCKER_COMPOSE_FILE="
set "rebuild=false"
set "frontend=false"
set "backend=false"

:: Parse command line arguments
call :parse_args %*

call :start_development
if errorlevel 1 (
    call :clean_up
)

endlocal
goto :eof

:: Start docker containers function
:start_docker_containers
if "%rebuild%"=="true" (
    echo Rebuilding the containers...
    docker-compose -f "%SCRIPT_DIR%\%DOCKER_COMPOSE_FILE%" up --build -d
) else (
    docker-compose -f "%SCRIPT_DIR%\%DOCKER_COMPOSE_FILE%" up -d
)
goto :eof

:: starts db running in container
:: starts server running with watch mode
:start_backend
    echo Starting Database for backend development.
    set DOCKER_COMPOSE_FILE=server-docker-compose.yml

    call :start_docker_containers

    cd %SCRIPT_DIR%
    cd ..
    call gradlew.bat -t build -x test -i
goto :eof

:: starts server and db running in containers
:: starts frontend in dev mode
:start_frontend
    echo Starting Database and server for frontend development.
    set DOCKER_COMPOSE_FILE=ui-docker-compose.yml

    call :start_docker_containers

    cd %SCRIPT_DIR%
    cd ..\ui
    call npm run dev
goto :eof

:: Start development function
:start_development
if "%backend%"=="true" (
    call :start_backend
) else if "%frontend%"=="true" (
    call :start_frontend
)

:: Clean up function
:clean_up
echo.
echo shutting down docker containers
docker compose -f "%SCRIPT_DIR%\%DOCKER_COMPOSE_FILE%" down
goto :eof

:: Parse command line arguments
:parse_args
if "%~1"=="" goto :args_done

if "%~1"=="-r" (
    set "rebuild=true"
) else if "%~1"=="-f" (
    set "frontend=true"
) else if "%~1"=="-b" (
    set "backend=true"
) else if "%~1"=="-h" (
    echo -f = frontend (start db + server)
    echo -b = backend (start db only)
    echo -r = rebuild local containers
    goto :eof
)
shift
goto :parse_args

:args_done
%@Try%
    call :start_development
%@EndTry%
:@Catch
    call :clean_up
:@EndCatch
)

endlocal
