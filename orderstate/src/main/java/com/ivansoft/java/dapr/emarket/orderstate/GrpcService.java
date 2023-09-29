package com.ivansoft.java.dapr.emarket.orderstate;

import com.google.protobuf.Any;
import io.dapr.v1.AppCallbackGrpc;
import io.dapr.v1.CommonProtos;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import com.ivansoft.java.dapr.emarket.orderstate.GrpcStateServiceProtos.SaveStateRequest;
import com.ivansoft.java.dapr.emarket.orderstate.GrpcStateServiceProtos.Response;
import com.ivansoft.java.dapr.emarket.orderstate.GrpcStateServiceProtos.GetStateResponse;
import com.ivansoft.java.dapr.emarket.orderstate.repositories.StateRepository;


public class GrpcService extends AppCallbackGrpc.AppCallbackImplBase {
    private Server server;

    public void start(int port) throws IOException {
        this.server = ServerBuilder
                .forPort(port)
                .addService(this)
                .build()
                .start();
        System.out.printf("Server: started listening on port %d\n", port);

        // Now we handle ctrl+c (or any other JVM shutdown)
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Server: shutting down gracefully ...");
                GrpcService.this.server.shutdown();
                System.out.println("Server: Bye.");
            }
        });
    }

    public void awaitTermination() throws InterruptedException {
        if (this.server != null) {
            this.server.awaitTermination();
        }
    }

    @Override
    public void onInvoke(CommonProtos.InvokeRequest request,
                         StreamObserver<CommonProtos.InvokeResponse> responseObserver) {
        StateRepository stateRepository = StateRepository.getInstance();
        try {
            if ("SaveOrder".equals(request.getMethod())) {
                // get data from the request
                String data = request.getData().getValue().toStringUtf8();
                // get key and value from data
                String[] keyValuePair = data.split(":");

                // set key and value to a SaveStateRequest instance
                SaveStateRequest saveStateRequest = SaveStateRequest.newBuilder()
                        .setKey(keyValuePair[0])
                        .setValue(keyValuePair[1])
                        .build();

                // save the state
                stateRepository.storeState(saveStateRequest.getKey(), saveStateRequest.getValue());

                // prepare response
                CommonProtos.InvokeResponse.Builder responseBuilder = CommonProtos.InvokeResponse.newBuilder();

                Response.Builder respBuilder = Response.newBuilder();
                respBuilder.setStatus("OK");
                Response response = respBuilder.build();

                GetStateResponse.Builder getStateResponseBuilder = GetStateResponse.newBuilder();
                getStateResponseBuilder.setValue(saveStateRequest.getValue());
                getStateResponseBuilder.setResponse(response);
                GetStateResponse getStateResponse = getStateResponseBuilder.build();

                // set response data
                responseBuilder.setData(Any.pack(getStateResponse));
                responseObserver.onNext(responseBuilder.build());
            }
        } finally {
            responseObserver.onCompleted();
        }
    }
}
