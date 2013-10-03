conyay
======

Wraps java apps in platform specific launchers.

Usage
=====
power.sh accepts two arguments, the name of the game to wrap and where its contents are located.

The contents for sps-gamelib are found in $project/pkg after running the packaging prep script.

As an example, here is how Munchoid would be packaged.

./power.sh Munchoid ~/dev/nnue/pkg

More Info
=========
This has only ever been run on Ubuntu.

conyay expects launch4j to be installed under /usr/local/bin/launch4j

If you see errors about windres not being found, then run this:
sudo apt-get install ia32-libs

You will need to install maven and JDK7 for the launcher to compile.
