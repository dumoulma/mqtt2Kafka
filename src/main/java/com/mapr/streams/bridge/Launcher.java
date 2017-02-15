package com.mapr.streams.bridge;

import com.mapr.streams.bridge.mqtt.MqttConsumer;
import com.mapr.streams.bridge.mqtt.callback.ConnectionLostException;
import com.mapr.streams.bridge.mqtt.callback.ProduceToStream;
import com.mapr.streams.producer.BytesProducer;
import com.mapr.streams.producer.StreamsProducer;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connects a MQTT topic with a MapR Stream.
 */
public class Launcher {
  private static Logger logger = LoggerFactory.getLogger(Launcher.class);
  private static boolean keepRunning = true;

  public static void main(String[] args) {
    CommandLineParser parser = new CommandLineParser();
    parser.parse(args);
    logger.debug("Got: {}", parser);
    try (StreamsProducer<String, byte[]> producer = new BytesProducer(parser.getStreamsTopic())) {
      ProduceToStream producerCallback = new ProduceToStream(producer);
      MqttConsumer mqtt = new MqttConsumer(parser.getMqttProperties(), producerCallback);
      logger.info("Initialization OK! Connecting to MQTT...");
      Runtime.getRuntime().addShutdownHook(new Thread(Launcher::stop));
      while (keepRunning) {
        try {
          mqtt.init();
          logger.info("Connected to MQTT server, resuming");
        } catch (MqttException | ConnectionLostException e) {
          logger.warn("Reconnect failed, retrying in 10 seconds. msg={}", e.getMessage(), e);
        }
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          logger.error("Sleep while waiting to reconnect interrupted!", e);
        }
      }
      logger.info("Work done!");
    }
  }

  private static void stop() {
    keepRunning = false;
  }
}
