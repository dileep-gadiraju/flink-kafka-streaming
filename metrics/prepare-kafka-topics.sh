../kafka_2.12-2.2.2/bin/kafka-topics.sh --delete  --zookeeper localhost:2181 --topic raw
../kafka_2.12-2.2.2/bin/kafka-topics.sh --delete  --zookeeper localhost:2181 --topic valid
../kafka_2.12-2.2.2/bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 6 --topic raw
../kafka_2.12-2.2.2/bin/kafka-topics.sh --create  --zookeeper localhost:2181 --replication-factor 1 --partitions 6 --topic valid

export KAFKA_HEAP_OPTS="-Xmx2G -Xms1G"
../kafka_2.12-2.2.2/bin/kafka-producer-perf-test.sh --topic raw --payload-file ./data.txt  --num-records 10000000 --throughput 5000000 --producer-props bootstrap.servers=localhost:9092 --payload-delimiter ,

