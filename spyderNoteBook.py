#!/usr/bin/env python2
# -*- coding: utf-8 -*-
"""
The Question we are seeking to answer is:
Trades/Min(Price*Volume) = ? 

But really these three variables are actually
data sets containing information about 10 different
variables.

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


################################ FUNCTIONS ####################################


'''
ANALYZE SINGLE MARKET
'''
def analyzeSingleMarket(colNum,mktType):
	data = []
	if(mktType == 1):
        # Price Data 
            return pdat[:,colNum]
	if(mktType == 2):
        # Volume data
            return vdat[:,colNum]
	if(mktType == 3):
        # Trading Activity
            return tdat[:,colNum]		
	else:
        		return data


'''
SIGMOID
'''
def sigmoid(x):
	return 1/(1+math.exp(-x))


'''
NORMALIZE A ONE DIMENSIONAL DATASET
'''
def normalizeFlattenedDataSet(data):
	norm = []
	for term in data:
		norm.append(sigmoid(term))
	return norm


'''
GET MARKET PARAMETERS
'''
def getMarketParameters(fname):
	params = []
	with open(fname) as f:
		reader = csv.reader(f)
		params = next(reader)
	return params


############################# END OF FUNCTIONS ################################

# Three Main Historic DataSets [Price][Volume][Volatility]
pdat = pd.read_csv('allMkt30d.csv').iloc[:,1:].values
vdat = pd.read_csv('allVol30d.csv').iloc[:,1:].values
tdat = pd.read_csv('tpmhrs30d.csv').iloc[:,1:].values
dates = pd.read_csv('allMkt30d.csv').iloc[:,0].values

# Get String params associated w/ Data Frames 
priceParams = getMarketParameters('allMkt30d.csv')
volParams   = getMarketParameters('allvol30d.csv')
tradeParams = getMarketParameters('tpmhrs30d.csv')

# Get the 30 Day moving average Data Frames
mavgPrice = pd.DataFrame(pd.ewma(pdat,span=624))
mavgVol = pd.DataFrame(pd.ewma(vdat,span=624))
mavgTrade = pd.DataFrame(pd.ewma(tdat,span=624))

# Three Latest Data Sets  - Data Frames 
p24dat = pd.read_csv('price24.csv').iloc[:,1:].values
v24dat = pd.read_csv('volume24.csv').iloc[:,1:].values
t24dat = pd.read_csv('trades24.csv').iloc[:,1:].values

# Get the Params for 24hr Data Frames
p24params = getMarketParameters('price24.csv')
v24params = getMarketParameters('volume24.csv')
t24params = getMarketParameters('trades24.csv')

# Get the 24hr Moving Avg 
mavgP24 = pd.DataFrame(pd.ewma(p24dat, span=360))
mavgV24 = pd.DataFrame(pd.ewma(v24dat, span=360))
mavgT24 = pd.DataFrame(pd.ewma(t24dat, span=360))

# First going to consider the algorithm for 3 Mkts. 
cbP = pd.DataFrame(analyzeSingleMarket(priceParams.index('coinbase'),1))
cbV = pd.DataFrame(analyzeSingleMarket(volParams.index('coinbase'),2))
cbT = pd.DataFrame(analyzeSingleMarket(tradeParams.index('coinbase'),3))
# Do the same for Bitfinex
bxP = pd.DataFrame(analyzeSingleMarket(priceParams.index('bitfinex'),1))
bxV = pd.DataFrame(analyzeSingleMarket(volParams.index('bitfinex'),2))
bxT = pd.DataFrame(analyzeSingleMarket(tradeParams.index('bitfinex'),3))
# Do BitStamp last 
bsP = pd.DataFrame(analyzeSingleMarket(priceParams.index('bitstamp'),1))
bsV = pd.DataFrame(analyzeSingleMarket(volParams.index('bitstamp'),2))
bsT = pd.DataFrame(analyzeSingleMarket(tradeParams.index('bitstamp'),3))
#Compare Coinbase with Bitfinex
cbp_bxp = cbP.corrwith(bxP,'index')[0]
cbv_bxv = cbV.corrwith(bxV,'index')[0]
cbt_bxt = cbT.corrwith(bxT,'index')[0]
# compare Coinbase w/ itself 
cbp_cbv = cbP.corrwith(1/cbV,'index')[0]
cbv_cbt = cbV.corrwith(cbT,'index')[0]
cbp_cbtv = cbP.corrwith(cbT/cbV,'index')[0]

# I'm guessing Bitstamp correlates MORE with CB than BFX
cbp_bsp = cbP.corrwith(bsP,'index')[0]
cbv_bsv = cbP.corrwith(bsV,'index')[0]
cbt_bst = cbT.corrwith(bsT,'index')[0]
# compare Bitfinex w/ itself 
bxp_bxv = bxP.corrwith(bxV,'index')[0]

# Print the results of the comparisons
print('CB Price <-> BFX Price: %f %s' % (cbp_bxp,"%"))
print('CB Vol <-> BFX Vol: %f %s' % (cbv_bxv,"%"))
print('CB Trades/Min <-> BFX Trades/Min: %f %s' % (cbt_bxt,"%"))
print('---- Comparing Coinbase Stats w/ itself ----')
print('CB Price <-> CB Vol: %f %s' % (cbp_cbv,"%"))
print('CB Vol <-> CB Trades/Min: %f %s' % (cbv_cbt,"%"))
print('CB Price <-> CB [Trades/Volume]: %f %s' %(cbp_cbtv,"%"))
print('---- Comparing CoinBase with BitStamp ----')
print('CB Price <-> BS Price: %f %s' % (cbp_bsp,'%'))
print('CB Vol <-> BS Vol: %f %s' % (cbv_bsv,'%'))
print('CB Trading <-> BS Trading: %f %s' % (cbt_bst,'%'))


def investigateCorr(df1,df2,label1,label2):
    plt.figure(1)
    plt.plot(df1,label=label1)
    plt.plot(df2,label=label2)
    plt.show()
    return 0 


# Also Need to Define a Cost Function! 
# Minimize the corr of diffs of df's, e.g: find downhill
    