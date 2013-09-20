NAME=$1
if [ "$#" == "0" ]; then
    echo "No arguments provided. Usage: power.sh gameTitle pkgDir"
    exit 1
fi

TITLE=$1
PKG=$2

function tunes(){
  echo "    ♪♪♪ $1 ♪♪♪"
}

JRE_VERSION="1.7.0_40"
OUT="rap"

tunes "I’m living in that 21st Century, doing something mean to it"
rm -rf $OUT
mkdir $OUT
mkdir $OUT/core

tunes "Do it better than anybody you ever seen do it"
cd launcher
mvn -q clean package
cd ..
cp launcher/target/conyay-launcher-jar-with-dependencies.jar $OUT/core/launcher.jar
cp -r $PKG/* $OUT/core/ 

tunes "Screams from the haters, got a nice ring to it"
for f in `ls build`; do
  LINE=$(head -$((${RANDOM} % `wc -l < lyrics` + 1)) lyrics | tail -1)
  tunes "$f: $LINE"
  
  LOCALCORE=$(cat build/$f/core)
 
  mkdir $OUT/$f
  mkdir --parents $OUT/$f/$LOCALCORE
 
  if [ -d JRE/$JRE_VERSION/$f ]; then
    mkdir $OUT/$f/core/JRE
    
    cp -r JRE/$JRE_VERSION/$f/* $OUT/$f/core/JRE
    WD=`pwd`
  fi

  cd build/$f
  bash build.sh $TITLE $WD/$OUT/$f
  cd ../..
  cp -r $OUT/core/* $OUT/$f/$LOCALCORE/ 
done

rm -rf $OUT/core

tunes "No one man should have all that power!"
