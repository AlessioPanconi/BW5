package buildweeek.bw2.controllers;

import buildweeek.bw2.DTO.*;
import buildweeek.bw2.DTO.payloadMetodiClienti.*;
import buildweeek.bw2.entities.Cliente;
import buildweeek.bw2.entities.Fattura;
import buildweeek.bw2.entities.Indirizzo;
import buildweeek.bw2.entities.Utente;
import buildweeek.bw2.exceptions.ValidationException;
import buildweeek.bw2.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/utente")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private FatturaService fatturaService;

    @Autowired
    private IndirizzoService indirizzoService;

    // Opzioni per admin

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
    public Utente getUtenteById(@PathVariable UUID idUtente) {
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
    public Utente getUtenteByIdAndUpdate(@PathVariable UUID idUtente, @RequestBody @Validated NewUtenteDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.utenteService.findUtenteByIdAndUpdate(idUtente, payload);
        }
    }

    @PatchMapping("/{idUtente}/addRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findUtenteByAndPatchRole(@PathVariable UUID idUtente) {
        this.utenteService.findUtenteByIdAndPatchRuolo(idUtente);
    }

    @PatchMapping("/{idUtente}/removeRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findUtenteByAndDeleteRoleAdmin(@PathVariable UUID idUtente) {
        this.utenteService.findUtenteByIdAndRemoveAdmin(idUtente);
    }

    // opzione admin Fatture
    @PostMapping("/cliente/nuovaFattura/{clienteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public FatturaRespDTO save(@PathVariable UUID clienteId ,@RequestBody @Validated FatturaDTO body, BindingResult validResu){
        if (validResu.hasErrors()) {
            throw new ValidationException(validResu.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }else {
            Fattura newF = this.fatturaService.save(clienteId,body);
            return new FatturaRespDTO(newF.getIdFattura());
        }
    }
    @PutMapping("/cliente/modificaFattura/{idFattura}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Fattura getByIdAndUpdate(@PathVariable UUID idFattura, @RequestBody @Validated FatturaDTO payload){
        return this.fatturaService.findByIdAndUpdate(idFattura, payload);
    }

    @DeleteMapping("/cliente/eliminaFattura/{idFattura}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void getByIdAndDelete(@PathVariable UUID idFattura){
        this.fatturaService.findByIdAndDelete(idFattura);
    }

    @GetMapping("/cliente/fattura/findByImporto")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Fattura> getFatturaByImporto(@RequestBody @Validated ImportoDTO body){
        return this.fatturaService.findByImporto(body.importoMin(), body.importoMax());
    }

    @GetMapping("/cliente/fattura/findByData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Fattura> findFatturaByData(@RequestBody @Validated DataDTO date){
        return this.fatturaService.findBydataFattura(date);
    }

    @GetMapping("/cliente/fattura/findByAnno/{anno}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Fattura> findFatturaByAnno(@PathVariable int anno){
        return this.fatturaService.findByAnno(anno);
    }


    @GetMapping("/cliente/fattura/findByCliente/{clienteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Fattura> findByCliente(@PathVariable UUID clienteId){
        return this.fatturaService.findByCliente(clienteId);
    }

    @GetMapping("/cliente/fattura/findByNumero/{numeroFattura}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Fattura findByNumero(@PathVariable long numeroFattura){
        return this.fatturaService.findByNumero(numeroFattura);
    }


    // sezione /me

    @GetMapping("/me")
    public Utente trovaIlMioProfilo(@AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        return this.utenteService.findUtenteById(currentAuthenticatedUtente.getIdUtente());
    }

    @PutMapping("/me")
    public Utente modificaIlMioProfilo(@AuthenticationPrincipal Utente currentAuthenticatedUtente, @RequestBody @Validated NewUtenteDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.utenteService.findUtenteByIdAndUpdate(currentAuthenticatedUtente.getIdUtente(), payload);
        }

    }

    @DeleteMapping("/me")
    public void eliminaIlMioProfilo(@AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        this.utenteService.findUtenteByIdAndDelete(currentAuthenticatedUtente.getIdUtente());
    }

    @PatchMapping("/me/{idUtente}/avatar")
    public String uploadImage(@PathVariable UUID idUtente, @RequestParam("avatar") MultipartFile file, @AuthenticationPrincipal Utente currentAuthenticatedUtente) {
        return this.utenteService.uploadAvatar(idUtente, file);
    }

    // Sezione cliente

    @PostMapping("/cliente")
    public Cliente saveCliente(@RequestBody @Validated NewClienteDTO payloadCliente, @RequestBody @Validated NewIndirizzoSedeLegaleDTO payloadIndirizzoSL, @RequestBody @Validated NewIndirizzoSedeOperativaDTO payloadIndirizzoSO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.clienteService.saveCliente(payloadCliente,payloadIndirizzoSL,payloadIndirizzoSO);
        }
    }

    @PutMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente updateCliente(@PathVariable UUID clienteId, @RequestBody @Validated NewClienteDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.clienteService.findClienteByIdAndUpdate(clienteId, payload);
        }

    }

    @DeleteMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteCliente(@PathVariable UUID clienteId) {
        this.clienteService.findClienteByIdAndDelete(clienteId);
    }

    @PostMapping("/cliente/byFatturatoMag")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> findClientiByFatturatoMaggiore(@RequestBody @Validated FatturatoAnnualeDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.clienteService.findClientiByFatturatoMaggiore(payload);
        }

    }

    @PostMapping("/cliente/byFatturatoMin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> findClientiByFatturatoMinore(@RequestBody @Validated FatturatoAnnualeDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.clienteService.findClientiByFatturatoMinore(payload);
        }
    }

    @PostMapping("/cliente/byDataInserimentoBefore")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> findClientiByBeforeDataInserimento(@RequestBody @Validated DataInserimentoDTO payload, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.clienteService.findClientiByBeforeDataInserimento(payload);
        }
    }

    @PostMapping("/cliente/byDataInserimentoAfter")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> findClientiByAfterDataInserimento(@RequestBody @Validated DataInserimentoDTO payload , BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.clienteService.findClientiByAfterDataInserimento(payload);
        }
    }

    @PostMapping("/cliente/byDataUltimoContatto")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> findClientiByDataUltimoContatto(@RequestBody @Validated DataUltimoContattoDTO payload ,BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.clienteService.findClientiByDataUltimoContatto(payload);
        }
    }

    @PostMapping("/cliente/byPartialName")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Cliente> findClientiByPartialName(@RequestBody @Validated PartialNameDTO payload,BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.clienteService.findClientiByPartialName(payload);
        }
    }

    @GetMapping("/cliente")
    public Page<Cliente> getPageClientiSortByName(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "nomeContatto") String sort) {
        return this.clienteService.findAll(pageNumber, pageSize, sort);
    }

    @GetMapping("/cliente/sortByFatturato")
    public Page<Cliente> getPageClientiSortByFatturato(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return this.clienteService.findAll(pageNumber, pageSize, "fatturatoAnnuale");
    }

    @GetMapping("/cliente/sortByDataInserimento")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Cliente> getPageClientiByDataInserimento(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return this.clienteService.findAll(pageNumber, pageSize, "dataInserimento");
    }

    @GetMapping("/cliente/sortByDataUltimoContatto")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Cliente> getPageClientiByDataUltimoContatto(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
        return this.clienteService.findAllReverse(pageNumber, pageSize, "dataUltimoContatto");
    }

    //SEZIONE ADMIN GESTIONE INDIRIZZI

    @PostMapping("/cliente/updateIndirizzoSL/{clienteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Indirizzo findUtenteByIdAndUpdateIndirizzoSL(@PathVariable UUID clienteId, @RequestBody @Validated NewIndirizzoSedeLegaleDTO body,BindingResult validationResult)
    {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.clienteService.findClienteByIdAndUpdateIndirizzoSL(clienteId,body);
        }
    }

    @PostMapping("/cliente/updateIndirizzoSo/{clienteId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Indirizzo findUtenteByIdAndUpdateIndirizzoSO(@PathVariable UUID clienteId, @RequestBody @Validated NewIndirizzoSedeOperativaDTO body,BindingResult validationResult)
    {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        } else {
            return this.clienteService.findClienteByIdAndUpdateIndirizzoSO(clienteId,body);
        }
    }



}