FROM gradle:7.4.2-jdk8
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle installDist
ENV PATH="/home/gradle/src/build/install/naima/bin:${PATH}"
WORKDIR /home/gradle/run