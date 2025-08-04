package buildweeek.bw2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "comune")
@Getter
@Setter
@NoArgsConstructor
public class Comune {

    @Id
    @GeneratedValue
    @Column(name = "id_comune")
    private UUID idComune;
    private String nome;

    @OneToMany(mappedBy = "id_indirizzo")
    private List<Indirizzo> indirizzi;

    public Comune(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Comune{" +
                "idComune=" + idComune +
                ", nome='" + nome + '\'' +
                '}';
    }
}
