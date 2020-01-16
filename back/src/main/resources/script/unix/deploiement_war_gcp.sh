#!/bin/bash
mypath=$(pwd)
pathToFolderTemporaire=$1
pathToFolderTemporaireOldNAme=$2
pathToFolderTemporaireWithNewFile=$3
cd $pathToFolderTemporaire
MKDIR $pathToFolderTemporaire"/extract"
cd $pathToFolderTemporaire"/extract"
"$JAVA_HOME/jar" -xvf $pathToFolderTemporaireOldNAme
cd $pathToFolderTemporaire
cp "gcp.properties" "$pathToFolderTemporaire/extract/WEB-INF/classes/gcp.properties"
"$JAVA_HOME/jar" -cf $pathToFolderTemporaireWithNewFile "$pathToFolderTemporaire/extract"
cd $mypath
