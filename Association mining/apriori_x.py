import linecache

log_path = 'log.txt'

def createC1():
    C1 = []
    line_id = 1
    transaction = linecache.getline(log_path,line_id)
    while transaction != '':
        items = transaction.split()
        for item in items:
            if item.find(':')!=-1:
                t = item.split(':')
                item = 'time:'+t[0]
            if not [item] in C1:
                C1.append([item])
        line_id+=1
        transaction = linecache.getline(log_path, line_id)
    C1.sort()
    return list(map(frozenset, C1)),line_id-1


def scanD(Ck, logLen,minSupport):
    ssCnt = {}
    line_id = 1
    transaction = linecache.getline(log_path, line_id)
    while transaction != '':
        tid = transaction.split()
        t = tid[0].split(':')
        tid[0]='time:'+t[0]
        for can in Ck:
            if can.issubset(tid):
                if can not in ssCnt.keys(): ssCnt[can] = 1
                else: ssCnt[can] += 1
        line_id+=1
        transaction = linecache.getline(log_path, line_id)

    numItems = float(logLen)
    Lk = []
    supportData = {}
    for key in ssCnt:
        support = ssCnt[key] / numItems
        if support >= minSupport:
            Lk.append(key)
        supportData[key] = support
    return Lk, supportData


def aprioriGen(Lk_1, k):
    Ck = []
    lenLk = len(Lk_1)
    for i in range(lenLk):
        L1 = list(Lk_1[i])[:k - 2]
        L1.sort()
        for j in range(i + 1, lenLk):
            L2 = list(Lk_1[j])[:k - 2]
            L2.sort()
            if L1 == L2:
                Ck.append(Lk_1[i] | Lk_1[j])

    return Ck

def apriori(minSupport = 0.5):
    C1,logLen = createC1()
    L1, supportData = scanD(C1, logLen,minSupport)
    L = [L1]
    k = 2
    while (len(L[k - 2]) > 0):
        Lk_1 = L[k - 2]
        Ck = aprioriGen(Lk_1, k)
        print("ck:", Ck)
        Lk, supK = scanD(Ck,logLen,minSupport)
        supportData.update(supK)
        print("lk:", Lk)
        L.append(Lk)
        k += 1
    return L, supportData


def generateRules(L, supportData, minConf=0.7):
    bigRuleList = []
    for i in range(1, len(L)):
        for freqSet in L[i]:
            H1 = [frozenset([item]) for item in freqSet]
            if (i > 1):
                rulesFromConseq(freqSet, H1, supportData, bigRuleList, minConf)
            else:
                calcConf(freqSet, H1, supportData, bigRuleList, minConf)
    return bigRuleList


def calcConf(freqSet, H, supportData, brl, minConf=0.7):
    prunedH = []
    for conseq in H:
        conf = supportData[freqSet] / supportData[freqSet - conseq]
        if conf >= minConf:
            print(freqSet - conseq, '-->', conseq, 'conf:', conf)
            brl.append((freqSet - conseq, conseq, conf))
            prunedH.append(conseq)

    return prunedH


def rulesFromConseq(freqSet, H, supportData, brl, minConf=0.7):
    m = len(H[0])
    if (len(freqSet) > (m + 1)):
        Hmp1 = aprioriGen(H, m + 1)
        Hmp1 = calcConf(freqSet, Hmp1, supportData, brl, minConf)
        if (len(Hmp1) > 0):
            rulesFromConseq(freqSet, Hmp1, supportData, brl, minConf)

# test
#L, supportData = apriori(minSupport=0.01)
#generateRules(L,supportData,0.7)