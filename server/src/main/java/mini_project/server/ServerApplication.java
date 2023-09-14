package mini_project.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	// @Bean
	// public WebMvcConfigurer corsConfigurer() {
	// 	return new EnableCORS("/api/*", "*");
	// }

	// @Bean
	// public WebMvcConfigurer corsConfigurer() {
	// 	return new WebMvcConfigurer() {
	// 		@Override
	// 		public void addCorsMappings(CorsRegistry registry) {
	// 			registry.addMapping("/api/**")
	// 			.allowedOrigins("http://localhost:4200")
	// 			.allowedMethods("GET", "POST", "PUT", "DELETE")
	// 			;
	// 		}
	// 	};
	// }

}
