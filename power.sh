NAME=$1
if [ "$#" == "0" ]; then
    echo "No arguments provided"
    exit 1
fi

JRE_VERSION="1.7.0_40"
OUT="rap"
rm -rf $OUT
mkdir $OUT
for f in `ls build`; do
  if [ -d JRE/$JRE_VERSION/$f ]; then
    LINE=$(head -$((${RANDOM} % `wc -l < lyrics` + 1)) lyrics | tail -1)
    echo "$LINE $f"
    mkdir $OUT/$f
    mkdir $OUT/$f/core
    mkdir $OUT/$f/core/JRE
    
    cp -r JRE/$JRE_VERSION/$f/* $OUT/$f/core/JRE
    WD=`pwd`
    cd drivers/$f
    bash build.sh $1 $WD/$OUT/$f
    cd ../..
  fi
done
