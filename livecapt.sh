#!/bin/bash
cd BtcDataLib
javac DataCollector.java
javac DataCleaner.java
javac Market.java
javac Orderbook.java
chmod +x orders.py
#chmod +x spyderNoteBook.py
for i in {1..5}
	do
	touch orderbook.txt
	java DataCollector > orderbook.txt
	echo 'OrderBooks Captured and saved to:'
	echo "$PWD./orderbook$i.txt"
	java DataCleaner
	touch results.txt
	python orders.py > results.txt
	cat results.txt
	# Now if i>0 start backprop. by analyzing results  
	sleep 10
	mv orderbook.txt "orderbook"$i".txt"
	mv results.txt "result"$i".txt"
done
chmod +x back.py
python back.py
