package buildweeek.bw2.repositories;

import buildweeek.bw2.DTO.FatturaDTO;
import buildweeek.bw2.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FatturaRepository extends JpaRepository<Fattura, UUID> {
    Fattura findBydataFattura(LocalDate date);
    List<Fattura> findByImporto(double importo);
    Optional<Fattura> findByNumero(long numero);
}
