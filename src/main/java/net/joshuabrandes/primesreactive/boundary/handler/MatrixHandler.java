package net.joshuabrandes.primesreactive.boundary.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Random;

@Component
public class MatrixHandler {

    public static final int MULTIPLICATION_LIMIT = 40;

    private final Random random = new Random();

    public Mono<ServerResponse> matrixStressTest(ServerRequest request) {
        // get "size" from path params
        var size = Integer.parseInt(request.pathVariable("size"));

        return Mono.just(size)
                .flatMap(n -> {
                    var matrix1 = generateMatrix(n);
                    var matrix2 = generateMatrix(n);

                    /*
                    return Mono.just(matrix1)
                            .expand(current -> Mono.just(multiplyMatrix(current, matrix2)))
                            .take(MULTIPLICATION_LIMIT)
                            .last();
                     */
                    return Mono.just(stressTest(matrix1, matrix2));
                })
                .flatMap(matrix -> ServerResponse.ok().bodyValue(reduceMatrix(matrix)));
    }


    private double[][] generateMatrix(int size) {
        var matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextDouble() * 1000;
            }
        }

        return matrix;
    }

    private double[][] multiplyMatrix(double[][] matrix1, double[][] matrix2) {
        // assume square and the same size
        var n = matrix1.length;
        var result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                var sum = 0.0;
                for (int k = 0; k < n; k++) {
                    sum += matrix1[i][k] * matrix2[k][j];
                }
                result[i][j] = sum;
            }
        }

        return result;
    }

    private double[][] stressTest(double[][] matrix1, double[][] matrix2) {
        var result = matrix1;
        for (int i = 0; i < MULTIPLICATION_LIMIT; i++) {
            result = multiplyMatrix(result, matrix2);
        }

        return result;
    }

    private double reduceMatrix(double[][] matrix) {
        var sum = 0.0;
        for (double[] doubles : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                sum += doubles[j];
            }
        }

        return sum;
    }
}
