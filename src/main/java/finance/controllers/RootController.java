package finance.controllers;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

//controller para redirecionar a raiz para o Swagger UI
 @RestController
    public class RootController {
        
        @GetMapping("/")
        public void redirectToSwagger(HttpServletResponse response) throws IOException {
            response.sendRedirect("/swagger-ui/index.html");
        }}