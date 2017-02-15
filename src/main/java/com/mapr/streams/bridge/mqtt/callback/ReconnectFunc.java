package com.mapr.streams.bridge.mqtt.callback;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface ReconnectFunc {
  void execute() throws MqttException;
}
