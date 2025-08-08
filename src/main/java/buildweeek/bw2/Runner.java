package buildweeek.bw2;

import buildweeek.bw2.entities.Comune;
import buildweeek.bw2.entities.Provincia;
import buildweeek.bw2.repositories.ComuneRepository;
import buildweeek.bw2.repositories.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Optional;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private ComuneRepository comuneRepository;

    @Override
    public void run(String... args) throws Exception {

        String provincePath = "src/main/java/buildweeek/bw2/csv/province-italiane.csv";
        String comuniPath = "src/main/java/buildweeek/bw2/csv/comuni-italiani.csv";

        caricaProvince(provincePath);
        caricaComuni(comuniPath);
    }

    private void caricaProvince(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";");

                String nomeProvincia = data[1].trim();
                String sigla = data[0].trim();
                String regione = data[2].trim();

                Provincia provincia = new Provincia(nomeProvincia, sigla, regione);

                if (!provinciaRepository.existsById(nomeProvincia)) {
                    provinciaRepository.save(provincia);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void caricaComuni(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(";");

                String nomeComune = data[2].trim();
                String nomeProvinciaRaw = data[3].trim();

                String nomeProvincia = normalizzaProvincia(nomeProvinciaRaw);

                Optional<Provincia> provinciaOpt = provinciaRepository.findById(nomeProvincia);
                if (provinciaOpt.isPresent()) {
                    Provincia provincia = provinciaOpt.get();
                    if (!comuneRepository.existsById(nomeComune)) {
                        Comune comune = new Comune(nomeComune, provincia);
                        comuneRepository.save(comune);
                    }
                } else {
                    System.out.println("Provincia non trovata: " + nomeProvincia);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String normalizzaProvincia(String nome) {
        return switch (nome) {
            case "Valle d'Aosta/Vallée d'Aoste" -> "Aosta";
            case "Verbano-Cusio-Ossola" -> "Verbania";
            case "Monza e della Brianza" -> "Monza-Brianza";
            case "Bolzano/Bozen" -> "Bolzano";
            case "La Spezia" -> "La-Spezia";
            case "Reggio nell'Emilia" -> "Reggio-Emilia";
            case "Forlì-Cesena" -> "Forli-Cesena";
            case "Pesaro e Urbino" -> "Pesaro-Urbino";
            case "Ascoli Piceno" -> "Ascoli-Piceno";
            case "Reggio Calabria" -> "Reggio-Calabria";
            case "Vibo Valentia" -> "Vibo-Valentia";
            case "Sud Sardegna" -> "Cagliari";
            default -> nome;
        };
    }
}

