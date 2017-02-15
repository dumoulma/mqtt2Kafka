package com.mapr.streams.bridge.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttConsumer implements AutoCloseable {
  private static final Logger logger = LoggerFactory.getLogger(MqttConsumer.class);

  private final MqttProperties mqttProperties;
  private final MqttCallback callback;

  private IMqttClient client;

  public MqttConsumer(MqttProperties mqttProperties, MqttCallback callback) {
    this.mqttProperties = mqttProperties;
    this.callback = callback;
  }

  public void init() throws MqttException {
    try {
      String url = mqttProperties.getHostname() + ":" + mqttProperties.getPort();
      logger.info("Opening MQTT connection: '{}'", url);

      MqttConnectOptions connectOptions = new MqttConnectOptions();
      connectOptions.setUserName(mqttProperties.getUsername());
      connectOptions.setPassword(mqttProperties.getPassword().toCharArray());

      client = new MqttClient(url, mqttProperties.getTopic(), new MemoryPersistence());
      client.setCallback(callback);
      client.connect(connectOptions);
      client.subscribe(mqttProperties.getTopic());

    } catch (MqttException e) {
      logger.error(e.getMessage(), e);
      throw e;
    }
    logger.info("Init OK!");
  }

  @Override
  public void close() {
    if (client != null) {
      try {
        logger.debug("Closing MQTT connection.");
        client.disconnect();
      } catch (MqttException e) {
        logger.error(e.getMessage(), e);
      }
    }
  }
}