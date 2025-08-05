package buildweeek.bw2.repositories;

import buildweeek.bw2.entities.Cliente;
import buildweeek.bw2.entities.Fattura;
import buildweeek.bw2.entities.StatoFattura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FatturaRepository extends JpaRepository<Fattura, UUID> {
    List<Fattura> findByDataFattura(LocalDate date);
    List<Fattura> findByImportoBetween(double importoMin , double importoMax);
    List<Fattura> findByDataFatturaBetween(LocalDate inizio, LocalDate fine);
    Optional<Fattura> findByNumero(long numero);
    List<Fattura> findByStatoFattura(StatoFattura statoFattura);
    List<Fattura> findFattureByCliente(Cliente cliente);
}
