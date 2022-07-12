FROM registry.gz.cvte.cn/library/jdk8

ENV HUBBLE_HOME /opt/hugegraph-hubble
WORKDIR ${HUBBLE_HOME}
ADD ./hugegraph-hubble/hugegraph-hubble $HUBBLE_HOME
EXPOSE 8088
ENTRYPOINT ["./bin/start-hubble.sh"]
