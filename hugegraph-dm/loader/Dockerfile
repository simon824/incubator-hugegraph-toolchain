FROM registry.gz.cvte.cn/library/jdk8

ENV LOADER_HOME /opt/hugegraph-loader
WORKDIR ${LOADER_HOME}
ADD ./hugegraph-loader/hugegraph-loader-1.0.0 $LOADER_HOME

ENTRYPOINT ["./bin/start-hubble.sh"]
