package io.netty.echo;

import io.vavr.control.Try;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Main {
    private static final Logger logger = LogManager.getLogger(EchoServer.class);

    public static final String H_OPTN = "h";
    public static final String P_OPTN = "p";

    public static final String JAR_FILE_NAME = "nettyEcho-1.0-SNAPSHOT";

    public static void main(String[] args) throws InterruptedException {

        Options optns = getOptions();
        if (hasHelpOption(optns.getOption(H_OPTN), args)) {
            printHelp(optns);
            return;
        }

        Try<Integer> localPort = getLocalPort(getCommandLine(args, optns));
        localPort.onFailure(ex -> System.exit(1));

        logger.info("Starting echo server on local port {}.", localPort.get());
        EchoServer.start(localPort.get());
    }

    private static Options getOptions() {
        Options ret = new Options();
        ret.addOption(Option.builder(H_OPTN).longOpt("help").required(false).hasArg(false).build());
        ret.addOption(Option.builder(P_OPTN).longOpt("localPort").required(true).hasArg(true).build());
        return ret;
    }

    private static boolean hasHelpOption(final Option helpOption, final String[] args) {
        Options options = new Options();
        options.addOption(helpOption);

        Try<Boolean> hasHelpOptn = Try.of(() -> new DefaultParser().parse(options, args, true))
                                      .onFailure(ex -> System.err.println(ex.getMessage())) // unexpected exception
                                      .map(commandLine -> commandLine.hasOption(helpOption.getOpt()));

        return hasHelpOptn.isFailure() || hasHelpOptn.get();
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(JAR_FILE_NAME, options, true);
    }


    private static Try<Integer> getLocalPort(Try<CommandLine> commandLine) {
        return commandLine
                .flatMap(cmndLine -> Try.of(() -> cmndLine.getParsedOptionValue(P_OPTN).toString())
                                        .onFailure(ex -> System.err.println(ex.getMessage())))
                .flatMap(port -> Try.of(() -> Integer.valueOf(port))
                                    .onFailure(
                                            ex -> System.err.println("Failed to convert to int. " + ex.getMessage())));
    }

    private static Try<CommandLine> getCommandLine(String[] args, Options optns) {
        return Try.of(() -> new DefaultParser().parse(optns, args))
                  .onFailure(ex -> System.err.println(ex.getMessage()));
    }
}