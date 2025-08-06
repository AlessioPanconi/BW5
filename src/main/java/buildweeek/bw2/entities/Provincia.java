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
    private String provincia;
    private String sigla;
    private String regione;

    public Provincia(String provincia, String sigla, String regione) {
        this.provincia = provincia;
        this.sigla = sigla;
        this.regione = regione;
    }

}
