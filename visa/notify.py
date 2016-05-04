"""Helper functions for notifications"""
import textwrap
import json
from visa import database, mailNinja, helper

def getNotificationMail(source, sender):
    if source.lower() == 'employee':
        query = "SELECT m.email_id from employee e, manager m where e.emp_id = ? and e.proj_code = m.proj_code LIMIT 1"
        result = database.query_db(query, sender, True)
        recipients = list(result)
        subject = "Employee Number {} has applied for visa registration.".format(sender)
        text_body = """\
                    Employee ID  : {:>20}
                    Employee Name: {:>20}
                    Project Code : {:>20}
                    Please log in to our android app to view the application.
                    """.format(emp_code, emp_name, proj_code)
        text_body = textwrap.dedent(text_body)
        return recipients, subject, text_body


def sendRegisterMail(emp_code, email_id, emp_name, proj_code, department, phone_no, emp_category):
    # Email to employee
    sender = "Fractal Analytics"
    subject = "You have registered for FRACTAL VISA APPLICATION process!"
    text_body = """\
                Employee ID  : {:>20}
                Employee Name: {:>20}
                Project_code : {:>20}
                Department   : {:>20}
                Phone No     : {:>20}
                Designation  : {:>20}
                You can now login to our android app using the above credentials!
                """.format(emp_code, emp_name, proj_code, department, phone_no, emp_category)
    text_body = textwrap.dedent(text_body)
    mailNinja.send_email(subject, sender, email_id, text_body, 2)
    # Emaoil notifying manager\

def sendNotifyMail(emp_code, country, visa_type):
    sender = "Fractal Analytics"
    query = "SELECT m.email_id from employee as e, manager as m where \
            e.emp_code = ? AND e.proj_code = m.proj_code"
    result = database.query_db(query, (emp_code,), True)
    manager_email_id = json.loads(helper.row_jsonify(result, True))['email_id']
    subject = "Employee working on your project has applied for VISA!"
    text_body = """\
    Employee ID  : {}
    Country      : {}
    Visa Type    : {}
    Login to our android app to ACCEPT/REJECT the application!
    """.format(emp_code, country, visa_type)
    mailNinja.send_email(subject, sender, manager_email_id, text_body, 2)


def sendAcceptMail(emp_code, access_level):
    query = "SELECT email_id FROM employee where emp_code = ?"
    result = database.query_db(query, (emp_code,), True)
    email = json.loads(helper.row_jsonify(result, True))['email_id']
    subject = "Your application has been accepted by {}".format(access_level)
    text_body = """\
                Hey Employee No {},
                Your application has been accepted by {} and has gone to the next step for approval!
                """.format(emp_code, access_level)
    text_body = textwrap.dedent(text_body)
    sender = "Fractal Analytics"
    mailNinja.send_email(subject, sender, email_id, text_body, 3)


def sendApprovedMail(emp_code):
    query = "SELECT email_id FROM employee where emp_code = ?"
    result = database.query_db(query, (emp_code,), True)
    email = helper.row_jsonify(result, True)['email_id']
    subject = "Your application has been approved!"
    text_body = """\
                Hey Employee No {},
                Congratulations! Your application has been APPROVED!
                """.format(emp_code)
    text_body = textwrap.dedent(text_body)
    sender = "Fractal Analytics"
    mailNinja.send_email(subject, sender, email_id, text_body, 4)


def sendDeniedMail(emp_code, access_level):
        query = "SELECT email_id FROM employee where emp_code = ?"
        result = database.query_db(query, (emp_code,), True)
        email = helper.row_jsonify(result, True)['email_id']
        subject = "Your application has been DENIED by the {}".format(access_level)
        text_body = """\
                    Hey Employee No {},
                    We are sorry to inform you that your application was denied.
                    """.format(emp_code)
        text_body = textwrap.dedent(text_body)
        sender = "Fractal Analytics"
        mailNinja.send_email(subject, sender, email_id, text_body, 4)
