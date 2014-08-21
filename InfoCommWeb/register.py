#-*-coding:utf-8 -*-

import json
import MySQLdb
import configer

def app(environ, start_response):
    status = '200 OK'
    headers = [('Content-type', 'text/html')]
    start_response(status, headers)
    return register(environ)

from bae.core.wsgi import WSGIApplication
application = WSGIApplication(app)

def register(environ):
    try:
        request_body_size = int(environ.get('CONTENT_LENGTH', 0))
    except (ValueError):
        request_body_size = 0

    request_body = environ['wsgi.input'].read(request_body_size)
    obj = json.loads(request_body)
    register_id = obj['register_id']

    mydb = MySQLdb.connect(
        host   = configer.host,
        port   = configer.port,
        user   = configer.api_key,
        passwd = configer.secret_key,
        db = configer.dbname)
    cursor = mydb.cursor()

    try:
        cursor.execute('select * from user_info where user_id=%s', (register_id,))
        records = cursor.fetchone()
        if not records is None:
            result = json.dumps({'register_result':'other_one'})
        else:
            cursor.execute('insert into user_info values(%s, %s)', (register_id, ''))
            mydb.commit()
            result = json.dumps({'register_result':'success'})   
    except Exception, e:       
        result = json.dumps({'register_result':'fail'})

    cursor.close()
    mydb.close()

    return [result]    