package com.learn.grpc.client;

import com.learn.grpc.sum.SumRequest;
import com.learn.grpc.sum.SumResponse;
import com.learn.grpc.sum.SumServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SumClient {
	public static void main(String[] args) {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
				.usePlaintext()
				.build();

		SumServiceGrpc.SumServiceBlockingStub syncClient = SumServiceGrpc.newBlockingStub(channel);
		SumRequest request = SumRequest.newBuilder()
				.setFirstNumber(10)
				.setSecondNumber(990)
				.build();
		SumResponse response = syncClient.sum(request);
		System.out.println("Result from sum-service: " + response.getResult());

	}

}
