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

  private final MqttProperties p;
  private final MqttCallback callback;

  private IMqttClient client;

  public MqttConsumer(MqttProperties p, MqttCallback callback) {
    this.p = p;
    this.callback = callback;
  }

  public void init() throws MqttException {
    try {
      String url = p.getHostname() + ":" + p.getPort();
      logger.info("Opening MQTT connection: '{}'", url);

      MqttConnectOptions connectOptions = new MqttConnectOptions();

      if (!p.getUsername().isEmpty()) {
        connectOptions.setUserName(p.getUsername());
      }
      if (!p.getPassword().isEmpty()) {
        connectOptions.setPassword(p.getPassword().toCharArray());
      }

      client = new MqttClient(url, p.getTopic(), new MemoryPersistence());
      client.setCallback(callback);
      client.connect(connectOptions);
      client.subscribe(p.getTopic());

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
