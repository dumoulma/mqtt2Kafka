# mqtt2Kafka

Bridge which consumes MQTT messages and republishes them on MapR Streams / Kafka 0.9 broker, on
the same topic.

The 'master' branch is compiled to work with Kafka. The 'mapr' branch will work with MapR Streams.
 
## Usage

    $ mqtt-kafka-bridge [options...]

Where `options` are:

     --help (-h)             : Show help (default: false)
     --id VAL                : MQTT Client ID (default: mqttKafkaBridge)
     --input-topic (-i) VAL  : MQTT input topic (default: temp/random)
     --output-topic (-o) VAL : MapR Streams output topic (default:
                               /streams/my-stream:topic)
     --password (-pw) VAL    : password (default: temp/random)
     --port (-p) VAL         : MQTT Server Port (default: 1883)
     --uri VAL               : MQTT Server URI
     --user (-u) VAL         : username (default: temp/random)

*Note: test mosquitto server@ tcp://test.mosquitto.org:1883

If you don't specify any command-line options, it uses the following defaults:

***Notes*** 
- you can't run more than one bridge using the default settings, since two clients cannot connect to the same MQTT server with the same client ID. Additionally, you will get multiple messages published to Kafka for each message published to MQTT. If you wish to run multiple instances, you'll need to divide up the topics among the instances, and make sure to give them different IDs.
- MapR Streams doesn't have brokers, only the stream path and topic is needed: "/path/to/my:topic"

## Logging
Logging with SLF4J and the slf4j-log4j bridge implementation.

[Paho](http://www.eclipse.org/paho/) and [Kafka](http://kafka.apache.org/) libraries it uses. There is a default `log4j.properties` file packaged with the jar which simply prints all messages of level `INFO` or greater to the console. If you want to customize logging, simply create your own `log4j.properties` file, and start up `mqttKafkaBridge` as follows:

    $ java -Dlog4j.configuration=file:///path/to/log4j.properties -jar mqttKafkaBridge.jar [options...]

## Acknowledgements
This project is initially based on the `mqttKafkaBridge` project (https://github.com/jacklund/mqttKafkaBridge). Its last contribution was 4 years ago and it used the now very old Kafka 0.7 API. I initially forked the repo but at this point everything is rewritten and so I feel it makes more sense to create a new repo.

