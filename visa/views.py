"""All views of our API"""
from flask import Flask, request
from visa import app, mailNinja, database
import json

@app.route('/api/executeSQL/query=<string:query>')
def executeSQL(query):
    """
    Dummy function to test android-api interactions.
    Will be deprecated.
    """
    print(query)
    result = database.query_db(query)
    # print([x for x in result])
    return json.dumps([dict(x) for x in result])

@app.route('/api/notify/', methods='POST')
def send_email():
    """
    POST parameters:
    - source: 'employee', 'manager', 'admin', or 'lawyer'
    - sender: `emp_id` of source
    """
    source = request.form['source']
    sender = request.form['sender']
    recipients, subject, text_body = getNotificationMail(source, sender)
    message_id = 3
    #mailNinja.send_email(subject, sender, recipients, text_body, message_id)
    print(subject, sender, recipients, text_body, message_id)

@app.route('/api/login', methods=['GET','POST'])
def login():
    """
    POST params:
    - id: `emp_id`
    - password: `password`
    - category: 'employee', 'manager', 'admin', or 'lawyer'
    """
    emp_id = request.form['id']
    password = request.form['password']
    category = request.form['category']
    query = "SELECT id, password FROM {} where id = ? and password = '?' LIMIT 1".format(category)
    cur = database.get_db().cursor()
    result = database.query_db(query, (emp_id, password), True)
    if result is None:
        success = 0
    else:
        success = 1
    return json.dumps(dict([("success", success)]))
