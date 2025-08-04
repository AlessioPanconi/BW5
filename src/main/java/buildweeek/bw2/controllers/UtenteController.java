package buildweeek.bw2.controllers;

import buildweeek.bw2.DTO.NewUtenteDTO;
import buildweeek.bw2.DTO.UpdateUtenteDTO;
import buildweeek.bw2.entities.Utente;
import buildweeek.bw2.exceptions.ValidationException;
import buildweeek.bw2.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/utente")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Utente> getPageUtenti(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize)
    {return  this.utenteService.findAll(pageNumber, pageSize);}

    @GetMapping("/{idUtente}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente getDipendenteById(@PathVariable UUID idUtente)
    {
        return this.utenteService.findUtenteById(idUtente);

    }

    @DeleteMapping("/{idUtente}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findUtenteByIdAndDelete(@PathVariable UUID idUtente) {
        this.utenteService.findUtenteByIdAndDelete(idUtente);
    }

    @PutMapping("/{idUtente}")
    public Utente findUtenteByIdAndUpdate (@PathVariable UUID idUtente,@RequestBody @Validated UpdateUtenteDTO body, BindingResult validationResult)
    {
        if(validationResult.hasErrors()){
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else{
            return this.utenteService.findUtenteByIdAndUpdate(idUtente,body);
        }
    }

    @GetMapping("/me")
    public Utente trovaIlMioProfilo(@AuthenticationPrincipal Utente currentAuthenticatedUtente)
    {
        return this.utenteService.findUtenteById(currentAuthenticatedUtente.getIdUtente());
    }

    @PutMapping("/me")
    public Utente modificaIlMioProfilo(@AuthenticationPrincipal Utente currentAuthenticatedUtente, @RequestBody @Validated UpdateUtenteDTO payload)
    {
        return this.utenteService.findUtenteByIdAndUpdate(currentAuthenticatedUtente.getIdUtente(),payload);
    }

    @DeleteMapping("/me")
    public void eliminaIlMioProfilo(@AuthenticationPrincipal Utente currentAuthenticatedUtente)
    {
        this.utenteService.findUtenteById(currentAuthenticatedUtente.getIdUtente());
    }

    @PatchMapping("/{idUtente}/avatar")
    public String uploadImage(@PathVariable UUID idUtente,@RequestParam("avatar") MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        return this.utenteService.uploadAvatar(idUtente, file);
    }
}
