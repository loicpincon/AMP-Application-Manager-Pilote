#!/bin/bash
mypath=$(pwd)
pathToFolderTemporaire=$1
pathToFolderTemporaireOldNAme=$2
pathToFolderTemporaireWithNewFile=$3
cd $pathToFolderTemporaire
rm -rf "$pathToFolderTemporaire/extract"
mkdir "$pathToFolderTemporaire/extract"
cd $pathToFolderTemporaire"/extract"
jar -xvf $pathToFolderTemporaireOldNAme
cd $pathToFolderTemporaire
cp "gcp.properties" "$pathToFolderTemporaire/extract/WEB-INF/classes/gcp.properties"
sudo jar -cf $pathToFolderTemporaireWithNewFile "$pathToFolderTemporaire/extract"
cd $mypath
