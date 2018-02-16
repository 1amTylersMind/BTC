#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
Created on Fri Feb 16 19:05:17 2018
   # BACK PROPOGATION 
    '''Look at past guesses in the results.txt files, 
      and compare with the corresponding orderbook.txt files
      then try to calc sums to be able to start comparing with
      Historic P,V,T data. 
      Thinking about Markov 
     |##############################|
     |# p0->R1->g0->p1->R2->g1>... #|
     |##############################|
    '''
  
"""
# Imports 
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import csv
import math

def checkPreviousResults(fname):
    # First try to get old orderbooks
    try:
        f1 = open(fname,'r')
        p1 = []
        t1 = []
        index1 = 0
        for line in f1:
            if(index1>2):
                if(',' in line):
                    p1.append(float(line.split(",")[0]))
                    t1.append(float(line.split(",")[1]))
            index1 += 1
    except:
        print('Could not find orderbook1.txt')
    P1 = pd.DataFrame(p1)
    T1 = pd.DataFrame(t1)
    R1 = pd.concat([P1,T1],axis=1)
    return R1


def checkLastGuess(fname):
    price0 = 0
    guess0 = 0 
    try:
        g1 = open(fname,'r')
        for line in g1:
            if('Last' in line):
                price0 = float(line.split('$')[1])
            if('Guess' in line):
                guess0 = float(line.split('$')[1])
    except:
        print('File Not Found!')

    return price0, guess0

def main():
    # Check if any old orderbooks are present
    R1 = checkPreviousResults('orderbook1.txt')
    p0, g0 = checkLastGuess('result1.txt')
    R2 = checkPreviousResults('orderbook2.txt')
    p1, g1 = checkLastGuess('result2.txt')
    R3 = checkPreviousResults('orderbook3.txt')
    p2, g2 = checkLastGuess('result3.txt')
    R4 = checkPreviousResults('orderbook4.txt')
    p3, g3 = checkLastGuess('result4.txt')
    R5 = checkPreviousResults('orderbook5.txt')
    p4, g4 = checkLastGuess('result4.txt')
    
    priceHistory = pd.DataFrame([p0,p1,p2,p3,p4])
    guesses = pd.DataFrame([g0,g1,g2,g3,g4])
    err = pd.DataFrame(np.array(np.subtract(np.array(priceHistory),np.array(guesses))))
    chain = pd.concat([priceHistory,guesses,err],axis=1)
    
if __name__ == '__main__':
    main()