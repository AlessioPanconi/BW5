package buildweeek.bw2.services;

import buildweeek.bw2.DTO.NewIndirizzoSedeLegaleDTO;
import buildweeek.bw2.DTO.NewIndirizzoSedeOperativaDTO;
import buildweeek.bw2.entities.Cliente;
import buildweeek.bw2.entities.Comune;
import buildweeek.bw2.entities.Indirizzo;
import buildweeek.bw2.enums.IndirizzoType;
import buildweeek.bw2.exceptions.BadRequestException;
import buildweeek.bw2.exceptions.NotFoundException;
import buildweeek.bw2.repositories.IndirizzoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IndirizzoService {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private IndirizzoRepository indirizzoRepository;

    @Autowired
    private ComuneService comuneService;

    public void save(Indirizzo newIndirizzo) {
        this.indirizzoRepository.save(newIndirizzo);
    }

    public Indirizzo findIndirizzoById(UUID idIndirizzo) {
        return this.indirizzoRepository.findById(idIndirizzo).orElseThrow(()-> new NotFoundException(idIndirizzo));
    }

    public Indirizzo findClienteByIdAndUpdateIndirizzoSL(UUID idCliente, NewIndirizzoSedeLegaleDTO payload)
    {
        Cliente foundCliente = this.clienteService.findClienteById(idCliente);
        List<Indirizzo> indirizziCliente = foundCliente.getIndirizzi();

        Optional<Indirizzo> indirizzoFound = indirizziCliente.stream()
                .filter(indirizzo -> indirizzo.getIndirizzoType().equals(IndirizzoType.SEDELEGALE))
                .findFirst();

        Indirizzo indirizzoToUpdate = indirizzoFound.orElseThrow(() ->
                new NotFoundException("Nessun indirizzo con tipo SEDELEGALE trovato.")
        );

        Comune comuneFound = this.comuneService.findComuneById(payload.comune());
        if (comuneFound == null) {
            throw new BadRequestException("Il comune con id " + payload.comune() + " non esiste.");
        }

        indirizzoToUpdate.setVia(payload.via());
        indirizzoToUpdate.setCap(payload.cap());
        indirizzoToUpdate.setCivico(payload.civico());
        indirizzoToUpdate.setLocalita(payload.localita());
        indirizzoToUpdate.setComune(comuneFound);
        indirizzoToUpdate.setProvincia(comuneFound.getProvincia());

        Indirizzo indirizzoSaved = this.indirizzoRepository.save(indirizzoToUpdate);
        System.out.println("Indirizzo modificato correttamente");
        return indirizzoSaved;
    }

    public Indirizzo findClienteByIdAndUpdateIndirizzoSO(UUID idCliente, NewIndirizzoSedeOperativaDTO payload)
    {
        Cliente foundCliente = this.clienteService.findClienteById(idCliente);
        List<Indirizzo> indirizziCliente = foundCliente.getIndirizzi();

        Optional<Indirizzo> indirizzoFound = indirizziCliente.stream()
                .filter(indirizzo -> indirizzo.getIndirizzoType().equals(IndirizzoType.SEDEOPERATIVA))
                .findFirst();

        Indirizzo indirizzoToUpdate = indirizzoFound.orElseThrow(() ->
                new NotFoundException("Nessun indirizzo con tipo SEDEOPERATIVA trovato.")
        );

        Comune comuneFound = this.comuneService.findComuneById(payload.comune());
        if (comuneFound == null) {
            throw new BadRequestException("Il comune con id " + payload.comune() + " non esiste.");
        }

        indirizzoToUpdate.setVia(payload.via());
        indirizzoToUpdate.setCap(payload.cap());
        indirizzoToUpdate.setCivico(payload.civico());
        indirizzoToUpdate.setLocalita(payload.localita());
        indirizzoToUpdate.setComune(comuneFound);
        indirizzoToUpdate.setProvincia(comuneFound.getProvincia());

        Indirizzo indirizzoSaved = this.indirizzoRepository.save(indirizzoToUpdate);
        System.out.println("Indirizzo modificato correttamente");
        return indirizzoSaved;
    }

}
