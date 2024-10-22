package com.petadoption.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PetAdoptionServer {

    private Server server;

    // Create a dynamic thread pool with a core thread count of 5, a maximum thread count of 20, and an idle thread keep-alive time of 60 seconds
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            5,                   // Core thread count
            20,                  // Maximum thread count
            60L,                 // Keep-alive time when threads are idle
            TimeUnit.SECONDS,     // Time unit in seconds
            new LinkedBlockingQueue<>()  // Task queue
    );

    public void start() throws IOException {
        // Configure the gRPC server using the dynamic thread pool
        server = ServerBuilder.forPort(50051)
                .addService(new PetAdoptionServiceImpl())
                .executor(threadPool)  // Use custom dynamic thread pool
                .build()
                .start();

        System.out.println("Server started, listening on 50051");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down gRPC server");
            PetAdoptionServer.this.stop();
        }));
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
        if (threadPool != null) {
            threadPool.shutdown();  // Shut down the thread pool
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final PetAdoptionServer server = new PetAdoptionServer();
        server.start();
        server.server.awaitTermination();
    }
}
