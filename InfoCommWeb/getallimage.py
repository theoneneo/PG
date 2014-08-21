#-*-coding:utf-8 -*-

import json
import MySQLdb
import configer

def app(environ, start_response):
	status = '200 OK'
	headers = [('Content-type', 'text/html')]
	start_response(status, headers)
	return get_all_image(environ)

from bae.core.wsgi import WSGIApplication
application = WSGIApplication(app)

def get_all_image(environ):
	try:
		request_body_size = int(environ.get('CONTENT_LENGTH', 0))
	except (ValueError):
		request_body_size = 0

	request_body = environ['wsgi.input'].read(request_body_size)
	obj = json.loads(request_body)
	res_id = obj['res_id']
	page = obj['page']

	mydb = MySQLdb.connect(
		host   = configer.host,
      	port   = configer.port,
      	user   = configer.api_key,
      	passwd = configer.secret_key,
      	db = configer.dbname)
 	cursor = mydb.cursor()
	cursor.execute('select * from all_image_data where parent_res_id=%s limit '+str(page*12)+','+str(page*12+12), (res_id,))
   	records = cursor.fetchall()

   	all_image_list = []
   	for row in records:
   		item = {'res_id':row[0], 'parent_res_id':row[1], 'link':row[2], 'text':row[3], 'coin':row[4]}
   		all_image_list.append(item)

   	all_image_list_dict = {'data':all_image_list}
   	result = json.dumps(all_image_list_dict)

   	cursor.close()
   	mydb.close()

	return [result]