#-*-coding:utf-8 -*-

import json
import MySQLdb
import configer

def app(environ, start_response):
	status = '200 OK'
	headers = [('Content-type', 'text/html')]
	start_response(status, headers)
	return get_main_image(environ)

from bae.core.wsgi import WSGIApplication
application = WSGIApplication(app)

def get_main_image(environ):
	try:
		request_body_size = int(environ.get('CONTENT_LENGTH', 0))
	except (ValueError):
		request_body_size = 0

	request_body = environ['wsgi.input'].read(request_body_size)
	obj = json.loads(request_body)
	page = obj['page']

	mydb = MySQLdb.connect(
		host   = configer.host,
      	port   = configer.port,
      	user   = configer.api_key,
      	passwd = configer.secret_key,
      	db = configer.dbname)
 	cursor = mydb.cursor()
	cursor.execute('select * from main_image_data limit '+str(page*12)+','+str(page*12+12))
   	records = cursor.fetchall()

   	main_image_list = []
   	for row in records:
   		item = {'res_id':row[0], 'link':row[1], 'text':row[2], 'coin':row[3]}
   		main_image_list.append(item)

   	main_image_list_dict = {'data':main_image_list}
   	result = json.dumps(main_image_list_dict)

   	cursor.close()
   	mydb.close()

	return [result]