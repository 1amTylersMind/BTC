#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""

Created on Thu Feb  8 15:07:26 2018
@author: Scott Robbins
@date: 2/8/2017
"""
# Imports 
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import csv
import math


def csvDataDump(data):
    f = open('trades.csv', "w")
    i = 0
    for line in data:
        if(i>2):
            f.write(line)
        i += 1
    


def getContextualData():
    f = open('orderbook.txt', "r")
    index = 0
    datadump = []
    for line in f:
        if(index == 0):
        	datadump.append(line.split(' bitfinex')[0])
        if(index == 1):
            datadump.append(line.split(' coinbase')[0])
        if(index == 2):
            datadump.append(line.split(' bitstamp')[0])
        index += 1
    f = open('context.csv', "w")
    for ln in datadump:
        f.write(ln + '\n')


def processTrades(orders):
    pdat = orders.iloc[:,0].values
    vdat = orders.iloc[:,1].values
    runnable = False
    if(len(pdat) == len(vdat)):
        print('Correct Dimensions for Trading Analysis')
        runnable = True
    else:
        print('Data has Incorrect Dimensions')
    if(runnable):
        pNorm = 0
        vNorm = 0
        for price in pdat:
            pNorm += price **2
        pNorm = 1/math.sqrt(pNorm)
        for vol in vdat:
            vNorm += vol**2
        vNorm = 1/math.sqrt(vNorm)
        print('Price Norm: %f \nVol Norm: %f' % (pNorm,vNorm))
        pWeights = []
        vWeights = []
        for price in pdat:
            pWeights.append(price*pNorm)
        for vol in vdat:
            vWeights.append(vol*vNorm)
        p = []
        v = []
        for pr in pdat:
            p.append(pr*pNorm)
        for vo in vdat:
            v.append(vo*vNorm)
        P = np.array(p)
        V = np.array(v)
        
        return P,V

def main():
    f = open('orderbook.txt', 'r')
    index = 0
    bfxStat = []
    cbStats = []
    bsStats = []
    trades = []
    for line in f:
        if(index == 0):
            bfxStat.append(line)
        if(index == 1):
            cbStats.append(line)
        if(index == 2):
            bsStats.append(line)
        if(',' in line):
            trades.append(line)
    index += 1
    # Now dump the Trade Data into a CSV for Analysis
    csvDataDump(trades)
    # Ok, So it is writing to the csv, but gotta clean it up.
    getContextualData()
    # Now Start making predictions 
    context = pd.read_csv('context.csv')
    trades = pd.read_csv('trades.csv')
    # get rid of brackets in context data! 
    # Analyze/Normalize trades 
    p, v  = processTrades(trades)
    pdat = pd.DataFrame(p)
    vdat = pd.DataFrame(v)
    #Ok let's start making the master matrix 
    pdat = pd.DataFrame(p)
    pdat[1] = v
    pdat[2] = trades.iloc[:,0].values
    pdat[3] = trades.iloc[:,1].values
    # Use pdat to isolate the most important trades 
    #makeInitialPredictions(pdat)
    print(pdat[1].mean)
    meanV = pdat[1].mean()
    maxV = pdat[1].max()
    predictions = {}
    index = 0
    for t in pdat[1]:
        if(t>meanV and t<=maxV):
            predictions[t] = pdat[2].get_value(index)
        index += 1
    print('ESTIMATING NEXT PRICE AT: $%f' % predictions[maxV])


if __name__ == '__main__':
    main()
