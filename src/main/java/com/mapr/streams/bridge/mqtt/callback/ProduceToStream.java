package com.mapr.streams.bridge.mqtt.callback;

import com.mapr.streams.producer.StreamsProducer;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProduceToStream implements MqttCallback {
  private static Logger logger = LoggerFactory.getLogger(ProduceToStream.class);

  private final StreamsProducer<String, byte[]> kafkaProducer;

  public ProduceToStream(StreamsProducer<String, byte[]> kafkaProducer) {
    this.kafkaProducer = kafkaProducer;
  }

  @Override
  public void connectionLost(Throwable throwable) throws ConnectionLostException {
    logger.error("Lost connection to MQTT server", throwable);
    throw new ConnectionLostException(throwable);
  }

  @Override
  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
    logger.debug("messageArrived(): {},{}", s, mqttMessage);
    kafkaProducer.write(mqttMessage.getPayload());
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken deliveryToken) {
    logger.debug("deliveryComplete(): {}", deliveryToken);
    //logger.warn("Couldn't get message from IMqttDeliveryToken! got: {}", deliveryToken, e);
  }
}
