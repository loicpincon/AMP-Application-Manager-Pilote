@echo on
set mypath=%cd%
set pathToFolderTemporaire=%1
set urlGit=%2
set version=%3

cd %pathToFolderTemporaire% &&  git clone -b %version% %urlGit% . && npm i && ng build --prod  && goto :endPause
pause
:endPause 
cd %mypath%


