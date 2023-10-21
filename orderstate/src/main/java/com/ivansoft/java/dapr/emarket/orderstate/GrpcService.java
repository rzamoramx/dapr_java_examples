package com.ivansoft.java.dapr.emarket.orderstate;

import com.google.protobuf.Any;
import com.ivansoft.java.dapr.emarket.common.models.Order;
import io.dapr.v1.AppCallbackGrpc;
import io.dapr.v1.CommonProtos;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import com.ivansoft.java.dapr.emarket.common.GrpcStateServiceProtos.SaveStateRequest;
import com.ivansoft.java.dapr.emarket.common.GrpcStateServiceProtos.GetStateRequest;
import com.ivansoft.java.dapr.emarket.common.GrpcStateServiceProtos.Response;
import com.ivansoft.java.dapr.emarket.common.GrpcStateServiceProtos.GetStateResponse;
import com.ivansoft.java.dapr.emarket.orderstate.repositories.StateRepository;
import com.ivansoft.java.dapr.emarket.common.Utils;
import java.util.Optional;
import java.util.logging.Logger;


public class GrpcService extends AppCallbackGrpc.AppCallbackImplBase {
    private static final Logger logger = Logger.getLogger(GrpcService.class.getName());
    private Server server;

    public void start(int port) throws IOException {
        this.server = ServerBuilder
                .forPort(port)
                .addService(this)
                .build()
                .start();
        logger.info("Server: started listening on port: " + port);

        // Now we handle ctrl+c (or any other JVM shutdown)
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Server: shutting down gracefully ...");
                GrpcService.this.server.shutdown();
                logger.info("Server: Bye.");
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
                logger.info("SaveOrder invoked");
                // get data from the request
                logger.info("request.getData().getValue(): " + request.getData().getValue().toStringUtf8());

                Order order = Utils.deserializeOrder(request.getData().getValue());
                logger.info("ORDER: " + order);
                String data = request.getData().getValue().toStringUtf8();
                String[] keyValuePair = data.split(":");

                // set key and value to a SaveStateRequest instance
                SaveStateRequest saveStateRequest = SaveStateRequest.newBuilder()
                        .setKey(keyValuePair[0])
                        .setValue(keyValuePair[1])
                        .build();

                // save the state in the repository
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

                logger.info("SaveOrder response: " + getStateResponse);

                // set response data
                responseBuilder.setData(Any.pack(getStateResponse));
                responseObserver.onNext(responseBuilder.build());
            }

            if ("GetState".equals(request.getMethod())) {
                logger.info("GetState invoked");
                // get data from the request
                String data = request.getData().getValue().toStringUtf8();
                String[] keyValuePair = data.split(":");
                // get key and create a GetStateRequest instance
                GetStateRequest getStateRequest = GetStateRequest.newBuilder()
                        .setKey(keyValuePair[0])
                        .build();

                // get the state from the repository
                Optional<String> result = stateRepository.retrieveState(getStateRequest.getKey());

                // prepare response
                CommonProtos.InvokeResponse.Builder responseBuilder = CommonProtos.InvokeResponse.newBuilder();
                Response.Builder respBuilder = Response.newBuilder();
                respBuilder.setStatus("OK");
                Response response = respBuilder.build();

                GetStateResponse.Builder getStateResponseBuilder = GetStateResponse.newBuilder();
                if (result.isPresent()) {
                    getStateResponseBuilder.setValue(result.get());
                } else {
                    logger.info("No state found for key: " + getStateRequest.getKey());
                    getStateResponseBuilder.setValue("");
                }
                getStateResponseBuilder.setResponse(response);
                GetStateResponse getStateResponse = getStateResponseBuilder.build();

                logger.info("GetState response: " + getStateResponse);

                // set response data
                responseBuilder.setData(Any.pack(getStateResponse));
                responseObserver.onNext(responseBuilder.build());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            responseObserver.onCompleted();
        }
    }
}
