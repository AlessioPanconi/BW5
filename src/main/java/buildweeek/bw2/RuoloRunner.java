package buildweeek.bw2;

import buildweeek.bw2.DTO.NewUtenteDTO;
import buildweeek.bw2.entities.Ruolo;
import buildweeek.bw2.entities.Utente;
import buildweeek.bw2.repositories.RuoloRepository;
import buildweeek.bw2.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RuoloRunner implements CommandLineRunner {

    @Autowired
    private RuoloRepository ruoloRepository;
    @Autowired
    private UtenteService utenteService;

    @Override
    public void run(String... args) throws Exception {
        if (ruoloRepository.count() == 0) {
            ruoloRepository.save(new Ruolo("UTENTE"));
            ruoloRepository.save(new Ruolo("ADMIN"));
            System.out.println("Ruoli caricati nel database");
        }
        try {
            NewUtenteDTO adminDto = new NewUtenteDTO(
                    "admin",
                    "emailAdmin@gmail.com",
                    "Admin123!",
                    "Mario",
                    "Rossi"
            );
            Utente admin = utenteService.saveUtente(adminDto);
            utenteService.findUtenteByIdAndPatchRuolo(admin.getIdUtente());
            System.out.println("L'utente admin è stato creato correttamente✅");
        } catch (Exception e) {
            System.out.println("L'utente admin esiste già!");
        }
    }


}
