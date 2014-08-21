#-*-coding:utf-8 -*-

import json
import MySQLdb
import configer

def app(environ, start_response):
	status = '200 OK'
	headers = [('Content-type', 'text/html')]
	start_response(status, headers)
	return login(environ)

from bae.core.wsgi import WSGIApplication
application = WSGIApplication(app)

def login(environ):
	try:
		request_body_size = int(environ.get('CONTENT_LENGTH', 0))
	except (ValueError):
		request_body_size = 0

	request_body = environ['wsgi.input'].read(request_body_size)
	obj = json.loads(request_body)
	login_id = obj['login_id']

	mydb = MySQLdb.connect(
		host   = configer.host,
      	port   = configer.port,
      	user   = configer.api_key,
      	passwd = configer.secret_key,
      	db = configer.dbname)
 	cursor = mydb.cursor()

 	try:
   		cursor.execute('select * from user_info where user_id=%s', (login_id,))
   		records = cursor.fetchone()
   		if not records is None:
   			result = json.dumps({'login_result':records[1]})
   		else:
   			result = json.dumps({'login_result':'none'})
	except Exception, e:
		result = json.dumps({'login_result':'fail'})

   	cursor.close()
   	mydb.close()

	return [result]