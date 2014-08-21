#-*-coding:utf-8 -*-

import json
import MySQLdb
import configer

def app(environ, start_response):
	status = '200 OK'
	headers = [('Content-type', 'text/html')]
	start_response(status, headers)
	return get_receiver_list(environ)

from bae.core.wsgi import WSGIApplication
application = WSGIApplication(app)

def get_receiver_list(environ):
	try:
		request_body_size = int(environ.get('CONTENT_LENGTH', 0))
	except (ValueError):
		request_body_size = 0

	request_body = environ['wsgi.input'].read(request_body_size)
	obj = json.loads(request_body)
	flag = obj['get_receiver_list']

	mydb = MySQLdb.connect(
		host   = configer.host,
      	port   = configer.port,
      	user   = configer.api_key,
      	passwd = configer.secret_key,
      	db = configer.dbname)
 	cursor = mydb.cursor()
	cursor.execute('select * from user_info')
   	records = cursor.fetchall()

   	user_id_list = []
   	for row in records:
   		item = {'user_id':row[0], 'nick_name':row[1]}
   		user_id_list.append(item)

   	receiver_list_dict = {'receiver_list':user_id_list}
   	result = json.dumps(receiver_list_dict)

   	cursor.close()
   	mydb.close()

	return [result]