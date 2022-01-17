FROM kaiwinter/docker-java8-maven
COPY src /root/algorithm/src/
COPY pom.xml /root/algorithm/
RUN ls -la /root/algorithm/*
RUN cd ~/algorithm/ && \ 
mvn clean install 
RUN rm -rf /root/*
EXPOSE 21
