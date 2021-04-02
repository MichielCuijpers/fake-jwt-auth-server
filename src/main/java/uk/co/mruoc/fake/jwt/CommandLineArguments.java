package uk.co.mruoc.fake.jwt;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class CommandLineArguments {

    @Parameter(
            names = { "--port", "-p" },
            description = "The port to run the server on, 0 will select a random free port"
    )
    private int port;

    @Parameter(
            names = { "--issuer", "-i" },
            description = "The issuer to use when generating jwt tokens"
    )
    private String issuer = "default-issuer";

    @Parameter(
            names = { "--audience", "-a" },
            description = "The audience to use when generating jwt if not supplied in request"
    )
    private String audience = "default-audience";

    public static CommandLineArguments parse(String[] args) {
        CommandLineArguments arguments = new CommandLineArguments();
        JCommander commander = JCommander.newBuilder()
                .programName("fake-jwt-auth-server")
                .addObject(arguments)
                .build();
        parse(args, commander);
        return arguments;
    }

    public static void parse(String[] args, JCommander commander) {
        try {
            commander.parse(args);
        } catch (ParameterException e) {
            log.debug(e.getMessage(), e);
            StringBuilder usage = new StringBuilder();
            commander.getUsageFormatter().usage(usage);
            throw new CommandLineArgumentsException(usage.toString());
        }
    }

}
