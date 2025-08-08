package buildweeek.bw2.services;

import buildweeek.bw2.DTO.FatturaDTO;
import buildweeek.bw2.DTO.payloadMetodiClienti.DataDTO;
import buildweeek.bw2.entities.Cliente;
import buildweeek.bw2.entities.Fattura;
import buildweeek.bw2.entities.Ruolo;
import buildweeek.bw2.entities.StatoFattura;
import buildweeek.bw2.exceptions.BadRequestException;
import buildweeek.bw2.exceptions.NotFoundException;
import buildweeek.bw2.repositories.FatturaRepository;
import buildweeek.bw2.repositories.StatoFatturaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class FatturaService {
    @Autowired
    private FatturaRepository fatturaRepository;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private StatoFatturaRepository statoFatturaRepository;

    public Fattura save(UUID idCliente,FatturaDTO payload){

        this.fatturaRepository.findByNumero(payload.numero()).ifPresent(numero->{
            throw new BadRequestException("Esiste già una fattura con questo numero: " + payload.numero());
        });

        Cliente clienteFound = this.clienteService.findClienteById(idCliente);
        StatoFattura statoFattura = this.statoFatturaRepository.findByNomeStatoFattura("NON_PAGATA").orElseThrow(() -> new NotFoundException("STATO FATTURA: NON_PAGATA INESISTENTE!"));

        try {
            LocalDate dataFattura = LocalDate.parse(payload.dataFattura());
        Fattura newF = new Fattura(dataFattura, payload.importo(), payload.numero(), statoFattura,clienteFound);
        clienteFound.getFatture().add(newF);
        Fattura saveF = this.fatturaRepository.save(newF);
        return saveF;
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data inserita non è valido. Il formato corretto è yyyy-mm-dd.");
        }


    }

    public Fattura findById(UUID fatturaId){
        return this.fatturaRepository.findById(fatturaId).orElseThrow(()->new NotFoundException(fatturaId));
    }

    public Fattura findByNumero(long numero){
        return this.fatturaRepository.findByNumero(numero).orElseThrow(()-> new NotFoundException("Non esiste una fattura con questo numero"));
    }

    public Fattura findByIdAndUpdate(UUID fatturaId, FatturaDTO payload){
        try {
        Fattura fnd = this.findById(fatturaId);
            LocalDate dataFattura = LocalDate.parse(payload.dataFattura());
            fnd.setDataFattura(dataFattura);
            fnd.setImporto(payload.importo());
            fnd.setNumero(payload.numero());
            Fattura modFatt = this.fatturaRepository.save(fnd);
            log.info("La fattura con ID: " + fnd.getIdFattura() + " è stata modificata correttamente!");
            return modFatt;
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data inserita non è valido. Il formato corretto è yyyy-mm-dd.");
        }
    }
    public void findByIdAndDelete(UUID fatturaId){
        Fattura fnd = this.findById(fatturaId);
        this.fatturaRepository.delete(fnd);
    }

    public List<Fattura> findByImporto(double importoMin , double importoMax)
    {
        List<Fattura> fattureFiltrate = this.fatturaRepository.findByImportoBetween(importoMin, importoMax);

        if(fattureFiltrate.isEmpty()) throw new NotFoundException("Nessuna fattura trovata per gli importi stabiliti!");

        return fattureFiltrate;
    }

    public List<Fattura> findBydataFattura(DataDTO date){
        try {
            LocalDate dataParsed = LocalDate.parse(date.dataFattura());
        return this.fatturaRepository.findByDataFattura(dataParsed);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data inserita non è valido. Il formato corretto è yyyy-mm-dd.");
        }
    }

    public List<Fattura> findByAnno(int data){

        int anno = data;
        LocalDate inizioAnno = LocalDate.of(anno,1,1);
        LocalDate fineAnno = LocalDate.of(anno,12,31);

        return this.fatturaRepository.findByDataFatturaBetween(inizioAnno,fineAnno);
    }



    public List<Fattura> findByCliente(UUID clienteId){
        Cliente cliente = this.clienteService.findClienteById(clienteId);
        return this.fatturaRepository.findFattureByCliente(cliente);
    }
}
