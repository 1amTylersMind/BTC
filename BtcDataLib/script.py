#!/usr/bin/env python

#Import libraries 
import numpy as np 
import matplotlib.pyplot as plt
import pandas as pd 

from pylab import plot,show

#Create dataSets from CSVs
prices = pd.read_csv('price24.csv')
volume = pd.read_csv('volume24.csv')
tradepm = pd.read_csv('trades24.csv')
#newerData = pd.read_csv('price30d.csv')

#Create DataFrames (np.ndarray)
pdat     = prices.iloc[:,1:].values
voldat   = volume.iloc[:,1:].values
tradedat = tradepm.iloc[:,1:].values
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
rp0 = pdat[:,[0]] 
rp1 = pdat[:,[1]]
rp2 = pdat[:,[2]]
rp3 = pdat[:,[3]]
rp4 = pdat[:,[4]]
rp5 = pdat[:,[5]]
rp6 = pdat[:,[6]]
rp7 = pdat[:,[7]]
rp8 = pdat[:,[8]]
#Do Derivatives 
rp0prime = pd.DataFrame(rp0)

# Isolate columns of Volume Data
rv0 = voldat[:,[0]]
rv1 = voldat[:,[1]]
rv2 = voldat[:,[2]]
rv3 = voldat[:,[3]]
rv4 = voldat[:,[4]]
rv5 = voldat[:,[5]]
rv6 = voldat[:,[6]]
rv7 = voldat[:,[7]]
rv8 = voldat[:,[8]]
# Isolate columns of TradesPerMin Data
rt0 = tradedat[:,[0]]
rt1 = tradedat[:,[1]]
rt2 = tradedat[:,[2]]
rt3 = tradedat[:,[3]]
rt4 = tradedat[:,[4]]
rt5 = tradedat[:,[5]]
rt6 = tradedat[:,[6]]
rt7 = tradedat[:,[7]]
rt8 = tradedat[:,[8]]
print("- Extracted 9 Columns of Price data ["+str(len(rp1)*9) +" points]")
print("- Extracted 9 Columns of Volume data ["+str(len(rv0)*9)+" points]")
print("- Extracted 9 Columns of Trades/Min data ["+str(len(rt0)*9)+" points]")
#the 6mo data isnt loading correctly 
#print('6 Month CoinBase History Data Points:')
#print(cbdat.shape)



#Illustrate the data sets 
fig, axes = plt.subplots(figsize = (10,10),nrows = 3, ncols = 1)
prices.plot(ax = axes[0])
volume.plot(ax = axes[1])
tradepm.plot(ax = axes[2])
#Add Labels 
axes[0].set_title('24hr Bitcoin Data')
axes[0].set_ylabel('Prices')
axes[1].set_ylabel('Volume')
axes[2].set_ylabel('Trades/Minute')
axes[2].set_xlabel('Hours (Shared X-axis)')
#plt.show()

#Illustrating information about particular markets
# starting with the first one arbitrarily 
plt.figure(2)
plt.subplot(311)
plt.subplot(311).set_title('24Hr Market Summary: Bitfinex')
plt.plot(rp0,label='Price')
#plt.plot(rp0prime,label='dp/dt')
#plt.plot(rp0,rp0prime,label='Price/derivative') 
plt.subplot(311).set_ylabel('Price 24Hr')
plt.subplot(312)
plt.plot(rv0,label='Vol')
plt.subplot(312).set_ylabel('Volume 24Hr')
plt.subplot(313)
plt.plot(rt0,label='Trades/Min')
plt.subplot(313).set_ylabel('Trades/Min 24Hr')

#Now the derivs 
#plt.plot(rp0prime.diff(periods=24, axis=0))
show()

