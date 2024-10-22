# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the generated JAR file into the container
COPY ./build/libs/grpc-pet-adoption-server-1.0-SNAPSHOT.jar /app/grpc-pet-adoption-server.jar

# Expose the gRPC server port
EXPOSE 50051

# Define the command to start the gRPC server
CMD ["java", "-jar", "/app/grpc-pet-adoption-server.jar"]
