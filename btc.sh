#!/bin/bash 
sudo pip install numpy
sudo pip install matplotlib 
sudo pip install pandas
sudo pip install -U statsmodels
sudo pip install spyder
echo '______________________________________'
echo 'Python Libraries Installed'
#git clone https://github.com/TylersDurden/BTC
#sleep 5
#cd BTC/BtcDataLib
cd /media/root/CoopersDB/BTC
echo '** BTC Finiancial Analyis Repository Downloaded ** '
cd BtcDataLib
echo '--------------------------------------'
echo '   Potential Data Sets to Analyze:    |' 
echo '--------------------------------------'
ls | egrep '\.csv'$ 
echo '--------------------------------------'
chmod +x datatrain.py
chmod +x script.py
touch quietpython.txt
# Analyze the 30day Data Sets
python script.py
sleep 2
# Now Analyze the 24hr Data Sets
echo '--------------------------------------'
echo '|Downloaded following 24hr Data:     |'
ls | egrep '\.csv'$ 
echo '--------------------------------------'
chmod +x script.py
python script.py
# Start getting live orderbook data to compare with predictions 
echo 'Historic Data Pre-Processed'
sleep 1
echo 'Beginning Live Data Capture'
cd ..
chmod +x livecapt.sh
su root ./livecapt.sh
