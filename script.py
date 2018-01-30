#!/usr/bin/env python

#Import libraries 
import numpy as np 
import matplotlib.pyplot as plt
import pandas as pd 

def isolateColumnData(data,col):
	index = 0
	result = list()
	for row in data:
		result.append(row.pop(col))		
		index =+1
	return result



#Create dataSets from CSVs
prices = pd.read_csv('allMkt30d.csv')
volume = pd.read_csv('allVol30d.csv')
tradepm = pd.read_csv('tpmhrs30d.csv')
newerData = pd.read_csv('price30d.csv')

#Create DataFrames (np.ndarray)
pdat     = prices.iloc[:,1:10].values
voldat   = prices.iloc[:,1:10].values
tradedat = prices.iloc[:,1:10].values

p = pdat.tolist()
v = voldat.tolist()
t = tradedat.tolist()

#Get the Dimensions of the data sets 
print('Price Data Points:')
print(pdat.shape)
print('Volume Data Points:')
print(voldat.shape)
print('Trades-Per_Minute Data Points:')
print(tradedat.shape)
#the 6mo data isnt loading correctly 
#print('6 Month CoinBase History Data Points:')
#print(cbdat.shape)


#Isolate history of individual markets for each dataset 
r0 = isolateColumnData(p,0)
print(str(len(r0)) + " elements isolated from column 0")
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
plt.show()



