package buildweeek.bw2.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "ruolo")
@Getter
@Setter
@NoArgsConstructor
public class Ruolo {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id_ruolo")
    private UUID idRuolo;
    @Column(name = "nome_ruolo")
    private String nomeRuolo;

    public Ruolo(String nomeRuolo) {
        this.nomeRuolo = nomeRuolo;
    }

    @Override
    public String toString() {
        return "Ruolo{" +
                "nomeRuolo='" + nomeRuolo + '\'' +
                ", idRuolo=" + idRuolo +
                '}';
    }
}
