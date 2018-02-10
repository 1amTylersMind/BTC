# Step 1 : Read in Orderbook.txt data
# First 3 lines formatted: < [last,high,low,vol] mktName >
# Then a single line denoting the name of the market followed
# by its orderbook. (Only captures Bfnx and BStamp )


def csvDataDump(data):
    f = open('trades.csv', "w")
    for line in data:
        f.write(line)


def main():
    f = open('orderbook1.txt', 'r')
    index = 0
    bfxStat = []
    cbStats = []
    bsStats = []
    trades = []
    for line in f:
        if(index == 0):
            bfxStat.append(line)
        if(index == 1):
            cbStats.append(line)
        if(index == 2):
            bsStats.append(line)
        if(',' in line):
            trades.append(line)
    index += 1
    # Now dump the Trade Data into a CSV for Analysis
    csvDataDump(trades)
    # Ok, So it is writing to the csv, but gotta clean it up.


if __name__ == '__main__':
    main()
