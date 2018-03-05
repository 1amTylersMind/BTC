#!/bin/bash
# Get historic data first:
wget -O last30d.csv http://data.bitcoinity.org/export_data.csv?c=e&data_type=price&t=l&timespan=30d
sleep 2
wget -O lastVol.csv http://data.bitcoinity.org/export_data.csv?c=e&currency=USD&data_type=volume&t=b&timespan=30d
sleep 2
wget -O activity.csv http://data.bitcoinity.org/export_data.csv?c=edata_type=tradespm&t=l&timespan=30d
sleep 2
echo 'Creating a Recurrent Neural Network based on the following data sets: '
ls | egrep '\.csv'$
sleep 1