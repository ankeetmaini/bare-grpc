package com.learn.grpc.service;

import com.learn.grpc.greet.GreetRequest;
import com.learn.grpc.greet.GreetResponse;
import com.learn.grpc.greet.GreetServiceGrpc;
import com.learn.grpc.greet.Greeting;
import io.grpc.stub.StreamObserver;

import java.util.stream.IntStream;

public class GreetingServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {
	@Override
	public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
		Greeting greeting = request.getGreeting();
		String firstName = greeting.getFirstName();
		String lastName = greeting.getLastName();

		String result = "Hello! " + firstName + " " + lastName;

		GreetResponse response = GreetResponse.newBuilder()
				.setResult(result)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void greetStreaming(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
		Greeting greeting = request.getGreeting();
		String firstName = greeting.getFirstName();
		String lastName = greeting.getLastName();

		String result = "Hello! " + firstName + " " + lastName;

		for (int i = 0; i < 50; i++) {
			GreetResponse response = GreetResponse.newBuilder()
					.setResult(result + ", position: " + i)
					.build();

			responseObserver.onNext(response);

			// interrupt
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		responseObserver.onCompleted();
	}
}
