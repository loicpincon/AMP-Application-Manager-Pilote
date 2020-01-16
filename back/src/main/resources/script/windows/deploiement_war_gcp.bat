@echo on
set mypath=%cd%
set pathToFolderTemporaire=%1
set pathToFolderTemporaireOldNAme=%2
set pathToFolderTemporaireWithNewFile=%3

cd %pathToFolderTemporaire%
MKDIR "%pathToFolderTemporaire%/extract"
cd "%pathToFolderTemporaire%/extract"
"%JAVA_HOME%/jar" -xvf %pathToFolderTemporaireOldNAme%
cd "%pathToFolderTemporaire%"
COPY /Y "gcp.properties" "%pathToFolderTemporaire%/extract/WEB-INF/classes/gcp.properties"
cd "%pathToFolderTemporaire%/extract"
"%JAVA_HOME%/jar" -cf %pathToFolderTemporaireWithNewFile% "%pathToFolderTemporaire%/extract"
cd %mypath%