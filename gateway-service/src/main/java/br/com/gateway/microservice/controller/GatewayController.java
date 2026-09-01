package br.com.gateway.microservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class GatewayController {

    private final WebClient webClient;

    @Value("${services.notification.url}")
    private String notificationServiceUrl;

    @Value("${services.ticket.url}")
    private String ticketServiceUrl;

    @Value("${services.user.url}")
    private String userServiceUrl;

    public GatewayController() {
        this.webClient = WebClient.builder().build();
    }

    @RequestMapping({
            "/{service}",
            "/{service}/{path:^(?!api).*$}/**"
    })
    public Mono<ResponseEntity<String>> proxy(
            @PathVariable String service,
            @PathVariable(required = false) String path,
            @RequestHeader HttpHeaders headers,
            @RequestParam(required = false)
            MultiValueMap<String, String> queryParams,
            @RequestBody(required = false) String body,
            HttpServletRequest request
    ) {

        String baseUrl = switch (service) {
            case "notifications" -> notificationServiceUrl;
            case "tickets" -> ticketServiceUrl;
            case "users" -> userServiceUrl;
            case "auth" -> userServiceUrl;
            default -> null;
        };

        System.out.println("URL recebida: " + baseUrl);

        if (baseUrl == null) {
            return Mono.just(
                    ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("Serviço não encontrado")
            );
        }

        String fullPath = request.getRequestURI()
                .replaceFirst("^/api", "");

        return webClient
                .method(HttpMethod.valueOf(request.getMethod()))
                .uri(baseUrl + fullPath)
                .headers(httpHeaders -> {
                    headers.forEach((key, values) -> {
                        String lowerKey = key.toLowerCase();
                        if (!lowerKey.equals("host")
                                && !lowerKey.equals("content-length")
                                && !lowerKey.equals("connection")) {
                            httpHeaders.put(key, values);
                        }
                    });
                })
                .body(
                        body == null ? Mono.empty() : Mono.just(body),
                        String.class
                )
                .exchangeToMono(response -> {

                    HttpStatusCode status = response.statusCode();

                    return response
                            .bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .map(responseBody -> {

                                HttpHeaders responseHeaders =
                                        new HttpHeaders();

                                response.headers()
                                        .asHttpHeaders()
                                        .forEach((key, values) -> {
                                            String lowerKey = key.toLowerCase();
                                            if (!lowerKey.equals("transfer-encoding")
                                                    && !lowerKey.equals("content-length")
                                                    && !lowerKey.equals("connection")) {
                                                responseHeaders.put(key, values);
                                            }
                                        });

                                return ResponseEntity
                                        .status(status)
                                        .headers(responseHeaders)
                                        .body(responseBody);
                            });
                });
    }
}