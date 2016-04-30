"""Helper functions for notifications"""
import textwrap
from visa import database, mailNinja

def getNotificationMail(source, sender):
    if source.lower() == 'employee':
        query = "SELECT m.email_id from employee e, manager m where e.emp_id = ? and e.proj_code = m.proj_code LIMIT 1"
        result = database.query_db(query, sender, True)
        recipients = list(result)
        subject = "Employee Number {} has applied for visa registration.".format(sender)
        text_body = """\
                    Employee ID: {{emp_id}:>20}
                    Employee Name: {{name}:>20}
                    Project Code: {{proj_code}:>20}

                    Please log in to our android app to view the application.
                    """
        text_body = textwrap.dedent(text_body)
        return recipients, subject, text_body

def sendRegisterMail(emp_code, email_id, emp_name, proj_code, department, phone_no, emp_category):
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
    sender = "Fractal Analytics"
    mailNinja.send_email(subject, sender, email_id, text_body, 2)

#curl -d "proj_code=2&emp_name=lol&email_id=miteshninja@gmail.com&phone_no=3&department=lol&emp_category=admin&password=lol" http://127.0.0.1:8181/api/register
