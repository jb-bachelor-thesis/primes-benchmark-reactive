package net.joshuabrandes.primesreactive.boundary.router;

import net.joshuabrandes.primesreactive.boundary.handler.EndpointHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class EndpointRouter {

    private final EndpointHandler endpointHandler;

    public EndpointRouter(EndpointHandler endpointHandler) {
        this.endpointHandler = endpointHandler;
    }

    @Bean
    RouterFunction<ServerResponse> getPrimes() {
        return route(GET("v1/prime/{max-value)"), endpointHandler::countPrimes);
    }
}
