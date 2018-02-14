#!/bin/bash
cd BtcDataLib
javac DataCollector.java
javac DataCleaner.java
javac Market.java
javac Orderbook.java
chmod +x orders.py
chmod +x spyderNoteBook.py
for i in {1..10}
	do
	touch orderbook.txt
	java DataCollector > orderbook.txt
	echo 'OrderBooks Captured and saved to:'
	echo "$PWD./orderbook$i.txt"
	java DataCleaner
	python orders.py
	sleep 10
	mv orderbook.txt "orderbook"$i".txt"
done
