package buildweeek.bw2.services;


import buildweeek.bw2.DTO.UtenteLoginDTO;
import buildweeek.bw2.entities.Utente;
import buildweeek.bw2.exceptions.UnauthorizedException;
import buildweeek.bw2.tools.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticazioneService {

        @Autowired
        private UtenteService utenteService;

        @Autowired
        private JWTTools jwtTools;

       @Autowired
       private PasswordEncoder bcrypt;

        public String checkAccessAndGenerateToken(UtenteLoginDTO body)
        {
            Utente found = this.utenteService.findByEmail(body.email());
            if (bcrypt.matches(body.password(), found.getPassword())) {
                String accessToken = jwtTools.createTokenUtente(found);
                return accessToken;
            } else {
                throw new UnauthorizedException("Password errata");
            }
        }
}
