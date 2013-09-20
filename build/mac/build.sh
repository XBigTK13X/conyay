#! /bin/bash
NAME=$1
OUT=$2

cp -r bundle.app $OUT/bundle.app
rm $OUT/bundle.app/Contents/Resources/Java/.placeholder
