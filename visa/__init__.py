from flask import Flask
from flask.ext.cors import CORS

app = Flask(__name__)
CORS(app)
app.config.from_object('config')
app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024

from visa import views
