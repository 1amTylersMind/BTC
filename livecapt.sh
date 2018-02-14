#!/bin/bash
pwd
javac DataCollector.java
javac DataCleaner.java
chmod +x orders.py
for i in {1..5}
	do
	touch orderbook1.txt
	java DataCollector > orderbook1.txt
	echo 'OrderBooks Captured and saved to:'
	echo "$PWD./orderbook$i.txt"
	java DataCleaner
	sleep 1
	python orders.py
	sleep 10
	mv orderbook1.txt "orderbook"$i".txt"
done
