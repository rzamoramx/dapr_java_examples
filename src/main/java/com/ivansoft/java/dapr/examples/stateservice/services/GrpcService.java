package com.ivansoft.java.dapr.examples.stateservice.services;

import com.google.protobuf.Any;
import io.dapr.v1.AppCallbackGrpc;
import io.dapr.v1.CommonProtos;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

import com.ivansoft.java.dapr.examples.stateservice.GrpcStateServiceProtos.SaveStateRequest;
import org.springframework.stereotype.Service;

@Service
public class GrpcService extends AppCallbackGrpc.AppCallbackImplBase {
    private Server server;

    private void start(int port) throws IOException {
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

    private void awaitTermination() throws InterruptedException {
        if (this.server != null) {
            this.server.awaitTermination();
        }
    }

    @Override
    public void onInvoke(CommonProtos.InvokeRequest request,
                         StreamObserver<CommonProtos.InvokeResponse> responseObserver) {
        try {
            if ("SaveState".equals(request.getMethod())) {
                SaveStateRequest sayRequest =
                        SaveStateRequest.newBuilder().setMessage(request.getData().getValue().toStringUtf8()).build();
                SayResponse sayResponse = this.say(sayRequest);
                CommonProtos.InvokeResponse.Builder responseBuilder = CommonProtos.InvokeResponse.newBuilder();
                responseBuilder.setData(Any.pack(sayResponse));
                responseObserver.onNext(responseBuilder.build());
            }
        } finally {
            responseObserver.onCompleted();
        }
    }
}
