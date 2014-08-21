#-*- coding: utf-8 -*-

import json
import MySQLdb
import configer
import xinge
import time

def app(environ, start_response):
    status = '200 OK'
    headers = [('Content-type', 'text/html')]
    start_response(status, headers)
    return send_push_message(environ)    

from bae.core.wsgi import WSGIApplication
application = WSGIApplication(app)

def send_push_message(environ):
    try:
        request_body_size = int(environ.get('CONTENT_LENGTH', 0))
    except (ValueError):
        request_body_size = 0

    request_body = environ['wsgi.input'].read(request_body_size)
    obj = json.loads(request_body)
    user_id_list = obj['receives_id']

    mydb = MySQLdb.connect(
        host   = configer.host,
        port   = configer.port,
        user   = configer.api_key,
        passwd = configer.secret_key,
        db = configer.dbname)
    cursor = mydb.cursor()
    try:
        key = str(time.time())
        for row in user_id_list:
            cursor.execute('select * from user_info where user_id=%s', (row,))
            record = cursor.fetchone()

            if not record is None:
                x = xinge.XingeApp(2100006809, '27722a6c2e014983351cded03c070649')
                msg = BuildNotice(key, obj['title'], obj['message'], obj['time'], obj['place'], obj['link'])
                ret = x.PushSingleAccount(xinge.XingeApp.DEVICE_ALL, record[0], msg)

        obj['key'] = key
        cursor.execute('insert into notice_info values(%s, %s)', (key, json.dumps(obj)))
        mydb.commit()
        result = json.dumps({'send_result':key})
    except Exception, e:        
        result = json.dumps({'send_result':'fail'})

    cursor.close()
    mydb.close()

    return [result]

def BuildNotice(key, title, message, time, place, link):
    msg = xinge.Message()
    msg.type = xinge.Message.TYPE_MESSAGE
    msg.content = 'notice'
    # 消息为离线设备保存的时间，单位为秒。默认为0，表示只推在线设备
    msg.expireTime = 86400
    # 自定义键值对，key和value都必须是字符串
    msg.custom = {'key':key, 'title':title, 'message':message, 'time':time, 'place':place, 'link':link}    
    return msg                       