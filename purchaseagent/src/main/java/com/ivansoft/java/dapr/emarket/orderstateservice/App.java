package com.ivansoft.java.dapr.emarket.orderstateservice;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        //var port = Integer.parseInt(System.getenv("DAPR_GRPC_PORT"));
        var port = 50051;

        final GrpcService service = new GrpcService();
        service.start(port);
        service.awaitTermination();
    }
}
