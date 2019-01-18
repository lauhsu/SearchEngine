import linecache
import time
import datetime
# 扫描数据库
# 假设包含字段有:搜索时间,用户ID,处理后的关键词,用户点击的url.

log = 'log.txt'
time_loc = 0
usr_loc=1
kw_loc = 3
date_str = '2018-1-1'

def getKeyword(search):
    return search

def transformStr(str):
    str_len = len(str)
    str = str[1:str_len-2]
    str_arr = str.split(',')
    res = ''
    print(str)
    for i in range(len(str_arr)):
        if i ==0:
            str_arr[i] = str_arr[i][1:len(str_arr[i])-1]
        else:
            str_arr[i] = str_arr[i][2:len(str_arr[i]) - 1]
        str_arr[2] = getKeyword(str_arr[2])
        print(str_arr[i])
        if i<len(str_arr)-1:
            res+=str_arr[i]+'\t'
        else:
            res+=str_arr[i]+'\n'

    print(res)
    return res

def extractLog():
    id = 1
    item = linecache.getline(log,id)
    print(item)
    file_table = []
    while item!='':
        print(str(id))
        item_slice = item.split()
        del item_slice[3:5]
        file_table.append(item_slice)
        id+=1
        item = linecache.getline(log, id)
        print(item)
    file_table.sort(key=lambda x:x[1])
    file = open('data.txt', 'w')
    for i in range(len(file_table)):
        file.write(transformStr(str(file_table[i])))
    file.close()

def getLogLen():
    return 1

def getOneRecord(id):
    return linecache.getline(log,id)

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

def getKwClusters(interval):
    start_pos = 1
    transactions = []
    search_item = linecache.getline(log, start_pos)
    items = search_item.split()
    start_time = items[time_loc]
    usr = items[usr_loc]
    key_words = []
    key_words.append(items[kw_loc])
    cur_pos = start_pos + 1

    while True:
        search_item = linecache.getline(log, cur_pos)
        if search_item=='':break

        items = search_item.split()
        new_usr = items[usr_loc]

        cur_time = items[time_loc]
        if computeTime(cur_time,start_time)<=interval and new_usr==usr:
            if items[-1] not in key_words:
                key_words.append(items[kw_loc])
        else:
            if new_usr!=usr:
                usr=new_usr
            transactions.append(key_words)
            key_words = []
            start_time = cur_time
        cur_pos+=1

    return transactions

def isUsrId(item):
    return True

def isTime(item):
    if item.find(':') != -1:
        return True
    else:
        return False

def getHour(item):
    return 'H:'+item[kw_loc]

def isIp(item):
    return True

def isUrl(item):
    return True

def isKeyWord(item):
    return True

def getKeyWords(item):
    keywords = []
    return keywords

def getAllKeyWords():
    keywords = []
    return keywords

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


