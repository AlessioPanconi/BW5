package buildweeek.bw2.services;

import buildweeek.bw2.DTO.FatturaDTO;
import buildweeek.bw2.entities.Fattura;
import buildweeek.bw2.exceptions.BadRequestException;
import buildweeek.bw2.exceptions.NotFoundException;
import buildweeek.bw2.repositories.FatturaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FatturaService {
    @Autowired
    private FatturaRepository fatturaRepository;

    public Fattura save(FatturaDTO payload){
        this.fatturaRepository.findByNumero(payload.numero()).ifPresent(numero->{
            throw new BadRequestException("Esiste già una fattura con questo numero: " + payload.numero());
        });
        Fattura newF = new Fattura(payload.dataFattura(), payload.importo(), payload.numero(), payload.statoFattura());
        Fattura saveF = this.fatturaRepository.save(newF);
        return saveF;
    }
    public Fattura findById(UUID fatturaId){
        return this.fatturaRepository.findById(fatturaId).orElseThrow(()->new NotFoundException(fatturaId));
    }
    public Fattura findByNumero(long numero){
        return this.fatturaRepository.findByNumero(numero).orElseThrow(()-> new NotFoundException("Non esiste una fattura con questo numero"));
    }
    public Fattura findByIdAndUpdate(UUID fatturaId, FatturaDTO payload){
        Fattura fnd = this.findById(fatturaId);
            fnd.setDataFattura(payload.dataFattura());
            fnd.setImporto(payload.importo());
            fnd.setNumero(payload.numero());
            Fattura modFatt = this.fatturaRepository.save(fnd);
            log.info("La fattura con numero " + fnd.getNumero() + " è stata modificata correttamente!");
            return modFatt;
    }
    public void findByIdAndDelete(UUID fatturaId){
        Fattura fnd = this.findById(fatturaId);
        this.fatturaRepository.delete(fnd);
    }

    public List<Fattura> findByImporto(double importoMin , double importoMax)
    {
        return this.fatturaRepository.findByImportoBetween(importoMin, importoMax);
    }

    public List<Fattura> findBydataFattura(LocalDate date){
        return this.fatturaRepository.findByDataFattura(date);
    }

    public List<Fattura> findByAnno(int data){

        int anno = data;
        LocalDate inizioAnno = LocalDate.of(anno,1,1);
        LocalDate fineAnno = LocalDate.of(anno,12,31);

        return this.fatturaRepository.findByDataFatturaBetween(inizioAnno,fineAnno);
    }

}
