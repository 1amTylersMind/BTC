import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
import pandas as pd
import statsmodels.api as sm
import math
import csv
from matplotlib import gridspec

# Three Main Historic DataSets [Price][Volume][Volatility]
prices = pd.read_csv('allMkt30d.csv')
volume = pd.read_csv('allVol30d.csv')
tradepm = pd.read_csv('tpmhrs30d.csv')
# Also 24Hr Data 
p24 = pd.read_csv('price24.csv')
v24 = pd.read_csv('volume24.csv')
t24 = pd.read_csv('trades24.csv')

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
NORMALIZE A ONE DIMENSIONAL DATASET
'''
def normalizeFlattenedDataSet(data):
	norm = []
	for term in data:
		norm.append(sigmoid(term))
	return norm


'''
Create a MACD Oscillator Visual for Arbitrary 30d mkt data
'''
def createMACD(mktmap,mkt):
	print('Creating MACD Oscillator for '+mkt)
	df = pd.DataFrame(mktmap[mkt])		
	dfprime = df.diff(periods=48,axis=0)	
	macd = pd.ewma(mktmap[mkt],span=288)
	bigmac = pd.ewma(mktmap[mkt],span=624)
	weekmacd = pd.ewma(mktmap[mkt],span = 168)
	# Make a plot
	plt.style.use('bmh')
	gs = gridspec.GridSpec(2,1,height_ratios=[3,1]) 	
	plt.subplot(gs[0])
	plt.title(mkt+' 30 Day ')
	plt.plot(df)	
	plt.subplot(gs[1])
	plt.plot(weekmacd - macd,label='MACD')
	plt.xlabel('Moving Average')
	plt.show()


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
plt.style.use('bmh')
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

# Map the three data sets by market
priceMap = organizeMarketData(pdat,priceParams,1)
volumeMap = organizeMarketData(vdat,volParams,2)
tradeMap = organizeMarketData(tdat,tradeParams,3)

#NOTE! Not all maps have the same markets. For 
# DeepDive selections, need markets that have all 3 maps

#Now ready to start analyzing single markets at a time
createMACD(priceMap,'bitfinex')


'''
Confirm that market is in all datasets
'''
def isCompleteMarket(pmap,vmap,tmap,mkt):
	if(mkt in pmap) and (mkt in vmap) and (mkt in tmap):
		return True
	else:
		return False
	

'''
Now Ready to make methods for analyzing by market
'''
def analyzeMarket(marketName):	
	
	# Isolate the [Price][Volume][Trading] sets for Mkt
	pseries = pd.DataFrame(priceMap[marketName])
	vseries = pd.DataFrame(volumeMap[marketName])
	tseries = pd.DataFrame(tradeMap[marketName])
	perr = pseries.std()
	verr = vseries.std()
	terr = tseries.std() 
	xdata = np.linspace(0,718,1)
	plt.figure(1)
	mpl.style.use('ggplot')
	# Price Subplot
	plt.subplot(311)
	plt.ylabel('Prices')
	plt.title(marketName+' 30d Data ')
    	#Isolate user defined DataSets to Plot
	plt.plot(pseries,label='Price data')	
	plt.plot(pd.ewma(pseries,span=24),label='Moving Avg')
	#plt.plot(pseries.diff(periods=24,axis=0))
	
	# Volume Subplot	
	plt.subplot(312)
	plt.ylabel('Volume')
   	plt.plot(vseries, label='Volume Data')
   	plt.plot(pd.ewma(vseries,span=24),label='Moving Avg')
	plt.legend(handles=[])
	# Trading volatility Subplot	
	plt.subplot(313)
	plt.style.use('bmh')
	plt.plot(tseries,label='Trades/min')
	plt.ylabel('Trades/Min')
	plt.plot(pd.ewma(tseries,span=24),label='Moving Avg')

	# Finally, show the plots 
	plt.show()
 


def main():
	#Allow User to instead select market
	print('Select a Market to look at closer:')
	index = 0
	for mkt in priceParams:
		print('[%i] - %s' % (index,mkt))
		index += 1
	running = True 
	while(running):
		selection = raw_input()
		print('Enter: DONE to exit this module')
		if (selection=='DONE'):
			running = False
		elif isCompleteMarket(priceMap,volumeMap,tradeMap,selection):
			analyzeMarket(selection)


if __name__ == '__main__':
	main()
