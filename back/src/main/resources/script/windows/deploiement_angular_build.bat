@echo on
set mypath=%cd%
set pathToFolderTemporaire=%1
set urlGit=%2
set folderName=%3

cd %pathToFolderTemporaire% &&  git clone %urlGit% . &&  npm i &&   ng build --prod && pause 
cd %mypath%