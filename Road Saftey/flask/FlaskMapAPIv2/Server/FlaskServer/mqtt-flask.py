from flask import Flask, jsonify, render_template, request
from flask_mqtt import Mqtt
import time
import paho.mqtt.client as paho


cord = 0
lat = 115
longi = 15
flag = 1
ff = str("%s %s %d" %(lat,longi,flag))



app = Flask(__name__)
app.config['MQTT_BROKER_URL'] = 'broker.hivemq.com'
app.config['MQTT_BROKER_PORT'] = 1883
# app.config['MQTT_USERNAME'] = 'bnaywdds'
# app.config['MQTT_PASSWORD'] = '2qBPEapeV_er'
app.config['MQTT_REFRESH_TIME'] = 1  
mqtt = Mqtt(app)


# def on_publish(client,userdata,msg):
#     mqtt.publish('coor3',ff)
 
@mqtt.on_connect()
def handle_connect(client, userdata, flags, rc):
    # mqtt.subscribe('cord1')
    # mqtt.subscribe('ff')
    mqtt.subscribe('test')
    mqtt.publish('coor3',ff)
    # mqtt.on_publish = on_publish
    # mqtt.publish('lat',lat)
    # mqtt.publish("longi",longi)

    
@mqtt.on_message()
def handle_mqtt_message(client, userdata, message):
 
    topic = message.topic
    if topic == 'cord1':
        cord = str(message.payload.decode())
        print(cord)
    if topic == 'flag':
        flag = str(message.payload.decode())
        print(flag)


# @app.route('/_stuff', methods = ['GET'])
# def stuff():
#     global lat
#     global longi

    
#     return jsonify(lat=lat,longi=longi)


@app.route('/')
def index():
    return render_template('index.html')

if __name__ == '__main__':
    app.run(host = '10.1.75.187',port=1883)


