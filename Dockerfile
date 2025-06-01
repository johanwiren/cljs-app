# Example of custom Java runtime using jlink in a multi-stage container build
FROM eclipse-temurin:21 AS jre-build

# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,java.net.http,java.se,java.compiler \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

# Define your base image
FROM debian:buster-slim AS build
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /javaruntime $JAVA_HOME

# Install clojure
RUN apt-get update && apt-get install -y curl git
RUN curl -L -O https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh
RUN chmod +x linux-install.sh
RUN ./linux-install.sh

# Download deps
COPY deps.edn .
RUN clojure -X:deps prep


COPY build.clj .
RUN clojure -T:build clean

# Continue with your application deployment
COPY . .
# Frontend
RUN clojure -M -m shadow.cljs.devtools.cli release frontend
# Backend with bundled frontend
RUN clojure -T:build uberjar

FROM debian:buster-slim
COPY --from=jre-build /javaruntime $JAVA_HOME
RUN mkdir /opt/app
COPY --from=build target/scw-docker.jar /opt/app/

RUN cd /opt/app

EXPOSE 8080

CMD ["java", "-XX:MaxRAMPercentage=90", "-jar", "/opt/app/scw-docker.jar"]
