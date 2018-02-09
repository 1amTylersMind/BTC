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
import numpy as np
# import matplotlib.pyplot as plt
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
pdat     = pd.read_csv('allMkt30d.csv').iloc[:,1:].values
vdat   = pd.read_csv('allVol30d.csv').iloc[:,1:].values
tdat = pd.read_csv('tpmhrs30d.csv').iloc[:,1:].values
dates = pd.read_csv('allMkt30d.csv').iloc[:,0].values

# Get String params associated w/ Data Frames 
priceParams = getMarketParameters('allMkt30d.csv')
volParams   = getMarketParameters('allvol30d.csv')
tradeParams = getMarketParameters('tpmhrs30d.csv')

# Get the 30 Day moving average Data Frames
mavgPrice = pd.ewma(pdat,span=624)
mavgVol = pd.ewma(vdat,span=624)
mavgTrade = pd.ewma(tdat,span=624)

# Three Latest Data Sets  - Data Frames 
p24dat = pd.read_csv('price24.csv').iloc[:,1:].values
v24dat = pd.read_csv('volume24.csv').iloc[:,1:].values
t24dat = pd.read_csv('trades24.csv').iloc[:,1:].values

# Get the Params for 24hr Data Frames
p24params = getMarketParameters('price24.csv')
v24params = getMarketParameters('volume24.csv')
t24params = getMarketParameters('trades24.csv')

# Get the 24hr Moving Avg 
mavgP24 = pd.ewma(p24dat, span=360)
mavgV24 = pd.ewma(v24dat, span=360)
mavgT24 = pd.ewma(t24dat, span=360)