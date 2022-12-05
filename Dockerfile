FROM gradle:7.4.2-jdk8
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN apt update && apt install -y imagemagick
RUN gradle installDist
ENV PATH="/home/gradle/src/build/install/naima/bin:${PATH}"
WORKDIR /home/gradle/run
ENTRYPOINT naima
