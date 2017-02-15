package com.mapr.streams.bridge;

import com.mapr.streams.bridge.mqtt.MqttProperties;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.UUID;

@SuppressWarnings("unused")
public class CommandLineParser {
  private static final int DEFAULT_MOSQUITTO_PORT = 1883;

  @Option(name = "--id", usage = "MQTT Client ID")
  private String clientId = UUID.randomUUID().toString().substring(0, 10);

  @Option(name = "--uri", required = true, usage = "MQTT Server URI)")
  private String serverURI;

  @Option(name = "--port", aliases = "-p", usage = "MQTT Server Port")
  private int serverPort = DEFAULT_MOSQUITTO_PORT;

  @Option(name = "--topic", required = true, aliases = "-t", usage = "MQTT topic")
  private String mqttTopic;

  @Option(name = "--streams-topic", required = true, aliases = "-s", usage = "MapR Streams output " +
          "topic (ex: /path:topic)")
  private String streamsTopic;

  @Option(name = "--user", aliases = "-u", usage = "username")
  private String username = "";

  @Option(name = "--password", aliases = "-pw", usage = "password")
  private String password = "";

  @Option(name = "--help", aliases = "-h", usage = "Show help")
  private boolean showHelp = false;

  private CmdLineParser parser = new CmdLineParser(this);

  MqttProperties getMqttProperties() {
    return new MqttProperties(serverURI, serverPort, clientId, mqttTopic, username, password);
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
    stream.println("mqtt2kafka [options...]");
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
