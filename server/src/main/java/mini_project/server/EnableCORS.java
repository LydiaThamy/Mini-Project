// package mini_project.server;

// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// public class EnableCORS implements WebMvcConfigurer {
//     final String path;
//     final String origins;

//     public EnableCORS(String path, String origins) {
//         this.path = path;
//         this.origins = origins;
//     }

//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping(path)
//                 .allowedOrigins(origins);
//     }
// }
