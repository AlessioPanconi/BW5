package buildweeek.bw2.services;

import buildweeek.bw2.entities.Cliente;
import buildweeek.bw2.entities.Comune;
import buildweeek.bw2.entities.Indirizzo;
import buildweeek.bw2.exceptions.NotFoundException;
import buildweeek.bw2.repositories.ComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ComuneService {
    @Autowired
    private ComuneRepository comuneRepository;

    public Comune findComuneById(String comune) {
        return this.comuneRepository.findById(comune).orElseThrow(()-> new NotFoundException(comune));
    }

}
