package buildweeek.bw2;

import buildweeek.bw2.entities.Ruolo;
import buildweeek.bw2.repositories.RuoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RuoloRunner implements CommandLineRunner {

    @Autowired
    private RuoloRepository ruoloRepository;

    @Override
    public void run(String... args) throws Exception {
        if (ruoloRepository.count() == 0) {
            ruoloRepository.save(new Ruolo("UTENTE"));
            ruoloRepository.save(new Ruolo("ADMIN"));
            System.out.println("Ruoli caricati nel database");
        }
    }
}
