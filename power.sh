JRE_VERSION="1.7.0_40"
OUT="rap"
rm -rf $OUT
mkdir $OUT
for f in `ls drivers`; do
  if [ -d JRE/$JRE_VERSION/$f ]; then
    echo "No one man should have all that power! $f"
    mkdir $OUT/$f
    mkdir $OUT/$f/core
    mkdir $OUT/$f/core/JRE
    cp -r JRE/$JRE_VERSION/$f/* $OUT/$f/core/JRE
    cd drivers/$f
    ./build.sh
    cd ../..
  fi
done
