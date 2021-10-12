# flink-kafka-streaming

flink streaming job with kafka boilerplate code.

# TODO:
# Try flink windowing , stateful processing
## Pre-requisites

1. Install jdk 8
2. Download and unzip Kafka 2.2 into <ROOT_DIR>
3. Run below commands from <ROOT_DIR>\<KAFKA_ROOT_DIR>

```
    ../kafka_2.12-2.2.2/bin/zookeeper-server-start.sh ../kafka_2.12-2.2.2/config/zookeeper.properties
    ../kafka_2.12-2.2.2/bin/kafka-server-start.sh ../kafka_2.12-2.2.2/config/server.properties
```

4. Create 2 topics in Kafka using the following commands

```
    ../kafka_2.12-2.2.2/bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 6 --topic raw
    ../kafka_2.12-2.2.2/bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 6 --topic valid
    ../kafka_2.12-2.2.2/bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 6 --topic downstream
```

5. Download and unzip flink 1.13.1 into <ROOT_DIR>. Run below command to start flink cluster from <FLINK_ROOT_DIR>.
```
   ../flink-1.13.1/bin/start-cluster.sh
   The [flink console](http://localhost:8081/) can be accessed once the flink cluster is started.
```

6. Clone this repo into <ROOT_DIR> and cd flink-kafka-streaming to build and submit job using below commands
```
   1. mvn clean package
   2. ../flink-1.13.1/bin/flink run ../flink-kafka-streaming/target/flink-kafka-streaming-1.0-SNAPSHOT.jar
```

7. Add messages to the raw topic by issuing the following commands from <ROOT_DIR>\<KAFKA_ROOT_DIR>.

```
../kafka_2.12-2.2.2/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic raw
Add the following text as input ABCD
```

8. Use a console consumer on the valid topic to see the messages that have been processed by the streaming job.

```
../kafka_2.12-2.2.2/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic valid --from-beginning
```

## Understanding the source code

```
**BaseStreaming** has the simple boilerplate code for the Kafka serializer and de-serializer.  
**StreamingJob** has the simple boilerplate code for a flink job. Call required methods from main to explore different types of jobs.
**CaseHandlerProcessFunction** is a simple process function that splits the incoming data by space and to lowercase
**KeyPrefixHandlerProcessFunction** is process function that splits the uncoming data by space and takes first 5 characters as key.
**application.conf** has the input and output topic names that can be configured. It defaults to raw (input topic) and
valid (output topic) along with other configuration

```

## Benchmarking the code on your workstation

1. Insert messages into the raw topic using the below command. The below command would insert 10 mil messages of 100
   chars to the raw topic

```
../kafka_2.12-2.2.2/bin/kafka-producer-perf-test.sh --topic raw --num-records 10000000 --record-size 100 --throughput 5000000 --producer-props bootstrap.servers=localhost:9092
```

2. Execute the program so that it starts streaming from raw to valid.
3. You can look at the lag for the consumer group (stream1 by default, can be changed in application.properties) of the
   valid topic every 10 seconds (using watch command) by executing the following command

```
   ../kafka_2.12-2.2.2/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group stream1
   ../kafka_2.12-2.2.2/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --group keyprefix
```