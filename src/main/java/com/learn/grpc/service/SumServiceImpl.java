package com.learn.grpc.service;

import com.learn.grpc.sum.SumRequest;
import com.learn.grpc.sum.SumResponse;
import com.learn.grpc.sum.SumServiceGrpc;
import io.grpc.stub.StreamObserver;

public class SumServiceImpl extends SumServiceGrpc.SumServiceImplBase {
	@Override
	public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
		int first = request.getFirstNumber();
		int second = request.getSecondNumber();
		int result = first + second;

		SumResponse response = SumResponse.newBuilder()
				.setResult(result)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
