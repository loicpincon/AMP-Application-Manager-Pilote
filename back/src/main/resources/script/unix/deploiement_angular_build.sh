#!/bin/bash
mypath=$(pwd)
pathToFolderTemporaire=$1
urlGit=$2
version=$3
cd $pathToFolderTemporaire &&  git clone -b $version  $urlGit . &&  npm i &&   ng build --prod 

cd $mypath
