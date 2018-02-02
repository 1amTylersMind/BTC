#!/bin/bash 
pip install numpy
pip install matplotlib
pip install --upgrade keras 
pip install pandas
pip install -U scikit-learn
#Now use external memory bc live image is running low 
cd /media/root/UNTITLED/BTC1.2
rm BTC
git clone https://www.github.com/TylersDurden/BTC
cd /media/root/UNTITLED/BTC1.2/BTC/Deep
chmod +x build.sh 
su root ./build.sh 

