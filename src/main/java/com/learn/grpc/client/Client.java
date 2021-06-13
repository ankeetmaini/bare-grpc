package com.learn.grpc.client;

import com.learn.grpc.greet.GreetRequest;
import com.learn.grpc.greet.GreetResponse;
import com.learn.grpc.greet.GreetServiceGrpc;
import com.learn.grpc.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {
	public static void main(String[] args) {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
				.usePlaintext() // only for local testing
				.build();

		GreetServiceGrpc.GreetServiceBlockingStub syncClient = GreetServiceGrpc.newBlockingStub(channel);

		Greeting greeting = Greeting.newBuilder()
				.setFirstName("Donald")
				.setLastName("Duck")
				.build();
		GreetRequest request = GreetRequest.newBuilder()
				.setGreeting(greeting)
				.build();

		GreetResponse response = syncClient.greet(request);
		System.out.println(response.getResult());

		channel.shutdown();
	}
}
