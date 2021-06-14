package com.learn.grpc.service;

import com.learn.grpc.greet.GreetRequest;
import com.learn.grpc.greet.GreetResponse;
import com.learn.grpc.greet.GreetServiceGrpc;
import com.learn.grpc.greet.Greeting;
import io.grpc.stub.StreamObserver;

import java.util.stream.IntStream;

public class GreetingServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {
	private GreetResponse createResponse (String result) {
		GreetResponse response = GreetResponse.newBuilder()
				.setResult("bye " + result)
				.build();
		return response;

	}
	@Override
	public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
		Greeting greeting = request.getGreeting();
		String firstName = greeting.getFirstName();
		String lastName = greeting.getLastName();

		String result = "Hello! " + firstName + " " + lastName;

		responseObserver.onNext(createResponse(result));
		responseObserver.onCompleted();
	}

	@Override
	public void greetStreaming(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
		Greeting greeting = request.getGreeting();
		String firstName = greeting.getFirstName();
		String lastName = greeting.getLastName();

		String result = "Hello! " + firstName + " " + lastName;

		for (int i = 0; i < 50; i++) {


			responseObserver.onNext(createResponse(result + ", position: " + i));

			// interrupt
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		responseObserver.onCompleted();
	}

	@Override
	public StreamObserver<GreetRequest> greetClientStreaming(StreamObserver<GreetResponse> responseObserver) {
		// creating a stream observer for the client
		// as client is sending multiple messages
		StreamObserver<GreetRequest> requestObserver = new StreamObserver<GreetRequest>() {
			String result = "";
			@Override
			public void onNext(GreetRequest value) {
				// will be called by client multiple times
				result += "Got " + value.getGreeting().getFirstName();
				System.out.println("server: " + value.getGreeting().getFirstName());
			}

			@Override
			public void onError(Throwable t) {

			}

			@Override
			public void onCompleted() {
				// client says, it's done
				// then we plug in our response

				responseObserver.onNext(createResponse(result));
				responseObserver.onCompleted();
			}
		};

		return requestObserver;
	}

	@Override
	public StreamObserver<GreetRequest> greetBiDirectional(StreamObserver<GreetResponse> responseObserver) {
		StreamObserver<GreetRequest> request = new StreamObserver<GreetRequest>() {
			@Override
			public void onNext(GreetRequest value) {
				// client will call multiple times
				// as both are streaming
				System.out.println("client sent: " + value.getGreeting().getFirstName());
				responseObserver.onNext(createResponse("got " + value.getGreeting().getFirstName()));
			}

			@Override
			public void onError(Throwable t) {

			}

			@Override
			public void onCompleted() {
				responseObserver.onNext(createResponse("cool, thanks client."));
				responseObserver.onCompleted();

			}
		};

		return request;
	}
}
