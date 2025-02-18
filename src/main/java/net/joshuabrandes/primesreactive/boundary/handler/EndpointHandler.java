package net.joshuabrandes.primesreactive.boundary.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

@Component
public class EndpointHandler {

    /*
    public Mono<ServerResponse> countPrimes(ServerRequest request) {
        var maxValue = (long) request.attributes().get("max-value");

        var counter = 0L;
        for (long i = 0; i < maxValue; i++) {
            if (isPrime(i)) counter++;
        }

        return ServerResponse.ok().bodyValue(counter);
    }
     */

    // More performant (but maybe distorting results for performance-tests)
    public Mono<ServerResponse> countPrimes(ServerRequest request) {
        var maxValue = (long) request.attributes().get("max-value");

        // Flux.range only allows for int upper boundaries, and Flux.generate is more complex
        var range = Stream.generate(() -> 1L).limit(maxValue);
        return Flux.fromStream(range)
                /* parallelized version (even higher performance, but also mor result distortion)
                .parallel()
                .runOn(Schedulers.parallel())
                 */
                .filter(this::isPrime)
                // .sequential()
                .count()
                .flatMap(counter -> ServerResponse.ok().bodyValue(counter));
    }

    private boolean isPrime(long n) {
        int sqrt = (int) Math.sqrt(n);
        for (int i = 2; i <= sqrt; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
