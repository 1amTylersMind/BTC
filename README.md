# BTC 
This project is an attempt at using real data to create a predictive algorithm for the 
price movements of Bitcoin. Predictions will rely on both historic and live data.

![30 Day Data](https://raw.githubusercontent.com/TylersDurden/BTC/master/ExampleFigure.png)


It is important that before any live data can be gathered, the program first gets information 
about the last 30 days and the last 24 hours. This way, when the program is presented with
live orderbook data, there is also some context as to what the state of the market is at
the present moment. 

![24 Hr Data](https://raw.githubusercontent.com/TylersDurden/BTC/master/30dMarketSummaryBfnx.png)

# DataTraining 
Ultimately the goal is to use historic knowledge of the movement of these exchanges
to be able leverage orderbook data into predictions about imminent price movements. 
So before any live orderbook data can truly be processed, the data training layer
will first be trained with a large volume of historic data across many exchanges
over a long period of time. 

As the program runs, it begins to append the history of this price data, now taking
into account the orderbooks that created the resulting price changes, and includes 
this information by propogating it back through the most recent history to modify
orderbook weights and by extension next predictions. 

# Data Sets 
For the historic data sets, I've chosen to utilize CSVs found at data.bitcoinity.org. 
I would highly suggest anyone interested in Bitcoin visits their site, it's amazing. 

# Predictive Techniques Employed 
The ultimate goal of this program is to utilize an articial network to take a vast amount 
of previous bitcoin related data, in order to inform the program on how to make short term
price predictions based on the current orderbook of live exchanges topography. 

Even more importantly, as the program continues to run it will acknowledge the accuracy of
it's previous predictions. 

# Probabilistic Orderbook 

Previous techniques have failed because they rely too heavily on past data for predictions. 
Instead, I am going to try an entirely new predictive technique, borrowing from some of the
mathematics I learned from Quantum Mechanics! 

# MORE TO COME! (Updates Daily)


# Demo Build - Watch to see how to run this project for yourself 
<a href="http://www.youtube.com/watch?feature=player_embedded&v=JLySP2X6L6g" target="_blank">
<img src="http://img.youtube.com/vi/JLySP2X6L6g/0.jpg" alt="Build and Run Demo" width="240" height="180" border="10" /></a>

