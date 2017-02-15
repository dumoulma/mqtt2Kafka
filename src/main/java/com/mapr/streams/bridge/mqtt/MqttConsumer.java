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
  private static final int QOS_LEVEL = 0;

  private final MqttProperties p;
  private final MqttCallback callback;

  private IMqttClient client;

  public MqttConsumer(MqttProperties p, MqttCallback callback) {
    this.p = p;
    this.callback = callback;
  }

  public void init() throws MqttException {
    try {
      String url = String.format("%s:%d", p.getHostname(), p.getPort());
      logger.info("Opening MQTT connection: '{}'", url);

      MqttConnectOptions connOpt = new MqttConnectOptions();
      if (!p.getUsername().isEmpty()) {
        connOpt.setUserName(p.getUsername());
      }
      if (!p.getPassword().isEmpty()) {
        connOpt.setPassword(p.getPassword().toCharArray());
      }
      connOpt.setAutomaticReconnect(true);
      connOpt.setKeepAliveInterval(30);
      connOpt.setCleanSession(true);

      client = new MqttClient(url, p.getClientId(), new MemoryPersistence());
      client.setCallback(callback);
      client.connect(connOpt);
      client.subscribe(p.getTopic(), QOS_LEVEL);

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
