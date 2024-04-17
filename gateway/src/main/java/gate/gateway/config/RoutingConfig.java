package gate.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

public class RoutingConfig {

    private static final String REMAIN = "/${remains}";

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                // Route vers le contrôleur d'authentification
                .route(r -> r.path("/auth/**")
                        .filters(f -> f.rewritePath("/auth/(?<remains>.*)", REMAIN)
                                .preserveHostHeader())
                        .uri("lb://authentification")
                )
                // Route vers le contrôleur de gestionUtilisateur
                .route(r -> r.path("/utilisateurs/**")
                        .filters(f -> f.rewritePath("/utilisateurs/(?<remains>.*)", REMAIN)
                                .preserveHostHeader())
                        .uri("lb://gestionUtilisateur")
                )
                // Route vers le contrôleur de gestionSalon
                .route(r -> r.path("/salon/**")
                        .filters(f -> f.rewritePath("/salon/(?<remains>.*)", REMAIN)
                                .preserveHostHeader())
                        .uri("lb://gestionSalon")
                )
                // Route vers le contrôleur de contact
                .route(r -> r.path("/contact/**")
                        .filters(f -> f.rewritePath("/contact/(?<remains>.*)", REMAIN)
                                .preserveHostHeader())
                        .uri("lb://contact")
                )

                .build();
    }
}
