'''
BTC ANALYST in Python

'''

#!/usr/bin/env python

#Import libraries 
import numpy as np 
import matplotlib.pyplot as plt
import pandas as pd 

from pylab import plot,show

#Create dataSets from CSVs
prices = pd.read_csv('allMkt30d.csv')
volume = pd.read_csv('allVol30d.csv')
tradepm = pd.read_csv('tpmhrs30d.csv')
newerData = pd.read_csv('price30d.csv')

#Create DataFrames (np.ndarray)
pdat     = prices.iloc[:,1:10].values
voldat   = volume.iloc[:,1:10].values
tradedat = tradepm.iloc[:,1:10].values
dates = prices.iloc[:,0].values
priceParams = prices.iloc[0,:].values
volParams   = volume.iloc[0,:].values
tradeParams = tradepm.iloc[0,:].values

#Get the Dimensions of the data sets 
print(' *** Dimensions of Data Sets *** ')
print('Price Data Points:')
print(pdat.shape)
print('Price Parameters:')
print(priceParams.shape)
print('Volume Data Points:')
print(voldat.shape)
print('Volume Parameters:')
print(volParams.shape)
print('Trades-Per-Minute Data Points:')
print(tradedat.shape)
print('Trades/Minute Parameters:')
print(tradeParams.shape)
print('Time Series Data Points: ')
print(dates.shape)


# Isolate history of individual markets for each dataset
# Starting with columns of Price Data  
price = {}
rp0 = pdat[:,[0]] 
rp1 = pdat[:,[1]]
rp2 = pdat[:,[2]]
rp3 = pdat[:,[3]]
rp4 = pdat[:,[4]]
rp5 = pdat[:,[5]]
rp6 = pdat[:,[6]]
rp7 = pdat[:,[7]]
rp8 = pdat[:,[8]]
price[0] = rp0
price[1] = rp1 
price[2] = rp2
price[3] = rp3
price[4] = rp4
price[5] = rp5
price[6] = rp6
price[7] = rp7
price[8] = rp8
# Isolate columns of Volume Data
vol = {}
rv0 = voldat[:,[0]]
rv1 = voldat[:,[1]]
rv2 = voldat[:,[2]]
rv3 = voldat[:,[3]]
rv4 = voldat[:,[4]]
rv5 = voldat[:,[5]]
rv6 = voldat[:,[6]]
rv7 = voldat[:,[7]]
rv8 = voldat[:,[8]]
vol[0] = rv0
vol[1] = rv1
vol[2] = rv2
vol[3] = rv3
vol[4] = rv4
vol[5] = rv5
vol[6] = rv6
vol[7] = rv7
vol[8] = rv8
# Isolate columns of TradesPerMin Data
tpm = {}
rt0 = tradedat[:,[0]]
rt1 = tradedat[:,[1]]
rt2 = tradedat[:,[2]]
rt3 = tradedat[:,[3]]
rt4 = tradedat[:,[4]]
rt5 = tradedat[:,[5]]
rt6 = tradedat[:,[6]]
rt7 = tradedat[:,[7]]
rt8 = tradedat[:,[8]]
tpm[0] = rt0
tpm[1] = rt1
tpm[2] = rt2
tpm[3] = rt3
tpm[4] = rt4 
tpm[5] = rt5
tpm[6] = rt6
tpm[7] = rt7
tpm[8] = rt8

print("- Extracted 9 Columns of Price data ["+str(len(rp1)*9) +" points]")
print("- Extracted 9 Columns of Volume data ["+str(len(rv0)*9)+" points]")
print("- Extracted 9 Columns of Trades/Min data ["+str(len(rt0)*9)+" points]")
'''
Make sure the same market is being plotted across different data sets with
dataSetParamMap()
'''


def dataSetParamMap(dataset):
    paramMap = {}
    index = 0 
    for param in dataset:
        paramMap[param] = index
        index+=1 
    return paramMap
    
    
#Illustrate the data sets 
fig, axes = plt.subplots(figsize = (10,10),nrows = 3, ncols = 1)
prices.plot(ax = axes[0])
volume.plot(ax = axes[1])
tradepm.plot(ax = axes[2])
#Add Labels 
axes[0].set_title('30d Bitcoin Data')
axes[0].set_ylabel('Prices')
axes[1].set_ylabel('Volume')
axes[2].set_ylabel('Trades/Minute')
axes[2].set_xlabel('Hours (Shared X-axis)')
plt.show()
plt.close()

#Illustrating information about particular markets
# starting with the first one arbitrarily 

def AnalyzeMarket(mkt):
    fig , axes = plt.subplots(figsize = (10,10), nrows = 3, ncols =1)
    axes[0].set_title('30d '+mkt+' data')
    axes[0].set_ylabel('Price')
    axes[1].set_ylabel('Volume')
    axes[2].set_ylabel('Trades/Minute')
    axes[2].set_xlabel('Hours (Shared Axis)')
    plt.subplot.(311)
    #Isolate user defined DataSets to Plot
    plt.plot(price[dataSetParamMap(priceParams)[mktName]])
    plt.subplot(312)
    plt.plot(vol[dataSetParamMap(volParams)[mktName]])
    plt.subplot(313)
    plt.plot(tpm[dataSetParamMap(tradeParams)[mktName]])
    plt.show()


print('Which Market would you like to Analyze?')
mktName = str(input())
print(' You selected'+mktName)
AnalyzeMarket(mktName)
    
