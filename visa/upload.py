import os
from flask import Flask, request, redirect, url_for
from werkzeug import secure_filename

ALLOWED_EXTENSIONS = set(['txt', 'pdf', '.doc', '.docx', 'png', 'jpg', 'jpeg', 'gif'])

UPLOADED_DEFAULT_DEST = app.config['UPLOAD_FOLDER']
