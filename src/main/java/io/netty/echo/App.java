package io.netty.echo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "nettyEcho-1.0-SNAPSHOT-all.jar", mixinStandardHelpOptions = true, version = "1.0-SNAPSHOT", description = "Starts the netty echo server.")
public final class App implements Callable<Integer> {
    private static final Logger logger = LogManager.getLogger(EchoServer.class);

    @Option(names = { "-p", "--port" }, required = true, description = "Port to listen on")
    private int localPort;

    public static void main(String[] args) throws InterruptedException {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }


    @Override
    public Integer call() throws Exception {
        logger.info("Starting echo server on local port {}.", localPort);
        EchoServer.start(localPort);

        return 0;
    }
}