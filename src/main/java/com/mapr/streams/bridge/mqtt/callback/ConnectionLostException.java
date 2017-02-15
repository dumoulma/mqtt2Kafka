package com.mapr.streams.bridge.mqtt.callback;

public class ConnectionLostException extends RuntimeException {
  public ConnectionLostException(Throwable cause) {
    super("Connection lost!", cause);
  }
}
