package gate.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				// Route vers le contrôleur d'authentification
				.route(r -> r.path("/auth/**")
						.uri("lb://authentification")
				)
				// Route vers le contrôleur de gestionUtilisateur
				.route(r -> r.path("/utilisateurs/**")
						.uri("lb://gestionUtilisateur")
				)
				// Route vers le contrôleur de gestionSalon
				.route(r -> r.path("/salon/**")
						.uri("lb://gestionSalon")
				)
				// Route vers le contrôleur de contact
				.route(r -> r.path("/contact/**")
						.uri("lb://contact")
				)
				// Ajoutez d'autres routes ici pour les autres services si nécessaire
				.build();
	}

}
