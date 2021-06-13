package com.learn.grpc.service;

import com.learn.grpc.greet.GreetRequest;
import com.learn.grpc.greet.GreetResponse;
import com.learn.grpc.greet.GreetServiceGrpc;
import com.learn.grpc.greet.Greeting;
import io.grpc.stub.StreamObserver;

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
}
