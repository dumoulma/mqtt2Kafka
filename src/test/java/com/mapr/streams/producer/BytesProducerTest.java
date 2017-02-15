package com.mapr.streams.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BytesProducerTest {

  @Mock
  private KafkaProducer<String, byte[]> mockKafka;

  @Test
  public void givenNewProducer_whenOpen_shouldIncreaseRefCountBy1() {
    BytesProducer p = new BytesProducer("myTopic");
    p.setStreamsProducer(mockKafka);
    p.open();

    assertThat(p.getRefCount(), equalTo(1));
  }

  @Test
  public void givenNewProducer_whenClose_shouldCallCloseOnKafkaProducer() {
    BytesProducer p = new BytesProducer("myTopic");
    p.setStreamsProducer(mockKafka);
    p.open();
    p.close();

    verify(mockKafka).close();
  }

  @Test
  public void whenWriteValue_shouldCallSendOnce() {
    BytesProducer p = new BytesProducer("myTopic");
    p.setStreamsProducer(mockKafka);
    p.open();
    byte[] payload = "TEST".getBytes();
    p.write(payload);

    //noinspection unchecked
    verify(mockKafka).send((ProducerRecord) anyObject());
  }

  @Test
  public void whenWriteValue_shouldCallWithRecordWithNullKey() {
    BytesProducer p = new BytesProducer("TEST");
    p.setStreamsProducer(mockKafka);
    p.open();
    byte[] payload = "TEST".getBytes();
    p.write(payload);

    ProducerRecord<String, byte[]> record = new ProducerRecord<>("TEST", null, payload);
    verify(mockKafka).send(record);
  }

  @Test
  public void whenWriteKeyValue_shouldCallWithRecordWithKeyValue() {
    BytesProducer p = new BytesProducer("TEST");
    p.setStreamsProducer(mockKafka);
    p.open();
    byte[] payload = "TEST".getBytes();
    final String key = "KEY";
    p.write(key, payload);

    ProducerRecord<String, byte[]> record = new ProducerRecord<>("TEST", key, payload);
    verify(mockKafka).send(record);
  }
}