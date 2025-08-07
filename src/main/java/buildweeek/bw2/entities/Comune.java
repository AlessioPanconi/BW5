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
    private String comune;

    @OneToMany(mappedBy = "comune")
    private List<Indirizzo> indirizzi;

    @ManyToOne
    @JoinColumn(name = "provincia")
    private Provincia provincia;

    public Comune(String comune, Provincia provincia) {
        this.comune = comune;
        this.provincia = provincia;
    }

}
