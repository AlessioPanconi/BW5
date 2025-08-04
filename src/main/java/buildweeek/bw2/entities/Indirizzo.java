package buildweeek.bw2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "indirizzo")
@Getter
@Setter
@NoArgsConstructor
public class Indirizzo {

    @Id
    @GeneratedValue
    @Column(name = "id_indirizzo")
    private UUID idIndirizzo;
    private String via;
    private String civico;
    private String localita;
    private int cap;

    public Indirizzo(String via, String civico, String localita, int cap) {
        this.via = via;
        this.civico = civico;
        this.localita = localita;
        this.cap = cap;
    }

    @Override
    public String toString() {
        return "Indirizzo{" +
                "cap=" + cap +
                ", idIndirizzo=" + idIndirizzo +
                ", via='" + via + '\'' +
                ", civico='" + civico + '\'' +
                ", localita='" + localita + '\'' +
                '}';
    }
}
