package buildweeek.bw2.controllers;

import buildweeek.bw2.DTO.LoginUtenteRespDTO;
import buildweeek.bw2.DTO.NewUtenteDTO;
import buildweeek.bw2.DTO.UtenteLoginDTO;
import buildweeek.bw2.entities.Utente;
import buildweeek.bw2.exceptions.ValidationException;
import buildweeek.bw2.services.AutenticazioneService;
import buildweeek.bw2.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping("/registerAdmin")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente createUtente(@RequestBody @Validated NewUtenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.utenteService.saveUtente(body);
        }
    }

    @PatchMapping("/{idUtente}/addRole")
    public void findUtenteByAndPatchRole(@PathVariable UUID idUtente) {
        this.utenteService.findUtenteByIdAndPatchRuolo(idUtente);
    }
}
