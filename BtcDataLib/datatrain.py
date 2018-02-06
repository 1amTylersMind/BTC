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
	size = len(priceMap[marketName])+len(volumeMap[marketName])+len(tradeMap[marketName])
	print(str(size)+' Points Colled on '+marketName)
	pseries = pd.DataFrame(priceMap[marketName])
	vseries = pd.Series(volumeMap[marketName])
	tseries = pd.Series(tradeMap[marketName])
	xdata = np.linspace(0,718,1)
	plt.figure(1)
	plt.subplot(311)
	plt.ylabel('Prices')
	plt.title(marketName+' 30d Data ')
    	#Isolate user defined DataSets to Plot
	plt.plot(pseries)
	plt.plot(pd.ewma(pseries,span=24))
	plt.subplot(312)
	plt.ylabel('Volume')
   	plt.plot(vseries)
   	plt.plot(pd.ewma(vseries,span=24))
	plt.subplot(313)
	plt.plot(tseries)
	plt.ylabel('Trades/Min')
	plt.plot(pd.ewma(tseries,span=24))
	# Finally, show the plots 
	plt.show()
 	


# Map the three data sets by market
priceMap = organizeMarketData(pdat,priceParams,1)
volumeMap = organizeMarketData(vdat,volParams,2)
tradeMap = organizeMarketData(tdat,tradeParams,3)



def main():
	# Analyze Bitfinex
	#analyzeMarket('bitfinex')
	#analyzeMarket('coinbase')
	#Allow User to instead select market
	print('Select a Market to look at closer:')
	index = 0
	for mkt in priceParams:
		print('[%i] - %s' % (index,mkt))
		index += 1
	running = True 
	while(running):
		selection = raw_input()
		analyzeMarket(selection)
		print('Enter: DONE to exit this module')
		if (selection=='DONE'):
			running = False


if __name__ == '__main__':
	main()
