package buildweeek.bw2.repositories;

import buildweeek.bw2.entities.Fattura;
import buildweeek.bw2.entities.StatoFattura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatoFatturaRepository extends JpaRepository<StatoFattura, UUID> {
    Optional<StatoFattura> findByNomeStatoFattura(String nomeStatoFattura);

}
