package buildweeek.bw2.repositories;

import buildweeek.bw2.entities.Fattura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FatturaRepository extends JpaRepository<Fattura, UUID> {
}
