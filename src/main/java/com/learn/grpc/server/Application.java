package com.learn.grpc.server;

import com.learn.grpc.service.GreetingServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Application {
	public static void main(String[] args) throws InterruptedException, IOException {
		Server server = ServerBuilder.forPort(50051)
				.addService(new GreetingServiceImpl())
				.build();

		server.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			server.shutdown();
		}));

		server.awaitTermination();
	}
}
