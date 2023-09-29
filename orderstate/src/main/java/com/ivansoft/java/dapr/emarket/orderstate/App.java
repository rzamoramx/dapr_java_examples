package com.ivansoft.java.dapr.emarket.orderstate;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        //var port = Integer.parseInt(System.getenv("DAPR_GRPC_PORT"));
        
        Options options = new Options();
        options.addRequiredOption("p", "port", true, "Port to listen to.");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        final GrpcService service = new GrpcService();
        service.start(Integer.parseInt(cmd.getOptionValue("port")));
        service.awaitTermination();
    }
}
