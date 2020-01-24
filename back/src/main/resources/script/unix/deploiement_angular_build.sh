#!/bin/bash
mypath=$(pwd)
pathToFolderTemporaire=$1
urlGit=$2

cd $pathToFolderTemporaire &&  git clone $urlGit . &&  npm i &&   ng build --prod 

cd $mypath
