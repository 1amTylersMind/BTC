#!/usr/bin/env bash
#------------------#
# Bitcoin Resources|
#------------------#
# Bitcoin Resources 
#BITFINEX 
touch bfnx.txt
curl https://api.bitfinex.com/v2/trades/tBTCUSD/hist > bfnx.txt
#BITMEX (link isn't wokring well)
touch bitmex.txt
curl https://api.bitmex.com/v1/orderBook/L2?symbol=XBT&depth=100 > bitmex.txt
#GDAX 
touch gdax.txt
curl https://api.gdax.com//products/BTC-USD/book?level=2 > gdax.txt
