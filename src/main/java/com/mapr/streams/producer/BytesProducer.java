package com.mapr.streams.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implements the StreamsProducer for MapR Streams.
 * <p>
 * This is a thread-safe reference counted implementation.
 */
@SuppressWarnings("unused")
public class BytesProducer implements StreamsProducer<String, byte[]> {
  private static Logger logger = LoggerFactory.getLogger(BytesProducer.class);

  private final AtomicInteger referenceCount = new AtomicInteger(0);
  private Producer<String, byte[]> streamsProducer;
  private String streamTopic;

  public BytesProducer(String streamTopic) {
    this.streamTopic = streamTopic;
    streamsProducer = new KafkaProducer<>(createDefaultProperties());
  }

  private Properties createDefaultProperties() {
    Properties props = new Properties();
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                      StringSerializer.class.getCanonicalName());
    props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                      ByteArraySerializer.class.getCanonicalName());

    return props;
  }

  @Override
  public void write(String key, byte[] value) {
    ProducerRecord<String, byte[]> record;

    if (key != null) {
      record = new ProducerRecord<>(streamTopic, key, value);
    } else {
      record = new ProducerRecord<>(streamTopic, value);
    }

    streamsProducer.send(record);
  }

  @Override
  public void write(byte[] value) {
    write(null, value);
  }

  public void open() {
    referenceCount.incrementAndGet();
  }

  public void close() {
    if (referenceCount.decrementAndGet() <= 0) {
      streamsProducer.close();
    }
  }

  // for testing purposes!
  void setStreamsProducer(Producer<String, byte[]> streamsProducer) {
    this.streamsProducer = streamsProducer;
  }

  int getRefCount() {
    return referenceCount.get();
  }

  @Override
  public String toString() {
    return "BytesProducer{" +
            "referenceCount=" + referenceCount +
            ", streamsProducer=" + streamsProducer +
            ", streamTopic='" + streamTopic + '\'' +
            '}';
  }
}
