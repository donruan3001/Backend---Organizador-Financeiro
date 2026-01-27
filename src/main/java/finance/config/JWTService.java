package finance.config;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import finance.domain.user.User;

@Service
public class JWTService {
    
@Autowired
@Value("${JWT_SECRET}")
private String secretKey;


public String createSecretKey(User user){

    // Gerar o token
try {

    var algorithm= Algorithm.HMAC256(secretKey);

    return JWT.create()
        .withIssuer("api auth")          // Identifica quem gerou o token
        .withSubject(user.getId().toString())   // Identifica o assunto do token
        .withClaim("username",user.getUsername())// O "dono" do token (usado no .getSubject())
        .withExpiresAt(dataExpiracao())        // Expiração do token
        .sign(algorithm);                      // Assina o token com o algoritmo
} catch (JWTCreationException exception){
    throw new RuntimeException("Erro ao gerar token JWT", exception);
}

}
//metodo que calcula a data de expiração do token
private Date dataExpiracao(){
    return Date.from(LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-03:00")));

}


public String getSecretKey(String tokenJWT){
    try {
        return JWT.require(Algorithm.HMAC256(secretKey))
            .withIssuer("api auth")
            .build()
            .verify(tokenJWT)
            .getSubject();
    } catch (JWTCreationException exception) { {
        throw new RuntimeException("Token JWT inválido ou expirado", exception);
    }

    }
}



}
