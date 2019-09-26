FROM openjdk:13-slim

MAINTAINER  Pieter Moris <author@email.com>

RUN mkdir -p /usr/share/man/man1mkdir -p /usr/share/man/man1 \
    && apt-get update && apt-get install -y \
    ant \
    && rm -rf /var/lib/apt/lists/* \
  && groupadd -r miner && useradd -r -g miner miner

# ARG UID=1000
# ARG GID=1000
# RUN useradd -m -u $UID -g $GID -o -s /bin/bash miner
# docker build --build-arg UID=$(id -u) --build-arg GID=$(id -g) \
#   -f bb.dockerfile -t testimg .

COPY --chown=miner:miner . /opt/miner

WORKDIR /opt/miner

USER miner

RUN ant all \
  && mkdir data

# ENTRYPOINT ["java", "-jar", "/opt/miner/build/jar/subgraphmining.jar"]

CMD ["java", "-jar", "/opt/miner/build/jar/subgraphmining.jar"]

# ENTRYPOINT is required in order to pass arguments. CMD doesn't seem to work, just temporary to overwrite run with bash.

# bind mount data dir is owned by root inside container and on host, even after creating it first in container and even after chowning it.

# docker build -f docker/Dockerfile -t mules .


# docker run -it -v /media/pieter/DATA/Wetenschap/Doctoraat/projects/subgraphmining/subgraph-miner-git/dockeroutvol:/opt/miner/data --rm mules --graph datasets/example/example_graph.txt --labels datasets/example/example_labels.txt --interest datasets/example/example_vertexset.txt --support 2 -maxsize 4 --algorithm base --verbose --out data/testdockeroutput.txt
