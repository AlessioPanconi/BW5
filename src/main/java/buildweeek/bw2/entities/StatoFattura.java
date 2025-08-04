package buildweeek.bw2.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "stato_fattura")
@Getter
@Setter
@NoArgsConstructor
public class StatoFattura {

    @Id
    @GeneratedValue
    @Column(name = "id_stato_fattura")
    private UUID idStatoFattura;
    @Column(name = "nome_stato_fattura")
    private String nomeStatoFattura;

    public StatoFattura(String nomeStatoFattura) {
        this.nomeStatoFattura = nomeStatoFattura;
    }

    @Override
    public String toString() {
        return "StatoFattura{" +
                "idStatoFattura=" + idStatoFattura +
                ", nomeStatoFattura='" + nomeStatoFattura + '\'' +
                '}';
    }
}
