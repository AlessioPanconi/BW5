package buildweeek.bw2.repositories;

import buildweeek.bw2.entities.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuoloRepository extends JpaRepository<Ruolo, UUID> {
    Optional<Ruolo> findByNomeRuolo(String nomeRuolo);
}
