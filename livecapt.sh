#!/bin/bash
cd src
javac DataCollector.java
javac DataCleaner.java

echo "Dir is $PWD"

for i in {1..5}
	do
	touch "orderbook"$i".txt"
	java DataCollector > "orderbook"$i".txt"
	echo 'OrderBooks Captured and saved to:'
	echo "$PWD./orderbook$i.txt"
	java DataCleaner
	sleep 15
done

