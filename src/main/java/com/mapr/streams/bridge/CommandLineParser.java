package com.mapr.streams.bridge;

import com.mapr.streams.bridge.mqtt.MqttProperties;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.OutputStream;
import java.io.PrintStream;

public class CommandLineParser {
  private static final String DEFAULT_MQTT_TOPIC = "temp/random";
  private static final String DEFAULT_MQTT_SERVER_URI = "tcp://test.mosquitto.org";
  private static final String DEFAULT_PORT = "1883";
  private static final String DEFAULT_STREAMS_TOPIC = "/streams/my-stream:topic";

  @Option(name = "--id", usage = "MQTT Client ID")
  private String clientId = "mqttKafkaBridge";

  @Option(name = "--uri", required = true, usage = "MQTT Server URI")
  private String serverURI = DEFAULT_MQTT_SERVER_URI;

  @Option(name = "--port", required = true, aliases = "-p", usage = "MQTT Server Port (default: " +
          "1883)")
  private String serverPort = DEFAULT_PORT;

  @Option(name = "--input-topic", aliases = "-i", usage = "MQTT input topic")
  private String mqttTopic = DEFAULT_MQTT_TOPIC;

  @Option(name = "--output-topic", aliases = "-o", usage = "MapR Streams output topic")
  private String streamsTopic = DEFAULT_STREAMS_TOPIC;

  @Option(name = "--user", aliases = "-u", usage = "username")
  private String username = DEFAULT_MQTT_TOPIC;

  @Option(name = "--password", aliases = "-pw", usage = "password")
  private String password = DEFAULT_MQTT_TOPIC;

  @Option(name = "--help", aliases = "-h", usage = "Show help")
  private boolean showHelp = false;

  private CmdLineParser parser = new CmdLineParser(this);

  MqttProperties getMqttProperties() {
    return new MqttProperties(serverURI, serverPort, username, password, clientId, mqttTopic);
  }

  String getStreamsTopic() {
    return streamsTopic;
  }

  void parse(String[] args) {
    boolean hadCmdLineException = false;
    try {
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      System.err.println(e.getMessage());
      hadCmdLineException = true;
    }
    if (showHelp || hadCmdLineException) {
      printUsage(System.out);
      System.exit(0);
    }
  }

  private void printUsage(OutputStream out) {
    PrintStream stream = new PrintStream(out);
    stream.println("mqtt-kafka-bridge [options...]");
    parser.printUsage(out);
    stream.println("\n*Note: test mosquitto server@ tcp://test.mosquitto.org:1883");
  }

  @Override
  public String toString() {
    return "CommandLineParser{" +
            "clientId='" + clientId + '\'' +
            ", serverURI='" + serverURI + '\'' +
            ", serverPort='" + serverPort + '\'' +
            ", mqttTopic='" + mqttTopic + '\'' +
            ", streamsTopic='" + streamsTopic + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", showHelp=" + showHelp +
            ", parser=" + parser +
            '}';
  }
}
