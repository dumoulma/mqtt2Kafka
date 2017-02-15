package com.mapr.streams.bridge.mqtt;

@SuppressWarnings({"WeakerAccess", "unused"})
public class MqttProperties {

    protected static final String PREFIX = "services.mqtt";

    private String hostname;
    private String port;
    private String username;
    private String password;
    private String clientName;
    private String topic;

    public MqttProperties() {
    }

    public MqttProperties(String hostname, String port, String username, String password,
                          String clientName, String topic) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.clientName = clientName;
        this.topic = topic;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getClientName() {
        return clientName;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "MqttProperties{" +
                "hostname='" + hostname + '\'' +
                ", port='" + port + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", clientName='" + clientName + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }
}