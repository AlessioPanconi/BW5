package buildweeek.bw2.services;

import buildweeek.bw2.DTO.NewUtenteDTO;
import buildweeek.bw2.entities.Ruolo;
import buildweeek.bw2.entities.Utente;
import buildweeek.bw2.exceptions.BadRequestException;
import buildweeek.bw2.exceptions.NotFoundException;
import buildweeek.bw2.repositories.UtenteRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private Cloudinary getImageUploader;


    public Utente findByEmail(String email) {
        return this.utenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Il dipendente con l'email " + email + " non è stato trovato!"));
    }

    public Page<Utente> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return this.utenteRepository.findAll(pageable);
    }

    public Utente findUtenteById(UUID idUtente) {
        return this.utenteRepository.findById(idUtente).orElseThrow(()-> new NotFoundException(idUtente));
    }

    public Utente saveUtente(NewUtenteDTO payload)
    {
        this.utenteRepository.findByEmail(payload.email()).ifPresent(utente -> {
            throw new BadRequestException("L'email " + utente.getEmail() + " appartiene già ad un'altro utente");
        });

        String avatarUrl = "https://ui-avatars.com/api/?name=" + payload.nome() + "+" + payload.cognome();
        List<Ruolo> listRuoli = new ArrayList<>();
        Ruolo ruoloUtente = new Ruolo("UTENTE");
        listRuoli.add(ruoloUtente);
        Utente newUtente =  new Utente(payload.username(), payload.email(),bcrypt.encode(payload.password()), payload.nome(), payload.cognome(),avatarUrl,listRuoli);
        Utente savedNewUtente = this.utenteRepository.save(newUtente);
        System.out.println("L'utente: "+ payload.username()+ "con email: "+ payload.email() +" è stato salvato correttamente");
        return savedNewUtente;
    }
    // rifare update


    public void findUtenteByIdAndPatchRuolo (UUID idUtente)
    {
        Utente found =findUtenteById(idUtente);
        Ruolo ruoloADMIN = new Ruolo("ADMIN");
        found.getRuoli().add(ruoloADMIN);
        this.utenteRepository.save(found);
    }

    public void findUtenteByIdAndRemoveAdmin (UUID idUtente)
    {
        Utente found = findUtenteById(idUtente);
        List<Ruolo> nuoviRuoli = found.getRuoli().stream().filter(ruolo -> !ruolo.equals("ADMIN")).toList();
        found.setRuoli(nuoviRuoli);
        this.utenteRepository.save(found);
    }



    public Utente findUtenteByIdAndUpdate (UUID idUtente, NewUtenteDTO payload)
    {
        Utente found =findUtenteById(idUtente);
        if (!found.getEmail().equals(payload.email()))
            this.utenteRepository.findByEmail(payload.email()).ifPresent(utente -> {
                throw new BadRequestException("L'email " + utente.getEmail() + " appartiene già ad un'altro utente");
            });

        if (!found.getUsername().equals(payload.username()))
            this.utenteRepository.findByUsername(payload.username()).ifPresent(utente -> {
                throw new BadRequestException("Questo username " + utente.getUsername() + " appartiene già ad un'altro utente");
            });

        found.setUsername(payload.username());
        found.setNome(payload.nome());
        found.setCognome(payload.cognome());
        found.setEmail(payload.email());
        found.setPassword(payload.password());

        Utente utenteModificato = this.utenteRepository.save(found);
        System.out.println("Utente modificato correttamente");
        return utenteModificato;
    }

    public String uploadAvatar(UUID idUtente, MultipartFile file) {
        Utente found= findUtenteById(idUtente);
        try {
            Map result = getImageUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageURL = (String) result.get("url");

            found.setAvatarUrl(imageURL);
            this.utenteRepository.save(found);

            return imageURL;
        } catch (Exception e) {
            throw new BadRequestException("Ci sono stati problemi nel salvataggio del file!");
        }
    }

    public void findUtenteByIdAndDelete(UUID idUtente) {
        Utente found = findUtenteById(idUtente);
        this.utenteRepository.delete(found);
    }

}
