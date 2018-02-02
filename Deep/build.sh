#!/bin/bash
# replace Old data with the latest data
cd /media/root/UNTITLED/BTC1.2/BTC/src/DataSets
echo 'Processing Historic Data'
ls | egrep './csv'$
sleep 1
rm allMkt30d.csv
rm allVol30d.csv
rm tpmhrs30d.csv
wget -o allMkt30d.csv http://data.bitcoinity.org/export_data.csv?c=e&currency=USD&data_type=price&r=hour&t=l&timespan=30d
wget -o allVol30d.csv http://data.bitcoinity.org/export_data.csv?c=e&currency=USD&data_type=volume&hour&r=hour&t=l&timespan=30d
wget -o tpmhrs30d.csv http://data.bitcoinity.org/export_data.csv?c=e&currency=USD&data_type=tradespm&hour&r=hour&t=l&timespan=30d

javac HistoricDataCollector.java
javac Maya.java
#javac HiddenLayerTwo.java
# All three data sets are treated as generic obj bc they are taken over same time intervals
#java HistoricDataCollector /root/Desktop/Deep ./allMkt30d.csv ./allVol30d.csv ./tpmhrs30d.csv
# ^Not using java for now while developing python visuals 

#Visualize/Process Historic Data
cd ~/Desktop/Deep
chmod +x script.py
python script.py 
echo 'Ready to start capturing Live Data'

#Start Capturing live orderbook data 
chmod +x dataCapt.sh
# ./dataCapt.sh 
