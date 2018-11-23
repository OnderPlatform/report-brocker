#!/usr/bin/env sh

sudo ./start \
    -Dplay.http.secret.key='art2$5sadfITbG@' \
    -Dhttp.port=80 \
    -Dlogger.config=/home/ubuntu/report-broker/log4j.xml