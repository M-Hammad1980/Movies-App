package com.app.movies.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.movies.databinding.FragmentMqttBinding
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttFragment : Fragment() {

    var binding :FragmentMqttBinding ?= null


    private var mqttClient: MqttClient? = null
//    private val broker = "tcp://mqtt.eclipse.org:1883" // Change this to your MQTT broker
    private val broker = "tcp://test.mosquitto.org:1883" // Change this to your MQTT broker
    private val clientId = MqttClient.generateClientId()
//    val clientId = "AndroidClient"

    private var topic: String? = null


    private lateinit var mqttAndroidClient: MqttClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMqttBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        try {
            mqttClient = MqttClient(broker, clientId, MemoryPersistence())
            mqttClient?.setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    // Handle connection lost
                    Log.e("mqtt**", "connectionLost: ", )
                }

                @Throws(Exception::class)
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    // Handle received message
                    Log.e("mqtt**", "messageArrived: ", )
                    message?.let {
                        val payload = String(it.payload)
                        updateReceivedMessage(payload)
                    }
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.e("mqtt**", "deliveryComplete: ", )
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("mqtt**", "catch: ${e.message}", )

        }

        binding?.apply {
            connectButton.setOnClickListener { connectToBroker() }
            subscribeButton.setOnClickListener { subscribeToTopic() }
            publishButton.setOnClickListener { publishMessage() }
        }
    }

    private fun connectToBroker() {
        val options = MqttConnectOptions()
        options.isCleanSession = true
        try {
            mqttClient?.connect(options)
        } catch (e: Exception) {

            Log.e("mqtt**", "connectToBroker catch: ${e.message}", )
            Log.e("mqtt**", "connectToBroker catch: ${e.cause}", )
            e.printStackTrace()
        }
    }

    private fun subscribeToTopic() {
        val topicEditText = binding?.topicEditText
        topic = topicEditText?.text.toString()
        try {
            mqttClient?.subscribe(topic)
        } catch (e: Exception) {
            Log.e("mqtt**", "subscribeToTopic catch: ${e.cause}", )
            e.printStackTrace()
        }
    }

    private fun publishMessage() {
        val messageEditText = binding?.messageEditText
        val message = messageEditText?.text.toString()
        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            mqttClient?.publish(topic, mqttMessage)
        } catch (e: Exception) {
            Log.e("mqtt**", "publishMessage catch: ${e.message}", )
            e.printStackTrace()
        }
    }

    private fun updateReceivedMessage(message: String) {
        activity?.runOnUiThread {
            binding?.publishedMessageTextView?.text = message
        }
    }

}