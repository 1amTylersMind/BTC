#!/bin/bash 
#pip install numpy
#pip install matplotlib
#pip install --upgrade keras 
#pip install pandas
#pip install -U scikit-learn
#Now use external memory bc live image is running low 
#cd /media/root/UNTITLED/
#mkdir BTC1.2
#cd BTC1.2
#git clone https://www.github.com/TylersDurden/BTC
cd /media/root/UNTITLED/BTC1.2/BTC/src/Datasets

cp allMkt30d.csv ~/Desktop/Deep
cp allVol30d.csv ~/Desktop/Deep
cp tpmhrs30d.csv ~/Desktop/Deep
cd ~/Desktop/Deep
chmod +x script.py
python script.py
