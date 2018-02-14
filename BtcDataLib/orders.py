
def csvDataDump(data):
    f = open('trades.csv', "w")
    for line in data:
        f.write(line)


def getContextualData():
    f = open('dat.txt', "r")
    index = 0
    datadump = []
    for line in f:
        if 'bitfinex' in line:
            if ',' in line:
                datadump.append(line.split(' bitfinex')[0])
        if 'coinbase' in line:
            if ',' in line:
                datadump.append(line.split(' coinbase')[0])
        if 'bitstamp' in line:
            if ',' in line:
                datadump.append(line.split(' bitstamp')[0])
        index += 1
    f = open('context.csv', "w")
    for ln in datadump:
        f.write(ln + '\n')


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
    getContextualData()


if __name__ == '__main__':
    main()
