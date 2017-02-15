package com.mapr.streams.producer;

public interface StreamsProducer<K, V> extends AutoCloseable {

  /**
   * Produces a record with the given key:value.
   */
  void write(K key, V value);

  /**
   * Produces a record with a null key.
   */
  void write(V value);

  /**
   * Initializes the producer (required)
   */
  void open();

  @Override
  void close();
}
