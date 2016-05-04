"""All views of our API"""
from flask import Flask, request
from visa import app, mailNinja, database, helper, notify
import json
import base64

@app.route('/api/executeSQL/query=<string:query>')
def executeSQL(query):
    """
    Dummy function to test android-api interactions.
    Will be deprecated.
    """
    print(query)
    #result = database.query_db(query)
    # print([x for x in result])
    #return helper.row_jsonify(result)
    if 'delete' in query.lower() or 'drop' in query.lower():
        return "Oh you slimy cheeky little kid! Quit messing with my database!"
    return "Try harder, something more destructive. Maybe like little bobby tables! ;P"

@app.route('/api/notify/', methods='POST')
def send_email():
    """
    POST parameters:
    - source: 'employee', 'manager', 'admin', or 'lawyer'
    - sender: `emp_code` of source
    """
    source = request.form['source']
    sender = request.form['sender']
    recipients, subject, text_body = getNotificationMail(source, sender)
    message_id = 3
    #mailNinja.send_email(subject, sender, recipients, text_body, message_id)
    print(subject, sender, recipients, text_body, message_id)

@app.route('/api/login/', methods=['POST'])
def login():
    """
    POST params:
    - id: `emp_code`
    - password: `password`
    - category: 'employee', 'manager', 'admin', or 'lawyer'
    """
    emp_code = request.form['id']
    password = request.form['password']
    category = request.form['category']
    query = "SELECT au.emp_code, password FROM allusers as au, {} as x where au.emp_code = ? and password = ? and au.emp_code = x.emp_code LIMIT 1".format(category.lower())
    result = database.query_db(query, (emp_code, password), True)
    if result is None:
        success = 0
    else:
        success = 1
    return json.dumps(dict([("success", success)]))

@app.route('/api/register', methods=['POST'])
def register():
    """
    Returns {"success": 1} or {"success": 0}
    """
    emp_name = request.form['emp_name']
    email_id = request.form['email_id']
    password = request.form['password']
    proj_code = request.form['proj_code']
    department = request.form['department']
    phone_no = request.form['phone_no']
    emp_category = request.form['emp_category']
    query = "INSERT INTO allusers (password) values (?)"
    emp_code = database.insert_db(query, (password,))
    query = "INSERT INTO {} VALUES\
            (?, ?, ?, ?, ?, ?)".format(emp_category)
    lastrowid = database.insert_db(query, (emp_code, proj_code, emp_name, email_id, phone_no, department))
    if database.commit_db():
        success = 1
        notify.sendRegisterMail(lastrowid, email_id, emp_name, proj_code, department, phone_no, emp_category)
    else:
        success = 0
    return json.dumps(dict([("success", success)]))

@app.route('/api/get/employeeDetails', methods=['GET', 'POST'])
def get_emp_details():
    """
    GET params:
    - empCode: `emp_code`
    """
    emp_code = int(request.args.get('empCode'))
    query = "SELECT * FROM employee where emp_code = ?"
    #query="WITH visadetails as (select * from visa_form where emp_code = ?) \
    #SELECT * from employee as e, visadetails where e.emp_code = ?"
    result = database.query_db(query, (emp_code,), True)
    return helper.row_jsonify(result, True)

@app.route('/api/get/visaDetails', methods=['GET', 'POST'])
def get_visa_details():
    """
    GET params:
    - empCode: `emp_code`
    """
    emp_code = int(request.args.get('empCode'))
    query = "SELECT * FROM visa_form where emp_code = ?"
    #query="WITH visadetails as (select * from visa_form where emp_code = ?) \
    #SELECT * from employee as e, visadetails where e.emp_code = ?"
    result = database.query_db(query, (emp_code,), True)
    if result:
        return helper.row_jsonify(result, True)
    else:
        return json.dumps(dict([("success", 0)]))


@app.route('/api/get/unapprovedEmployees', methods=['GET'])
def get_unapproved_emp():
    """
    GET params:
    - emp_code: `emp_code`
    - access_level: 'manager', 'admin'
    """
    emp_code = int(request.args.get('emp_code'))
    approved_by = request.args.get('approved_by')
    query = "SELECT * FROM visa_form where visa_status = ?"
    result = database.query_db(query, (approved_by,))
    return helper.row_jsonify(result)

@app.route('/api/set/visaForm', methods=['POST'])
def set_visa_form():
    emp_code = request.form['emp_code']
    country = request.form['country']
    passport_no = request.form['passport_no']
    visa_type = request.form['visa_type']
    visa_status = "nobody"
    if database.query_db("SELECT * from visa_form where emp_code = ?", (emp_code,), True):
        success = 2
    else:
        query = "INSERT INTO visa_form \
        (emp_code, country, visa_type, passport_no, visa_status) \
        VALUES (?, ?, ?, ?, ?)"
        database.insert_db(query, (emp_code, country.lower(), visa_type.lower(), passport_no.lower(), visa_status.lower()))
        database.commit_db()
        notify.sendNotifyMail(emp_code, country.lower(), visa_type.lower())
        success = 1
    return json.dumps(dict([("success",success)]))

@app.route('/api/set/visaStatus', methods=['POST'])
def set_visa_status():
    emp_code = request.form['emp_code']
    access_level = request.form['access_level']
    action = request.form['action']
    status_flow = ('nobody', 'manager', 'admin', 'lawyer')

    if action == "accept":
        if access_level == "lawyer":
            status = "approved"
            notify.sendApprovedMail(emp_code)
        else:
            notify.sendAcceptMail(emp_code, access_level)
            status = access_level
    else:
        notify.sendDeniedMail(emp_code. access_level)
        status = "denied"

    query = "UPDATE visa_form set visa_status = ? where emp_code = ?"
    database.insert_db(query, (status, emp_code))
    database.commit_db()
    success = 1
    return json.dumps(dict([("success",success)]))

@app.route('/api/set/uploadImage', methods=['GET','POST'])
def upload_image():
    print("Request data: \n", request.form)
    image = base64.b64decode(request.form['image'])
    image.save("/home/ox/Dropbox/Projects/visaServer/visa/static/file.jpg")
    return '{"status":"saved"}'

@app.route('/api/get/adminStatistics', methods=['GET'])
def admin_stats():
    stat_type = request.args.get('statType')
    if stat_type == "visa_type":
        visa_dict = {}
        visa_types = ('h1', 'l1', 'b1', 'student', 'business')
        for visa in visa_types:
            query = "SELECT count(lower(visa_type)) from visa_form where visa_type = ?"
            result = database.query_db(query, (visa,), True)
            visa_dict[visa] = result[0]
        return json.dumps(visa_dict)
    elif stat_type == "visa_status":
        query = "SELECT count(visa_id) as visa_id from visa_form"
        result = database.query_db(query, one=True)
        result_dict = dict(result)
        query2 = "SELECT count(visa_type) as visa_approved from visa_form \
                WHERE visa_status = 'approved'"
        result2 = database.query_db(query2, one=True)
        result2_dict = dict(result2)
        print(result_dict, result2_dict)
        result_dict.update(result2_dict)
        return json.dumps(result_dict)
    else:
        return "Error.", 404
