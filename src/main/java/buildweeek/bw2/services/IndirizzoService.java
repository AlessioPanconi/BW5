package buildweeek.bw2.services;

import buildweeek.bw2.entities.Indirizzo;
import buildweeek.bw2.exceptions.NotFoundException;
import buildweeek.bw2.repositories.IndirizzoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IndirizzoService {

    @Autowired
    private IndirizzoRepository indirizzoRepository;

    public Indirizzo save(Indirizzo newIndirizzo) {
        this.indirizzoRepository.save(newIndirizzo);
        return newIndirizzo;
    }

    public Indirizzo findIndirizzoById(UUID idIndirizzo) {
        return this.indirizzoRepository.findById(idIndirizzo).orElseThrow(()-> new NotFoundException(idIndirizzo));
    }

}
