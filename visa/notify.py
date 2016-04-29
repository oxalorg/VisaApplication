"""Helper functions for notifications"""
import textwrap
from visa import database

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
