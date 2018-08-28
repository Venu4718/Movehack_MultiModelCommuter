import paho.mqtt.client as mqtt

def on_connect(mqttc, userdata, rc):
    print('connected...rc=' + str(rc))
    mqttc.publish('lat',181)

def on_disconnect(mqttc, userdata, rc):
    print('disconnected...rc=' + str(rc))

def on_message(mqttc, userdata, msg):
    print('message received...')
    print('topic: ' + msg.topic + ', qos: ' + 
          str(msg.qos) + ', message: ' + str(msg.payload))

def on_publish(mqttc, userdata, mid):
    print('message published')
    mqttc.disconnect()

mqttc = mqtt.Client()
mqttc.on_connect = on_connect
mqttc.on_disconnect = on_disconnect
mqttc.on_message = on_message
mqttc.on_publish = on_publish
mqttc.connect(host='10.1.75.187', port=1883)
mqttc.loop_forever()