package uk.co.mruoc.fake.jwt;

import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.fake.jwt.authserver.DefaultFakeJwtAuthServerConfig;
import uk.co.mruoc.fake.jwt.authserver.FakeJwtAuthServer;
import uk.co.mruoc.fake.jwt.authserver.FakeJwtAuthServerConfig;

@Slf4j
public class Main {

    public static void main(String[] args) {
        try {
            CommandLineArguments arguments = CommandLineArguments.parse(args);
            FakeJwtAuthServer server = new FakeJwtAuthServer(toConfig(arguments));
            log.info("server running at {}", server.start());
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        } catch (CommandLineArgumentsException e) {
            log.error(e.getMessage());
        }
    }

    private static FakeJwtAuthServerConfig toConfig(CommandLineArguments arguments) {
        return DefaultFakeJwtAuthServerConfig.builder()
                .port(arguments.getPort())
                .issuer(arguments.getIssuer())
                .defaultAudience(arguments.getAudience())
                .build();
    }

}
