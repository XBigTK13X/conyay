#! /bin/bash

NAME=$1
OUT=$2

rm -rf work
mkdir work
sed "s/---NAME---/$NAME/g" < launch4j.template > work/windows_launch4j.xml
/usr/local/bin/launch4j/launch4j work/windows_launch4j.xml 
cp work/$NAME.exe $OUT/$NAME.exe
