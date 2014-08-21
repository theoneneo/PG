#-*-coding:utf-8 -*-

import json
import MySQLdb
import configer

def app(environ, start_response):
    status = '200 OK'
    headers = [('Content-type', 'text/html')]
    start_response(status, headers)
    return set_nick_name(environ)

from bae.core.wsgi import WSGIApplication
application = WSGIApplication(app)

def set_nick_name(environ):
    try:
        request_body_size = int(environ.get('CONTENT_LENGTH', 0))
    except (ValueError):
        request_body_size = 0

    request_body = environ['wsgi.input'].read(request_body_size)
    obj = json.loads(request_body)
    user_id = obj['user_id']
    nick_name = obj['nick_name']

    mydb = MySQLdb.connect(
        host   = configer.host,
        port   = configer.port,
        user   = configer.api_key,
        passwd = configer.secret_key,
        db = configer.dbname)
    cursor = mydb.cursor()

    cursor.execute('select * from user_info where user_id=%s', (user_id,))
    records = cursor.fetchone()
    if records is None:
        result = json.dumps({'nick_result':'no_register_id'})
    else:
        cursor.execute('select * from user_info where nick_name=%s', (nick_name,))
        nick_records = cursor.fetchone()
        if nick_records is None:        
            try:
                cursor.execute('update user_info set nick_name=%s where user_id=%s', (nick_name,user_id))
                mydb.commit()
                result = json.dumps({'nick_result':'success'})   
            except Exception, e:       
                result = json.dumps({'nick_result':'fail'})
        else:
            result = json.dumps({'nick_result':'other_one'})

    cursor.close()
    mydb.close()

    return [result]    