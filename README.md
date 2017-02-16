# mqtt2Kafka

Bridge which consumes MQTT messages and republishes them on MapR Streams / Kafka 0.9 broker, on
the same topic.

- The 'master' branch is compiled to work with MapR Streams. 
- The 'apache-kafka-0.9' branch will work with Apache Kafka 0.9 (TODO).
 
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

    For testing: you can use mosquitto public test server (https://test.mosquitto.org/gauge):
    $ mqtt2kafka -t temp/random --uri tcp://test.mosquitto.org -s <your output stream: /path:topic>

***Notes*** 
- you can't run more than one bridge using the default settings, since two clients cannot connect to the same MQTT server with the same client ID. Additionally, you will get multiple messages published to Kafka for each message published to MQTT. If you wish to run multiple instances, you'll need to divide up the topics among the instances, and make sure to give them different IDs.
- MapR Streams doesn't have brokers, only the stream path and topic is needed: "/path/to/my:topic"
- Tested with the mosquitto MQTT 3.1 server (https://mosquitto.org/)

## Logging
Logging with SLF4J and the slf4j-log4j bridge implementation. There is a default `log4j.properties` file packaged with the jar which simply prints all messages of level `INFO` or greater to the console and level `DEBUG` to a file `mqtt2kafka.log`. 


To use a customized logging config (`log4j.properties`), it's necessary to launch from the jar by using the following command:

    $ java -Dlog4j.configuration=file://</path/to/my/log4j.properties> -jar mqtt2kafka-jar-with-dependencies.jar [options...]
    
*Note: assumes `mqtt2kafka-jar-with-dependencies.jar` is in the current directory.*

## Dependencies
[Paho](http://www.eclipse.org/paho/) and [Kafka](http://kafka.apache.org/) and[]MapR Streams](https://www.mapr.com/products/mapr-streams).
 
For a great intro to MapR Streams please check out [Getting Started with MapR Streams](https://www.mapr.com/blog/getting-started-sample-programs-mapr-streams) by Tug Grall.

## Testing with MapR Sandbox
It's very easy to test this utility using the MapR Sandbox. Please [get it here for free](https://www.mapr.com/products/mapr-sandbox-hadoop).

MapR Streams support the Kafka 0.9 API but with an implementation that has no dependency on brokers. Launch the VM, create a stream to publish to with 2 simple commands and it's ready to go.

On the MQTT side, we can test using the public test.mosquitto.org MQTT server as a data source to subscribe to.

Here is how to do it: 
1. start the sandbox VM on your computer, then open a terminal and logon to the sandbox (typically: `ssh -p 2222 localhost`):
2. Create a stream and topic

        $ maprcli stream create -path /mqttstream
        $ maprcli stream topic create -path /mqttstream -topic test
3. Start piping messages from the test mqtt server to MapR Streams
 
        $ mqtt2kafka --id mqtt2kafka --uri tcp://test.mosquitto.org -t temp/random -s /mqttstream:test
    
    
## Acknowledgements
This project is initially based on the `mqttKafkaBridge` project (https://github.com/jacklund/mqttKafkaBridge). Its last contribution was 4 years ago and it used the now very old Kafka 0.7 API. I initially forked the repo but at this point everything is rewritten and so I feel it makes more sense to create a new repo.

