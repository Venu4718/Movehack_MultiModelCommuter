from flask import Flask, jsonify, render_template, request
from flask_mqtt import Mqtt
import time
import paho.mqtt.client as paho

app = Flask(__name__)
app.config['MQTT_BROKER_URL'] = '10.1.76.46'
app.config['MQTT_BROKER_PORT'] = 1883
# app.config['MQTT_USERNAME'] = 'bnaywdds'
# app.config['MQTT_PASSWORD'] = '2qBPEapeV_er'
app.config['MQTT_REFRESH_TIME'] = 100  
mqtt = Mqtt(app)

@mqtt.on_connect()
def handle_connect(client, userdata, flags, rc):
    mqtt.subscribe('lat')

    
@mqtt.on_message()
def handle_mqtt_message(client, userdata, message):
    print("received a message",message)
    global lat
    global longi
    topic = message.topic
    if topic == 'lat':
        lat = float(message.payload.decode())
        print(lat)
    if topic == 'longi':
        longi = float(message.payload.decode())
        print(longi)

if __name__ == '__main__':
    
    app.run(host = '10.1.75.187',port=8000)