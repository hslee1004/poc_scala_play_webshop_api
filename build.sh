#!/bin/bash

unzip play-scala-jupiter-1.0.zip

service supervisord stop
sleep 5

cp -r play-scala-jupiter-1.0/* /home/play_jupiter_shop

rm /home/play_jupiter_shop/RUNNING_PID
service supervisord start

rm -r play-scala-jupiter-1.0
#rm play-scala-jupiter-1.0.zip




