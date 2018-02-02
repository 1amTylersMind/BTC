
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
