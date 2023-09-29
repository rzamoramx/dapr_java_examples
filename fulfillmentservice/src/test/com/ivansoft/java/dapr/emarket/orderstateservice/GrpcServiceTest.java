package com.ivansoft.java.dapr.emarket.orderstateservice;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.ivansoft.java.dapr.emarket.orderstateservice.services.StateService;
import io.dapr.v1.AppCallbackGrpc;
import io.dapr.v1.CommonProtos;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import io.grpc.ManagedChannelBuilder;

class GrpcServiceTest {
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    private ManagedChannel channel;

    private GrpcService grpcService;
    private AppCallbackGrpc.AppCallbackImplBase serviceMock;
    private StreamObserver<CommonProtos.InvokeResponse> responseObserverMock;

    @BeforeEach
    public void setUp() {
        grpcService = new GrpcService();
        serviceMock = mock(AppCallbackGrpc.AppCallbackImplBase.class);
        responseObserverMock = mock(StreamObserver.class);

        // Create a managed channel instance to the started server
        channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build();

        // Register channel for automatic cleanup
        grpcCleanup.register(channel);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        grpcService.awaitTermination();

        grpcService = null;
        serviceMock = null;
        responseObserverMock = null;
    }

    @Test
    public void testOnInvoke_SaveState() throws InvalidProtocolBufferException {
        // Mocking the incoming request
        CommonProtos.InvokeRequest request = CommonProtos.InvokeRequest.newBuilder()
                .setMethod("SaveState")
                .setData(Any.pack(CommonProtos.InvokeRequest.newBuilder().getDataBuilder()
                        .setValue(ByteString.copyFromUtf8("key:value"))
                        .build()))
                .build();

        // Create a stub client for the defined service
        AppCallbackGrpc.AppCallbackBlockingStub client = AppCallbackGrpc.newBlockingStub(channel);

        // Calling the method to be tested
        grpcService.onInvoke(request, responseObserverMock);
        //CommonProtos.InvokeResponse response = client.onInvoke(request);

        // Verifying that the appropriate methods were called
        verify(responseObserverMock, times(1)).onNext(any(CommonProtos.InvokeResponse.class));
        verify(responseObserverMock, times(1)).onCompleted();

        // assert that response is equal to value
        //assertEquals(response.getData().unpack(GrpcStateServiceProtos.GetStateResponse.class).getValue(), "value");
    }
}