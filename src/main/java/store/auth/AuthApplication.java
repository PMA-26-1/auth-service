package store.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCaching
@EnableFeignClients(
    basePackages = {
        "store.account"
    }
)
public class AuthApplication {

    // Spring boot entrypoint
    // Enables Feign clients from the store.account package
    
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
