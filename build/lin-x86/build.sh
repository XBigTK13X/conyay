NAME=$1
OUT=$2
sed 's/---NAME---/$NAME/g' ".desktop" > $OUT/.desktop
cp icon.png $OUT/icon.png
cp run.sh $OUT/$NAME.sh
