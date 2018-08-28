
import time 
import paho.mqtt.client as paho

broker = 'broker.hivemq.com'

client = paho.Client("client1")
client.connect(broker)
client.loop_start()

client.subscribe("flag")
client.subscribe("longi")
client.publish("flag","hello world")
client.publish("cord","good nice")

# time.sleep(4)
# client.disconnect() 
# client.loop_stop()