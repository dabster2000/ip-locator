FROM resin/rpi-raspbian:wheezy

RUN echo 'deb http://archive.raspberrypi.org/debian/ wheezy main' >> /etc/apt/sources.list.d/raspi.list
RUN apt-get update
RUN apt-get -y upgrade
RUN echo oracle-java8-jdk shared/accepted-oracle-license-v1-1 select true| /usr/bin/debconf-set-selections
RUN apt-get -y --force-yes install oracle-java8-jdk
RUN apt-get clean

ADD out/artifacts/ip_locator_jar/ip-locator.jar /iplocator.jar

CMD java -jar iplocator.jar