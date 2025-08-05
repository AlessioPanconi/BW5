package buildweeek.bw2.controllers;

import buildweeek.bw2.DTO.LoginUtenteRespDTO;
import buildweeek.bw2.DTO.UtenteLoginDTO;
import buildweeek.bw2.services.AutenticazioneService;
import buildweeek.bw2.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/noAuth")
public class AutenticazioneController {

    @Autowired
    private AutenticazioneService autenticazioneService;

    @Autowired
    private UtenteService utenteService;

    @PostMapping("/login")
    public LoginUtenteRespDTO loginUtenteRespDTO(@RequestBody UtenteLoginDTO body)
    {
        String token = autenticazioneService.checkAccessAndGenerateToken(body);
        return new LoginUtenteRespDTO(token);
    }
}
