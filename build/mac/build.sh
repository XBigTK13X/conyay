#! /bin/bash
NAME=$1
OUT=$2

sed "s/---NAME---/$NAME/g" < Info.plist.template > bundle.app/Contents/Info.plist
cp -r bundle.app $OUT

