#Prep the historic data 
echo 'Processing Historic Data'
ls | egrep './csv'$
sleep 1
cd /media/root/UNTITLED/BTC1.2/BTC/src/DataSets
javac HistoricDataCollector.java
javac Maya.java
#javac HiddenLayerTwo.java
# All three data sets are treated as generic obj bc they are taken over same time intervals
java HistoricDataCollector /root/Desktop/Deep ./allMkt30d.csv ./allVol30d.csv ./tpmhrs30d.csv

#Visualize/Process Historic Data
cd ~/Desktop/Deep
chmod +x script.py
python script.py 
echo 'Ready to start capturing Live Data'
# Make DataCapt separate .sh to do in || w .py's?
chmod +x dataCapt.sh
# ./dataCapt.sh 
