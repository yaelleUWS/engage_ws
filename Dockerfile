FROM ubuntu:14.04
MAINTAINER yaelle.chaudy@uws.ac.uk

# Update Ubuntu
RUN apt-get update

# Install dependencies
RUN apt-get install -y maven openjdk-7-jdk openjdk-7-jre-headless openjdk-7-jre-lib

# Set JAVA HOME
ENV JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64

# Install maven dependencies
ADD lib /opt/WebService/lib
ADD install.sh /opt/WebService/install.sh
RUN cd /opt/WebService; ./install.sh

#ADD pom.xml /opt/WebService/
#RUN cd /opt/WebService; mvn clean install

# Add code inside the container
ADD . /opt/WebService

RUN cd /opt/WebService; mvn clean install

# open ports
EXPOSE 8080
WORKDIR /opt/WebService

CMD mvn clean install exec:exec