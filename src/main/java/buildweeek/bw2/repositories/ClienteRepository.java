package buildweeek.bw2.repositories;

import buildweeek.bw2.entities.Cliente;
import buildweeek.bw2.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByPec(String pec);
    Optional<Cliente> findByTelefono(String telefono);
    Optional<Cliente> findByPartitaIva(String partitaIva);

}
