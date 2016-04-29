import hmac
import requests
from visa import app

def send_email(subject, sender, recipients, text_body, message_id):
    """
    Send email using the Mailgun APIv3
    :param recipients - [list]
    :param message_id - to keep track of db/webhooks
    """
    return requests.post(
        "https://api.mailgun.net/v3/mitshi.in/messages",
        auth=("api", app.config['MAILGUN_API_KEY']),
        data={"from": sender + " <mailgun@mitshi.in>",
              "to": recipients,
              "subject": subject,
              "text": text_body,
              "v:my-custom-data": {"message_id": message_id}})
