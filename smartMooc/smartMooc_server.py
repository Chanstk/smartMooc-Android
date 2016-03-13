# coding=utf-8
from flask import Flask, request
import base64
from facepp import API, File

API_KEY = '3d7ee3617d5afbd9623c9bfb9271a9d7'
API_SECRET = 'YULzlXK3F0se3DtGM74KuIFv6DVnsEsF'
api = API(API_KEY, API_SECRET)
app = Flask(__name__)


@app.route('/data/post/', methods=['POST'])
def data_post():
    photos = []
    for i in range(1, 4):
        photos.append(base64.b64decode(request.form[str(i)]))
    imageSaver(photos)
    personID = faceRecogition()
    return personID


@app.route('/')
def hello_world():
    return 'Hello World!'


def imageSaver(photos):
    i = 1
    for photo in photos:
        fout = open(str(i)+'.jpg', 'w')
        i += 1
        fout.write(photo)
        fout.close()


def faceRecogition():
    IDS = [api.detection.detect(img=File(str(i)+'.jpg'), mode = 'oneface') for i in range(1, 4)]
    face_ids = [info['face'][0]['face_id'] for info in IDS]
    person = api.person.create(face_id=face_ids)
    print person['person_id']
    return person['person_id']

if __name__ == '__main__':
   app.run(host='0.0.0.0')

