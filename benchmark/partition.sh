#!/bin/bash

docker exec -it kafka1 kafka-topics.sh --alter --topic user.mail --partitions 3 --bootstrap-server localhost:9092