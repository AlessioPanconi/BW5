package buildweeek.bw2;

import buildweeek.bw2.DTO.NewUtenteDTO;
import buildweeek.bw2.entities.Fattura;
import buildweeek.bw2.entities.Ruolo;
import buildweeek.bw2.entities.StatoFattura;
import buildweeek.bw2.entities.Utente;
import buildweeek.bw2.repositories.FatturaRepository;
import buildweeek.bw2.repositories.RuoloRepository;
import buildweeek.bw2.repositories.StatoFatturaRepository;
import buildweeek.bw2.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RuoloRunner implements CommandLineRunner {

    @Autowired
    private RuoloRepository ruoloRepository;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private StatoFatturaRepository statoFatturaRepository;

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${admin.nome}")
    private String adminNome;
    @Value("${admin.cognome}")
    private String adminCognome;

    @Override
    public void run(String... args) throws Exception {
        if (ruoloRepository.count() == 0) {
            ruoloRepository.save(new Ruolo("UTENTE"));
            ruoloRepository.save(new Ruolo("ADMIN"));
            System.out.println("Ruoli caricati nel database");
        }
        if (statoFatturaRepository.count() == 0) {
            statoFatturaRepository.save(new StatoFattura("PAGATA"));
            statoFatturaRepository.save(new StatoFattura("NON_PAGATA"));
            statoFatturaRepository.save(new StatoFattura("IN_ATTESA"));
            System.out.println("Stati fattura caricati nel database");
        }
        try {
            NewUtenteDTO adminDto = new NewUtenteDTO(
                    adminUsername,
                    adminEmail,
                    adminPassword,
                    adminNome,
                    adminCognome
            );
            Utente admin = utenteService.saveUtente(adminDto);
            utenteService.findUtenteByIdAndPatchRuolo(admin.getIdUtente());
            System.out.println("L'utente admin è stato creato correttamente✅");
        } catch (Exception e) {
            System.out.println("L'utente admin esiste già!");
        }
    }


}
