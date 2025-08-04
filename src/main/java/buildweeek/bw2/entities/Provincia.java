package buildweeek.bw2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "provincia")
@Getter
@Setter
@NoArgsConstructor
public class Provincia {

    @Id
    @GeneratedValue
    @Column(name = "id_provincia")
    private UUID idProvincia;
    private String nome;
    private String sigla;

    @OneToMany(mappedBy = "id_comune")
    private List<Comune> comuni;

    public Provincia(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    @Override
    public String toString() {
        return "Provincia{" +
                "idProvincia=" + idProvincia +
                ", nome='" + nome + '\'' +
                ", sigla='" + sigla + '\'' +
                '}';
    }
}
