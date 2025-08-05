package buildweeek.bw2.controllers;

import buildweeek.bw2.DTO.NewClienteDTO;
import buildweeek.bw2.DTO.NewUtenteDTO;
import buildweeek.bw2.DTO.payloadMetodiClienti.DataInserimentoDTO;
import buildweeek.bw2.DTO.payloadMetodiClienti.DataUltimoContattoDTO;
import buildweeek.bw2.DTO.payloadMetodiClienti.FatturatoAnnualeDTO;
import buildweeek.bw2.DTO.payloadMetodiClienti.PartialNameDTO;
import buildweeek.bw2.entities.Cliente;
import buildweeek.bw2.entities.Utente;
import buildweeek.bw2.exceptions.ValidationException;
import buildweeek.bw2.services.ClienteService;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/utente")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private ClienteService clienteService;

//Opzioni per admin

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente createUtente(@RequestBody @Validated NewUtenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.utenteService.saveUtente(body);
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Utente> getPageUtenti(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return this.utenteService.findAll(pageNumber, pageSize);
    }

    @GetMapping("/{idUtente}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente getDipendenteById(@PathVariable UUID idUtente) {
        return this.utenteService.findUtenteById(idUtente);
    }

    @DeleteMapping("/{idUtente}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findUtenteByIdAndDelete(@PathVariable UUID idUtente) {
        this.utenteService.findUtenteByIdAndDelete(idUtente);
    }

    @PutMapping("/{idUtente}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente getUtenteByIdAndUpdate(@PathVariable UUID idUtente, @RequestBody @Validated NewUtenteDTO payload) {
        return this.utenteService.findUtenteByIdAndUpdate(idUtente, payload);
    }

    @PatchMapping("/{idUtente}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findUtenteByAndPatchRole(@PathVariable UUID idUtente) {
        this.utenteService.findUtenteByIdAndPatchRuolo(idUtente);
    }

    @PatchMapping("/{idUtente}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findUtenteByAndDeleteRoleAdmin(@PathVariable UUID idUtente) {
        this.utenteService.findUtenteByIdAndRemoveAdmin(idUtente);
    }

//sezione /me opzioni che può fare un utente dopo il login su se stesso

    @GetMapping("/me")
    public Utente trovaIlMioProfilo(@AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        return this.utenteService.findUtenteById(currentAuthenticatedUtente.getIdUtente());
    }

    @PutMapping("/me")
    public Utente modificaIlMioProfilo(@AuthenticationPrincipal Utente currentAuthenticatedUtente, @RequestBody @Validated NewUtenteDTO payload) {
        return this.utenteService.findUtenteByIdAndUpdate(currentAuthenticatedUtente.getIdUtente(), payload);
    }

    @DeleteMapping("/me")
    public void eliminaIlMioProfilo(@AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        this.utenteService.findUtenteById(currentAuthenticatedUtente.getIdUtente());
    }

    @PatchMapping("/me/{idUtente}/avatar")
    public String uploadImage(@PathVariable UUID idUtente, @RequestParam("avatar") MultipartFile file, @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        return this.utenteService.uploadAvatar(idUtente, file);
    }

    //Sezione cliente

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Cliente> getPageClienti(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return this.clienteService.findAllClienti(pageNumber, pageSize);
    }

    @GetMapping("/cliente")
    public Page<Cliente> getPageClientiSortByName(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "nomeContatto") String sort) {
        return this.clienteService.findAll(pageNumber, pageSize, sort);
    }

    @GetMapping("/cliente")
    public Page<Cliente> getPageClientiSortByFatturato(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "fatturatoAnnuale") String sort) {
        return this.clienteService.findAll(pageNumber, pageSize, sort);
    }

    @GetMapping("/cliente")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Cliente> getPageClientiByDataInserimento(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "dataInserimento") String sort) {
        return this.clienteService.findAll(pageNumber, pageSize, sort);
    }

    @GetMapping("/cliente")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Cliente> getPageClientiByDataUltimoContatto(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "dataUltimoContatto") String sort) {
        return this.clienteService.findAllReverse(pageNumber, pageSize, sort);
    }

    //Sort by provincia per sede legale

    @GetMapping("/cliente")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> findClientiByFatturatoMaggiore(@RequestBody @Validated FatturatoAnnualeDTO payload) {
        return this.clienteService.findClientiByFatturatoMaggiore(payload);
    }

    @GetMapping("/cliente")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> findClientiByFatturatoMinore(@RequestBody @Validated FatturatoAnnualeDTO payload) {
        return this.clienteService.findClientiByFatturatoMinore(payload);
    }

    @GetMapping("/cliente")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> findClientiByBeforeDataInserimento(@RequestBody @Validated DataInserimentoDTO payload) {
        return this.clienteService.findClientiByBeforeDataInserimento(payload);
    }

    @GetMapping("/cliente")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> findClientiByAfterDataInserimento(@RequestBody @Validated DataInserimentoDTO payload) {
        return this.clienteService.findClientiByAfterDataInserimento(payload);
    }

    @GetMapping("/cliente")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> findClientiByDataUltimoContatto(@RequestBody @Validated DataUltimoContattoDTO payload) {
        return this.clienteService.findClientiByDataUltimoContatto(payload);
    }

    @GetMapping("/cliente")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> findClientiByPartialName(@RequestBody @Validated PartialNameDTO payload) {
        return this.clienteService.findClientiByPartialName(payload);
    }

    //POST cliente

    @PostMapping("/cliente")
    public Cliente saveCliente(@RequestBody @Validated NewClienteDTO payload) {
        return this.clienteService.saveCliente(payload);
    }

    //PUT cliente

    @PutMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente updateCliente(@RequestParam UUID clienteId, @RequestBody @Validated NewClienteDTO payload) {
        return this.clienteService.findClienteByIdAndUpdate(clienteId, payload);
    }

    //DELETE cliente
  @DeleteMapping("/cliente/{clienteId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public void deleteCliente(@RequestParam UUID clienteId) {
        return this.clienteService.findClienteByIdAndDelete(clienteId);
  }


}
