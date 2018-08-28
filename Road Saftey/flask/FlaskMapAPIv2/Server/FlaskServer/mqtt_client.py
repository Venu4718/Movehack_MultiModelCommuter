import paho.mqtt.client as mqtt


def on_connect(mqttc, userdata, rc):
    print('connected...rc=' + str(rc))
    mqttc.subscribe(topic='device/#', qos=0)

def on_disconnect(mqttc, userdata, rc):
    print('disconnected...rc=' + str(rc))

def on_message(mqttc, userdata, msg):
    print('message received...')
    print('topic: ' + msg.topic + ', qos: ' + 
          str(msg.qos) + ', message: ' + str(msg.payload))
    

def on_subscribe(mqttc, userdata, mid, granted_qos):
    print('subscribed (qos=' + str(granted_qos) + ')')

def on_unsubscribe(mqttc, userdata, mid, granted_qos):
    print('unsubscribed (qos=' + str(granted_qos) + ')')


print("active..")

mqttc = mqtt.Client()
mqttc.on_connect = on_connect
print("connected")
mqttc.on_disconnect = on_disconnect
print("disconnected")
mqttc.on_message = on_message
print("received message")
mqttc.on_subscribe = on_subscribe
print("subscribe")

mqttc.on_unsubscribe = on_unsubscribe
print("unsubscribe")
mqttc.connect(host='10.1.75.187', port=1883)
mqttc.loop_forever()