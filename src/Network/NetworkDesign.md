## Network Design 
Can the immediate price changes of a Bitcoin be predicted by analyzing
it's live Orderbook Data? That is the question this project is seeking
to answer. To do this we first need to inform the program about what 
Bitcoin markets have looked like in the past, and how they have behaved
under changing conditions like additional volume or trading activity. 
Therefore we provide the program with DataSets tracking key attributes 
accross 10 major exchanges over a long period of time. 

# Utilizing Historical Data 
The three key data sets that will be used to for generating weights are
Price data, Volume data, and Trades-per-minute data. The data sets are
logged hourly over the last 30 days. 

Looking at the fluctuations in price, and perhaps more interestingly the 
potential relationships to be found between price changes in diff. btc 
markets, a function will be generated that will assign a weight for any
given price. 

After analyzing the Volume data, and again the relationship between how
changes in volume lead to changes in price (and these attr. b/w mkts. too), 
a function will be generated to assign a weight based on the volume of a trade. 

After analyzing the historical data detailing the trades per minute, the
size of the orderbook being analyzed will be assigned a weight based on the
trends of how live orderbook maps to stastical properties revealed by 
analyzing trades-per-min data. 

 