#!/bin/bash
# For data capt, need to go to src folder
echo 'Beginning Test Collection. [N=3]'
# Begin live capt
for i in {1..2}
    do 
    cd .. 
    cd src  
    javac DataCollector.java
    touch orderbook.txt
    java DataCollector > orderbook.txt
    echo "Saved orderbook to $PWD./orderbook$i.txt"
    mv orderbook.txt "orderbook"$i".txt"
    mv "orderbook"$i".txt" /projects/BTC/Theory
    cd ..
    cd Theory
    # Now Try and Make a Prediction 
    javac MarkovBook.java
    java MarkovBook "$i"
    # check if previous predictions have been made
    sleep 13
done
