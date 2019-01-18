import linecache
import time
import datetime
import os
import re
log_path = 'log.txt'
date_str = '2018-1-1'
def computeTime(time1_str,time2_str):
    timeArray1 = time.strptime(date_str+' '+time1_str, "%Y-%m-%d %H:%M:%S")
    timeStamp1 = int(time.mktime(timeArray1))
    timeArray2 = time.strptime(date_str+' '+time2_str, "%Y-%m-%d %H:%M:%S")
    timeStamp2 = int(time.mktime(timeArray2))
    t1 = datetime.datetime.fromtimestamp(timeStamp1)
    t2 = datetime.datetime.fromtimestamp(timeStamp2)
    if t1>t2:
        return (t1-t2).seconds
    else:
        return (t2-t1).seconds


def getTransactions():
    start_pos=1
    transactions = []
    search_item = linecache.getline(log_path, start_pos)
    items = search_item.split()
    start_time = items[0]
    usr = items[1]
    key_words =[]
    key_words.append(items[-1])

    cur_pos = start_pos+1
    while True:
        search_item = linecache.getline(log_path, cur_pos)
        if search_item=='':break

        items = search_item.split()
        new_usr = items[1]

        cur_time = items[0]
        if computeTime(cur_time,start_time)<=1800 and new_usr==usr:
            if items[-1] not in key_words:
                key_words.append(items[-1])
        else:
            if new_usr!=usr:
                usr=new_usr
            transactions.append(key_words)
            key_words = []
            start_time = cur_time
        cur_pos+=1

    return transactions

def loadDataSet():
    return getTransactions()


def createC1(dataSet):
    C1 = []
    for transaction in dataSet:
        for item in transaction:
            if not [item] in C1:
                C1.append([item])

    C1.sort()
    return list(map(frozenset, C1))


def scanD(D, Ck, minSupport):
    ssCnt = {}
    for tid in D:
        for can in Ck:
            if can.issubset(tid):
                if can not in ssCnt.keys(): ssCnt[can] = 1
                else: ssCnt[can] += 1

    numItems = float(len(D))
    Lk= []
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

def apriori(dataSet, minSupport = 0.5):
    C1 = createC1(dataSet)
    L1, supportData = scanD(dataSet, C1, minSupport)
    L = [L1]
    k = 2
    while (len(L[k-2]) > 0):
        Lk_1 = L[k-2]
        Ck = aprioriGen(Lk_1, k)
        print("ck:",Ck)
        Lk, supK = scanD(dataSet, Ck, minSupport)
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

def getKeyword(item):
    head = item.find('{')
    tail = item.rfind('}')
    return item[head+1:tail]

def recommend(keyword,fileName):
    line_id = 1
    success = False
    while True:
        log = linecache.getline(fileName, line_id)
        if log=='':break
        i = log.find(':')
        cur_keyword=log[:i]
        if keyword==cur_keyword:
            reco_keyword = log[i+2:-2]
            success=True
            break
        line_id+=1
    if not success:
        print('can not find')
    return reco_keyword,success


#dataset = loadDataSet()
#L, supportData = apriori(dataset, minSupport=0.05)
#rules = generateRules(L,supportData,0.5)
#saveRules(rules,0.5)

r,success= recommend('\'歼10\'','0.5.txt')
print(r)