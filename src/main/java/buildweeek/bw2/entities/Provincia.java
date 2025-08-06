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
    @Column(name = "id_provincia")
    private UUID idProvincia;
    private String sigla;
    private String provincia;
    private String regione;

    @OneToMany
    @JoinColumn(name = "id_provincia")
    private List<Comune> comuni;

    public Provincia(String provincia, String sigla, String regione) {
        this.sigla = sigla;
        this.provincia = provincia;
        this.regione = regione;
    }

    @Override
    public String toString() {
        return "Provincia{" +
                "idProvincia=" + idProvincia +
                ", provincia='" + provincia + '\'' +
                ", sigla='" + sigla + '\'' +
                '}';
    }
}
