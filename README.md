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
it's previous predictions. Previous techniques have failed because they rely too heavily on
past data for predictions. Bitcoin is extremely volatile, and clearly more attention must be
paid to live information. I am going to try an entirely new predictive technique, borrowing 
from some of the mathematics I learned from Quantum Mechanics!

# Probabilistic Orderbook 
 
The orders found on the books across major exchanges are indicative of not only the spread of
prices, but the rapidity in which they're coming in. One can imagine that just after an official
market price has been recorded, and buy/sell orders start coming in, that these bids represent a 
sort of probabilistic cloud of where the future price will become next. With each successive moment
a previous probabilistic model of potential market values is collapsed into a single true market price,
opening up the potential for change again for next moment.  

Treating price points as expectation values of probabilistic STATEs (defined by prices making up an 
orderbook), and considering a price as an expectation value of that state, I will treat the overall price 
as a superposition of the various prices across orderbooks. To make an estimate as to how the system will 
evolve over time, historic price information will be used to generate a constant used in the price equation.

The resulting predictiction will instead be a series of potential paths, with unique probabilities for each.
During the next time step the true price will be logged, and the program will remember the orderbook layout 
for this resulting change to modify how Probabilities will be calculated in the future (for that price STATE). 

I believe the most interesting aspect of this method of inquiry will be the ability to maximize the simple
quantum equations of superposition to represent the distribution of order sizes, and their corresponding impact
on the market, in precise way. Even more intersting will be to see how the program adapts, and hopefully becomes
more precise, as it accumulates more orderbook information tethered to resulting price movements of the market. 

# MORE TO COME! (Updates Daily)


# Demo Build - Watch to see how to run this project for yourself 
<a href="http://www.youtube.com/watch?feature=player_embedded&v=JLySP2X6L6g" target="_blank">
<img src="http://img.youtube.com/vi/JLySP2X6L6g/0.jpg" alt="Build and Run Demo" width="240" height="180" border="10" /></a>

