#!/bin/bash
# For data capt, need to go to src folder
cd .. 
cd src
echo 'Beginning Test Collection. [N=3]'
# Begin live capt
for i in {1..2}
    do 
    touch orderbook.txt
    java DataCollector > orderbook.txt
    echo "Saved orderbook to $PWD./orderbook$i.txt"
    mv orderbook.txt "orderbook"$i".txt"
    mv "orderbook"$i".txt" /projects/BTC/Theory
    sleep 13
done
# Now Analyze the Orderbooks gathered. 
cd ..
cd Theory
javac MarkovBook.java
java MarkovBook 2