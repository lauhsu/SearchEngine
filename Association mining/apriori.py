import data_process as dp


def createC1ByRec():
    C1 = []
    id = 1
    transaction = dp.getOneRecord(id)
    while transaction != '':
        items = transaction.split()
        for item in items:
            if dp.isTime(item):
                item = dp.getHour(item)
            if not [item] in C1:
                C1.append([item])
        id+=1
        transaction = dp.getOneRecord(id)

    C1.sort()
    return list(map(frozenset, C1)),dp.getLogLen()


def createC1ByKw():
    dataSet = dp.getKwClusters(1800)
    #print(dataSet)
    C1 = []
    for transaction in dataSet:
        for item in transaction:
            if not [item] in C1:
                C1.append([item])

    C1.sort()
    return list(map(frozenset, C1)),dataSet


def getSupportData(numItems,ssCnt,minSupport):
    Lk = []
    supportData = {}
    for key in ssCnt:
        support = ssCnt[key] / numItems
        if support >= minSupport:
            Lk.append(key)
        supportData[key] = support
    return Lk, supportData


def scanDByRec(Ck, logLen,minSupport):
    ssCnt = {}
    id= 1
    transaction = dp.getOneRecord(id)
    while transaction != '':
        tid = transaction.split()
        tid[0] = dp.getHour(tid[0])
        for can in Ck:
            if can.issubset(tid):
                if can not in ssCnt.keys():
                    ssCnt[can] = 1
                else:
                    ssCnt[can] += 1
        id+=1
        transaction = dp.getOneRecord(id)

    return getSupportData(float(logLen),ssCnt,minSupport)


def scanDByKw(D, Ck, minSupport):
    ssCnt = {}
    for tid in D:
        for can in Ck:
            if can.issubset(tid):
                if can not in ssCnt.keys():
                    ssCnt[can] = 1
                else:
                    ssCnt[can] += 1
    return getSupportData(float(len(D)), ssCnt, minSupport)


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


def aprioriByRec(minSupport = 0.5):
    C1,logLen = createC1ByRec()
    L1, supportData = scanDByRec(C1, logLen,minSupport)
    L = [L1]
    k = 2
    while (len(L[k - 2]) > 0):
        Lk_1 = L[k - 2]
        Ck = aprioriGen(Lk_1, k)
        print("ck:", Ck)
        Lk, supK = scanDByRec(Ck,logLen,minSupport)
        supportData.update(supK)
        print("lk:", Lk)
        L.append(Lk)
        k += 1
    return L, supportData

def getKwSet():
    m_set = []
    return m_set

def aprioriByKw(minSupport = 0.5):
    C1,D = createC1ByKw()
    L1, supportData = scanDByKw(D,C1, minSupport)
    L = [L1]
    k = 2
    while (len(L[k-2]) > 0):
        Lk_1 = L[k-2]
        Ck = aprioriGen(Lk_1, k)
        print("ck:",Ck)
        Lk, supK = scanDByKw(D,Ck, minSupport)
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

def getKeyword(item):
    head = item.find('{')
    tail = item.rfind('}')
    return item[head+1:tail]


def saveRules(rules,conf):
    fileName = str(conf)+'.txt'
    f = open(fileName,'a')
    recommendDic = {}
    for item in rules:
        if len(item[0])==1:
            pre=getKeyword(str(item[0]))
            next = getKeyword(str(item[1]))
            if pre not in recommendDic.keys():
                recommendDic[pre]=[next]
            else:
                recommendDic[pre].append(next)
    for key in recommendDic.keys():
        log = key+':'+str(recommendDic[key]).replace('"','')+'\n'
        f.write(log)
    f.close()


def runByRec(minSupport = 0.5,minConf=0.7):
    L, supportData = aprioriByRec(minSupport)
    return generateRules(L, supportData, minConf)


def runBykw(minSupport=0.5,minConf=0.7):
    L, supportData = aprioriByKw(minSupport)
    return generateRules(L, supportData, minConf)

runBykw(0.05)