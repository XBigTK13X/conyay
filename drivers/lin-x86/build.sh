NAME=$1
OUT=$2
sed 's/---NAME---/$NAME/g' ".desktop" > $OUT/.desktop
