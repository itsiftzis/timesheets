#!/usr/bin/env bash
# Onorio Catenacci 3 January 2014
 
echo "installing java7"
add-apt-repository ppa:webupd8team/java
apt-get -y -q update > /dev/null 2>&1
echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
apt-get -y -q install oracle-java7-installer > /dev/null 2>&1
 
echo "Installing scala and setting it up . . . "
 
apt-get update > /dev/null 2>&1
apt-get install -y scala > /dev/null 2>&1
 
# Thanks to pardigmatic on StackOverflow for this procedure to install
# SBT via apt-get on Ubuntu
# See here: http://stackoverflow.com/questions/13711395/install-sbt-on-ubuntu
 
echo "Installing SBT . . ."
debfile="repo-deb-build-0002.deb"
wget http://apt.typesafe.com/"$debfile" > /dev/null 2>&1
dpkg -i "$debfile" > /dev/null 2>&1
apt-get update > /dev/null 2>&1
apt-get install -y sbt > /dev/null 2>&1

basevim="/home/vagrant/.vim"
 
echo "Setting up VIM for syntax highlighting"
#Create directories for vim syntax highlighting then fetch the files from github
#Credit where credit's due, one liner is from blog posting by Bruce Snyder
#http://bsnyderblog.blogspot.com/2012/12/vim-syntax-highlighting-for-scala-bash.html
 
mkdir -p "$basevim"/{ftdetect,indent,syntax} && for d in ftdetect indent syntax ; do wget --no-check-certificate -O "$basevim"/$d/scala.vim https://raw.github.com/scala/scala-dist/master/tool-support/src/vim/$d/scala.vim; done > /dev/null 2>&1
 
rm -f "$debfile"

sudo su -
echo "Setting up mongo"
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/mongodb.list
sudo apt-get update > /dev/null 2>$1
sudo apt-get install -y mongodb-org
sudo service mongod restart
export LC_ALL=C