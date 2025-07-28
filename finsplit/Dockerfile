# # Stage 1: Base with Ubuntu + Java 23 + MySQL + Maven
# FROM ubuntu:22.04

# ENV DEBIAN_FRONTEND=noninteractive
# ENV JAVA_HOME=/opt/java/jdk-23
# ENV PATH="$JAVA_HOME/bin:$PATH"
# ENV MYSQL_DATABASE=finsplit
# ENV MYSQL_ROOT_PASSWORD=root

# # Install dependencies: curl, MySQL, Maven, Java dependencies
# RUN apt-get update && apt-get install -y \
#     curl wget gnupg2 unzip mysql-server maven git software-properties-common \
#     && rm -rf /var/lib/apt/lists/*

# # Install Java 23
# RUN mkdir -p /opt/java && \
#     curl -L -o /tmp/jdk.tar.gz https://download.oracle.com/java/23/archive/jdk-23.0.2_linux-aarch64_bin.tar.gz && \
#     tar -xzf /tmp/jdk.tar.gz -C /opt/java && \
#     rm /tmp/jdk.tar.gz

# # Copy Spring Boot source code to image
# COPY . /app
# WORKDIR /app

# # Set proper permissions for MySQL
# RUN mkdir -p /var/run/mysqld && chmod 777 /var/run/mysqld

# ENV JAVA_HOME=/opt/java/jdk-23.0.2
# ENV PATH="${JAVA_HOME}/bin:${PATH}"


# # Build the Spring Boot application (generates target/*.jar)
# RUN mvn clean package

# # Expose ports
# EXPOSE 8080 3306

# # Copy entrypoint script
# COPY docker-entrypoint.sh /docker-entrypoint.sh
# RUN chmod +x /docker-entrypoint.sh

# ENTRYPOINT ["/docker-entrypoint.sh"]


# Stage 1: Ubuntu + Java 23 + MySQL + Maven
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive
ENV MYSQL_DATABASE=finsplit
ENV MYSQL_ROOT_PASSWORD=root

# Install system dependencies
RUN apt-get update && apt-get install -y \
    curl wget gnupg2 unzip mysql-server maven git software-properties-common \
    && rm -rf /var/lib/apt/lists/*

# Install Java 23 (x64 version)
RUN mkdir -p /opt/java && \
    curl -L -o /tmp/jdk.tar.gz https://download.oracle.com/java/23/archive/jdk-23.0.2_linux-x64_bin.tar.gz && \
    tar -xzf /tmp/jdk.tar.gz -C /opt/java && \
    rm /tmp/jdk.tar.gz

ENV JAVA_HOME=/opt/java/jdk-23.0.2
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Setup MySQL permissions
RUN mkdir -p /var/run/mysqld && chmod 777 /var/run/mysqld

# Copy Spring Boot app
COPY . /app
WORKDIR /app

# Build the Spring Boot app
RUN mvn clean package -DskipTests

# Expose app and DB ports
EXPOSE 8080 3306

# Copy and set entrypoint script
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
