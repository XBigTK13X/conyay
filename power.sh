NAME=$1
if [ "$#" == "0" ]; then
    echo "No arguments provided"
    exit 1
fi

function tunes(){
  echo "    ♪♪♪ $1 ♪♪♪"
}

JRE_VERSION="1.7.0_40"
OUT="rap"

tunes "I’m living in that 21st Century, doing something mean to it"
rm -rf $OUT
mkdir $OUT
mkdir $OUT/work

tunes "Do it better than anybody you ever seen do it"
cd launcher
mvn -q clean package
cd ..

tunes "Screams from the haters, got a nice ring to it"
for f in `ls build`; do
  LINE=$(head -$((${RANDOM} % `wc -l < lyrics` + 1)) lyrics | tail -1)
  tunes "$LINE $f"
    
  mkdir $OUT/$f
  mkdir $OUT/$f/core
 
  if [ -d JRE/$JRE_VERSION/$f ]; then
    mkdir $OUT/$f/core/JRE
    
    cp -r JRE/$JRE_VERSION/$f/* $OUT/$f/core/JRE
    WD=`pwd`
    cd build/$f
    bash build.sh $1 $WD/$OUT/$f
    cd ../..
  fi
done

tunes "No one man should have all that power!"
