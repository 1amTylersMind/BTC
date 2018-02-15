#!/bin/bash
cd BtcDataLib
javac DataCollector.java
javac DataCleaner.java
javac Market.java
javac Orderbook.java
chmod +x orders.py
chmod +x spyderNoteBook.py
for i in {1..2}
	do
	touch orderbook.txt
	java DataCollector > orderbook.txt
	echo 'OrderBooks Captured and saved to:'
	echo "$PWD./orderbook$i.txt"
	java DataCleaner
	touch results.txt
	python orders.py > results.txt
	cat results.txt
	sleep 10
	mv orderbook.txt "orderbook"$i".txt"
	mv results.txt "result"$i".txt"
done
