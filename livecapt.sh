#!/bin/bash
cd src
javac DataCollector.java
javac DataCleaner.java
chmod +x orders.py
for i in {1..5}
	do
	touch "orderbook"$i".txt"
	java DataCollector > "orderbook"$i".txt"
	echo 'OrderBooks Captured and saved to:'
	echo "$PWD./orderbook$i.txt"
	java DataCleaner
	sleep 1
	python orders.py
	sleep 10
done
