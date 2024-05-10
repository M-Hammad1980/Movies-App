package com.app.movies.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.movies.R
import com.app.movies.databinding.FragmentMqttBinding
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.nio.charset.StandardCharsets

class MqttFragment : Fragment() {

    var binding :FragmentMqttBinding ?= null



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

        binding?.apply {
            connectButton.setOnClickListener { connectToBroker() }
            subscribeButton.setOnClickListener { subscribeToTopic() }
            publishButton.setOnClickListener { publishMessage() }
        }

        val serverUri = "tcp://mqtt.eclipse.org:1883"
        val clientId = "AndroidClient"

        mqttAndroidClient = MqttClient( serverUri, clientId)
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String) {
                if (reconnect) {
                    // Reconnection is successful
                } else {
                    // Initial connection
                }
            }

            override fun connectionLost(cause: Throwable?) {
                // Connection lost
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                binding?.publishedMessageTextView?.text = message.toString()
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                // Message delivery complete
            }
        })
    }

    private fun connectToBroker() {
        mqttAndroidClient.connect()
    }

    private fun subscribeToTopic() {
        val topic = binding?.topicEditText?.text.toString()
        mqttAndroidClient.subscribe(topic)
    }

    private fun publishMessage() {
        val topic = binding?.topicEditText?.text.toString()
        val message = binding?.messageEditText?.text.toString()
        val mqttMessage = MqttMessage(message.toByteArray(StandardCharsets.UTF_8))
        mqttAndroidClient.publish(topic, mqttMessage)
    }

}