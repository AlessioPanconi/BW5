package buildweeek.bw2.repositories;

import buildweeek.bw2.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FatturaRepository extends JpaRepository<Fattura, UUID> {
//    List<Fattura> findBydataFattura(List<Fattura> fatture, LocalDate date);
//    List<Fattura> findByImportoBetween(List<Fattura> fatture, double importoMin , double importoMax);
    Optional<Fattura> findByNumero(long numero);
}
