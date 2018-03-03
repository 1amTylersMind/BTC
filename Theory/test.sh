#!/bin/bash
# For data capt, need to go to src folder
echo 'Beginning Test Collection. [N=5]'
# Begin live capt
for i in {1..5}
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
    touch prediction.txt
    javac MarkovBook.java  
    java MarkovBook "$i" > prediction.txt 
    cat prediction.txt
    mv prediction.txt "prediction"$i".txt"
    # Find the best fit between these predictions
    # That will represent an estimate of the next Minute
    # (1:1 for collection/prediction time)
    sleep 13
done
