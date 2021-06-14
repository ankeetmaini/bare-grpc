package com.learn.grpc.client;

import com.learn.grpc.greet.GreetRequest;
import com.learn.grpc.greet.GreetResponse;
import com.learn.grpc.greet.GreetServiceGrpc;
import com.learn.grpc.greet.Greeting;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingBiDirectionalStreamingClient {

	private static GreetRequest createRequest(String name) {
		Greeting greeting = Greeting.newBuilder()
				.setFirstName(name)
				.build();
		GreetRequest request = GreetRequest.newBuilder()
				.setGreeting(greeting)
				.build();

		return request;
	}

	public static void main(String[] args) {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
				.usePlaintext() // only for local testing
				.build();

		// create an async client
		GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);
		CountDownLatch latch = new CountDownLatch(1);
		StreamObserver<GreetRequest> request = asyncClient.greetBiDirectional(new StreamObserver<GreetResponse>() {
			@Override
			public void onNext(GreetResponse value) {
				// server will call it multiple times
				System.out.println("onNext from server");
				System.out.println("response: " + value.getResult());
			}

			@Override
			public void onError(Throwable t) {

			}

			@Override
			public void onCompleted() {
				System.out.println("onCompleted from server");
				latch.countDown();
			}
		});

		request.onNext(createRequest("Tom"));
		request.onNext(createRequest("Dick"));
		request.onNext(createRequest("Harry"));
		request.onNext(createRequest("Barry"));
		request.onNext(createRequest("Larry"));
		request.onCompleted();

		try {
			latch.await(4L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Client shutting down...");
		channel.shutdown();
	}
}
