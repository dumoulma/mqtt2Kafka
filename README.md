# mqtt2Kafka

Bridge which consumes MQTT messages and republishes them on MapR Streams / Kafka 0.9 broker, on
the same topic.

The 'master' branch is compiled to work with Kafka. The 'mapr' branch will work with MapR Streams.
 
## Usage

    $ mqtt2kafka [options...]

Where `options` are:

     --help (-h)              : Show help (default: true)
     --id VAL                 : MQTT Client ID (default: 2c605ceb-3)
     --password (-pw) VAL     : password (default: )
     --port (-p) N            : MQTT Server Port (default: 1883)
     --streams-topic (-s) VAL : MapR Streams output topic (ex: /path:topic)
     --topic (-t) VAL         : MQTT topic
     --uri VAL                : MQTT Server URI)
     --user (-u) VAL          : username (default: )

    *Note: for testing, you can use mosquitto public test server:
    $ mqtt2kafka -t temp/random --uri tcp://test.mosquitto.org -s <your output stream: /path:topic>

If you don't specify any command-line options, it uses the following defaults:

***Notes*** 
- you can't run more than one bridge using the default settings, since two clients cannot connect to the same MQTT server with the same client ID. Additionally, you will get multiple messages published to Kafka for each message published to MQTT. If you wish to run multiple instances, you'll need to divide up the topics among the instances, and make sure to give them different IDs.
- MapR Streams doesn't have brokers, only the stream path and topic is needed: "/path/to/my:topic"

## Logging
Logging with SLF4J and the slf4j-log4j bridge implementation.

[Paho](http://www.eclipse.org/paho/) and [Kafka](http://kafka.apache.org/) libraries it uses. There is a default `log4j.properties` file packaged with the jar which simply prints all messages of level `INFO` or greater to the console. If you want to customize logging, simply create your own `log4j.properties` file, and start up `mqttKafkaBridge` as follows:

    $ java -Dlog4j.configuration=file:///path/to/log4j.properties -jar mqttKafkaBridge.jar [options...]

## Testing that it works
There is a very useful test.mosquitto.org server that is available to test publicly! If we combine with the [MapR Sandbox](https://www.mapr.com/products/mapr-sandbox-hadoop/download) it's possible to easily test this code on a live stream!

start the sandbox VM on your computer, then open a terminal and logon to the sandbox (typically: `ssh -p 2222 localhost`):
 
    # create a stream and topic
    $ maprcli stream create -path /mqttstream
    $ maprcli stream topic create -path /mqttstream -topic test
    
    # start piping messages from the test mqtt server to MapR Streams 
    $ mqtt2kafka --id mqtt2kafka --uri tcp://test.mosquitto.org -t temp/random -s /mqttstream:test
    
    
## Acknowledgements
This project is initially based on the `mqttKafkaBridge` project (https://github.com/jacklund/mqttKafkaBridge). Its last contribution was 4 years ago and it used the now very old Kafka 0.7 API. I initially forked the repo but at this point everything is rewritten and so I feel it makes more sense to create a new repo.

