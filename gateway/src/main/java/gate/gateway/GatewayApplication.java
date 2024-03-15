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
				.route( r-> r.path("/gestionUtilisateur/**")
						.filters(f->f.rewritePath("/gestionUtilisateur/(?<remains>.*)","/${remains}")
								.preserveHostHeader()
						)
						.uri("lb://gestionUtilisateur")
				)
				.route(r -> r.path("/gestionSalon/**")
						.filters(f -> f.rewritePath("/gestionSalon/(?<remains>.*)", "/${remains}")                                )
						.uri("lb://gestionSalon")
				)
				.route(r -> r.path("/authentification/**")
						.filters(f -> f.rewritePath("/authentification/(?<remains>.*)", "/${remains}")                                )
						.uri("lb://authentification")
				)
				.build();
	}
}
