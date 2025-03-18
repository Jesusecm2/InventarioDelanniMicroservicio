FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
ADD ./target/microservicio-inventario-1.0.jar microservicio-inventario-1.0.jar
RUN apk add --no-cache tzdata
ENV JAVA_OPTS="-Djava.awt.headless=true"
ENV TZ=America/Caracas
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apk add --no-cache msttcorefonts-installer fontconfig
RUN update-ms-fonts
RUN apk add ttf-dejavu

ENTRYPOINT [ "java","-jar","microservicio-inventario-1.0.jar" ]