#!/bin/bash 
pip install numpy
pip install matplotlib
pip install --upgrade keras 
pip install pandas
pip install -U scikit-learn
#Now use external memory bc live image is running low 
cd /media/root/UNTITLED/
#mkdir BTC1.2
cd BTC1.2
#git clone https://www.github.com/TylersDurden/BTC
cd /media/root/UNTITLED/BTC1.2/BTC/src/Datasets
#cp allMkt30d.csv ~/Desktop/Deep
#cp allVol30d.csv ~/Desktop/Deep
#cp tpmhrs30d.csv ~/Desktop/Deep
# Don't Need some steps after first run
cd ~/Desktop/Deep

#Prep the historic data 
echo 'Processing Historic Data'
ls | egrep './csv'$
sleep 1
cd /media/root/UNTITLED/BTC1.2/BTC/src/DataSets
javac HistoricDataCollector.java
javac Maya.java
#javac HiddenLayerTwo.java
# All three data sets are treated as generic obj bc they are taken over same time intervals
#java HistoricDataCollector /root/Desktop/Deep ./allMkt30d.csv ./allVol30d.csv ./tpmhrs30d.csv

#Visualize/Process Historic Data
cd ~/Desktop/Deep
chmod +x script.py
python script.py 
echo 'Ready to start capturing Live Data'
# Make DataCapt separate .sh to do in || w .py's?



# To Quickly Grab some live data: 
cd /media/root/UNTITLED/BTC1.2/BTC/src
javac DataCollector.java
cd Network
javac DataTrainingLayer.java
javac BookKeeper.java
javac HistoricDataCollector.java
cd ..
for number in {1..5}
do
touch dat.txt
touch results.txt
java DataCollector > dat.txt
echo 'Live Orderbook Data Captured'
echo 'Data Logged to ./dat.txt'
cd Network
java DataTrainingLayer orderbook /media/root/UNTITLED/BTC1.2/BTC/src ./dat.txt
#remove the data after it's been processed 
cd .. 
sleep 35s
rm dat.txt
done 

