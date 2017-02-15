package com.mapr.streams.bridge.mqtt;

public class MqttProperties {
  private final String hostname;
  private final int port;
  private final String username;
  private final String password;
  private final String clientId;
  private final String topic;

  public MqttProperties(String hostname, int port, String clientId, String topic,
                        String username, String password) {
    this.hostname = hostname;
    this.port = port;
    this.username = username;
    this.password = password;
    this.clientId = clientId;
    this.topic = topic;
  }

  String getHostname() {
    return hostname;
  }

  int getPort() {
    return port;
  }

  String getUsername() {
    return username;
  }

  String getPassword() {
    return password;
  }

  String getClientId() {
    return clientId;
  }

  String getTopic() {
    return topic;
  }

  @Override
  public String toString() {
    return "MqttProperties{" +
            "hostname='" + hostname + '\'' +
            ", port='" + port + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", clientId='" + clientId + '\'' +
            ", clientId='" + clientId + '\'' +
            ", topic='" + topic + '\'' +
            '}';
  }
}
