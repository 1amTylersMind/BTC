 #!/usr/bin/env bash
cd /media/root/UNTITLED/BTC1.2/BTC/src/DataSets
 # Alt, to get Data from script for updating CSVs: 
wget -o last30d.csv http://data.bitcoinity.org/export_data.csv?c=e&currency=USD&data_type=price&r=hour&t=l&timespan=30d
wget -o lastVol.csv http://data.bitcoinity.org/export_data.csv?c=e&currency=USD&data_type=volume&hour&r=hour&t=l&timespan=30d
wget -o activity.csv http://data.bitcoinity.org/export_data.csv?c=e&currency=USD&data_type=tradespm&hour&r=hour&t=l&timespan=30d

echo 'Creating a Recurrent Neural Network based on the following data sets: '
ls | egrep '\.csv'$
sleep 1
javac Maya.java
javac HistoricDataCollector.java
# All three data sets are same objects/col over same time intervals 
java HistoricDataCollector /projects/BTC/src/DataSets ./allMkt30d.csv ./allVol30d.csv ./tpmhrs30d.csv 
java HistoricDataCollector /projects/BTC ./last30d.csv ./lastVol.csv ./activity.csv

#java Historic DataCollector /projects/BTC ./last30d.csv ./lastVol.csv ./activity.csv
#java Maya /projects/BTC/src/DataSets ./naddrs2yr.csv sentiment
# Paths will be slightly different for other machine  
