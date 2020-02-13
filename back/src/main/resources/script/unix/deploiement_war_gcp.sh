#!/bin/bash
mypath=$(pwd)
pathToFolderTemporaire=$1
pathToFolderTemporaireOldNAme=$2
pathToFolderTemporaireWithNewFile=$3
nameFileParam=$4
cd $pathToFolderTemporaire
rm -rf "$pathToFolderTemporaire/extract"
mkdir "$pathToFolderTemporaire/extract"
cd $pathToFolderTemporaire"/extract"
jar -xvf $pathToFolderTemporaireOldNAme
cd $pathToFolderTemporaire
cp "$pathToFolderTemporaire/$nameFileParam" "$pathToFolderTemporaire/extract/WEB-INF/classes/$nameFileParam"
jar -cf $pathToFolderTemporaireWithNewFile "$pathToFolderTemporaire/extract"
cd $mypath
