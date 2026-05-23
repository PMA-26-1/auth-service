package store.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import feign.Response;
import store.account.AccountOut;



@RestController
public class AuthResource implements AuthController {

    // REST controller that implements AuthController
    // Calls AuthService and sets the JWT cookie on login and logout 
    
    @Autowired
    private AuthService authService;

    @Override
    public ResponseEntity<Void> login(LoginIn in) {

        final TokenOut out = authService.login(in.email(), in.password());

        return ResponseEntity
            .ok()
            .header(
                HttpHeaders.SET_COOKIE,
                buildTokenCookie(out.token(), authService.getDuration()).toString()
            )
            .build();
    }

    @Override
    public ResponseEntity<Void> logout() {

        return ResponseEntity
            .ok()
            .header(
                HttpHeaders.SET_COOKIE,
                buildTokenCookie(null, 0l).toString()
            )
            .build();

    }

    @Override
    public ResponseEntity<Void> register(RegisterIn in) {

        authService.register(in);
        return ResponseEntity.created(null).build();

    }

    @Override
    public ResponseEntity<AccountOut> whoAmI(String idAccount) {

        return ResponseEntity.ok(authService.whoAmI(idAccount));

    }

    @Override
    public ResponseEntity<Void> healthCheck() {

        return ResponseEntity.ok().build();

    }

    private ResponseCookie buildTokenCookie(String content, Long duration) {

        return ResponseCookie.from(AuthController.AUTH_COOKIE_TOKEN, content)
            .httpOnly(authService.getHttpOnly())
            .sameSite("None")
            .secure(true)
            .path("/")
            .maxAge(Duration.ofMillis(duration))
            .build();

    }

    @Override
    public ResponseEntity<Map<String, String>> solveToken(TokenOut map) {

        final String idAccount = authService.solveToken(map.token());
        return ResponseEntity.ok(Map.of("idAccount", idAccount));

    }



}
