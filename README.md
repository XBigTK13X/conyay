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

If that fails, then pull in the following 32-bit libraries
sudo apt-get install libc6:i386 libgcc1:i386 gcc-4.6-base:i386 libstdc++5:i386 libstdc++6:i386

Ubuntu dependencies one-liner
sudo apt-get install zip maven git

Manually downloading and configuring the latest JDK7 is recommended
Set JAVA_HOME in /etc/environment
