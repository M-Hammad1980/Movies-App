package com.app.movies.data.source
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

class MQTTManager {
    private var mqttClient: MqttClient? = null

    fun connectToBroker(brokerUrl: String, clientId: String, username: String, password: String) {
        try {
            mqttClient = MqttClient(brokerUrl, clientId, null)
            val options = MqttConnectOptions()
            options.userName = username
            options.password = password.toCharArray()
            mqttClient?.connect(options /*, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    // Connection success
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    // Connection failure
                }
            }*/)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun publishMessage(topic: String, message: String) {
        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            mqttClient?.publish(topic, mqttMessage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}