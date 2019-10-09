FROM openjdk:13-slim

RUN mkdir -p /usr/share/man/man1mkdir -p /usr/share/man/man1 \
  && apt-get update && apt-get install -y \
  ant \
  && rm -rf /var/lib/apt/lists/*

COPY . /opt/subgraphminer

WORKDIR /opt/subgraphminer

RUN ant all \
  && ln -s /data data

ENTRYPOINT ["java", "-jar", "/opt/subgraphminer/build/jar/subgraphmining.jar"]
