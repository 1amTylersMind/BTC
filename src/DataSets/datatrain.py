import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import statsmodels.api as sm
import csv

# Three Main Historic DataSets [Price][Volume][Volatility]
prices = pd.read_csv('allMkt30d.csv')
volume = pd.read_csv('allVol30d.csv')
tradepm = pd.read_csv('tpmhrs30d.csv')

'''
GET MARKET PARAMETERS
'''
def getMarketParameters(fname):
	params = []
	with open(fname) as f:
		reader = csv.reader(f)
		params1 = next(reader)
		index = 0
		for element in params1:
			if(index>0): 
				params.append(element)
			index+=1
	return params



# Apply Functions to Price DataSet First
# Optimize
# Then make program include values for all 3 
pdat     = prices.iloc[:,1:11].values
vdat	 = volume.iloc[:,1:11].values
tdat 	 = tradepm.iloc[:,1:11].values 
priceParams = getMarketParameters('allMkt30d.csv')
volParams = getMarketParameters('allVol30d.csv')
tradeParams = getMarketParameters('tpmhrs30d.csv')

# Note that parameters are not in the same place
# Between each Data Set variety 


'''
ORGANIZE DATASET BY MARKET
'''
def organizeMarketData(dataSet,dsparams,typ):
	mapping = {}
	for mkt in dsparams:
		index = 0
		mapping[mkt] = analyzeSingleMarket(index,typ)
		index += 1
	return mapping
	
	
'''
ANALYZE SINGLE MARKET
'''
def analyzeSingleMarket(colNum,mktType):
	data = []
	if(mktType == 1):
		return pdat[:,colNum]
	if(mktType == 2):
		return vdat[:,colNum]
	if(mktType == 3):
		return tdat[:,colNum]		
	else:
		return data


'''
SIGMOID
'''
def sigmoid(x):
	return 1/(1+math.exp(-x))


'''
Now Ready to make methods for analyzing by market
'''
def analyzeMarket(marketName):
	#Make Models	
	size = len(priceMap[marketName])+len(volumeMap[marketName])+len(tradeMap[marketName])
	print(str(size)+' Points Colled on '+marketName)
	pseries = pd.DataFrame(priceMap[marketName])
	vseries = pd.Series(volumeMap[marketName])
	tseries = pd.Series(tradeMap[marketName])
	xdata = np.linspace(0,718,1)
	
	print(str(len(pseries))+' and '+ str(len(np.arange(len(pseries)))))
	print(str(pseries.std(axis=None)))
	#prcModel = sm.Logit(priceMap[marketName],np.arange(len(pseries)))
	#volModel = sm.Logit(vseries,xdata)
    #tpmModel = sm.Logit(tseries,xdata)
	#pfit = priceModel.fit()
	#vfit = volModel.fit()
	#tfit = tpmModel.fit()
	
	# Make plots 
	#pseries.plot()
	pd.ewma(pseries,span=24).plot(style='k-')
	plt.plot(pseries)
	plt.title(marketName+' 30d Price')
	plt.show()
 	

# Map the three data sets by market
priceMap = organizeMarketData(pdat,priceParams,1)
volumeMap = organizeMarketData(vdat,volParams,2)
tradeMap = organizeMarketData(tdat,tradeParams,3)

# Analyze Bitfinex
analyzeMarket('bitfinex')
