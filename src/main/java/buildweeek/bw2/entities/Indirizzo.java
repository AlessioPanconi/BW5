package buildweeek.bw2.entities;

import buildweeek.bw2.enums.IndirizzoType;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    @Setter(AccessLevel.NONE)
    @Column(name = "id_indirizzo")
    private UUID idIndirizzo;
    private String via;
    private String civico;
    private String localita;
    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    private IndirizzoType indirizzoType;
    private int cap;

    @ManyToOne
    @JoinColumn(name = "comune")
    private Comune comune;

    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;

    public Indirizzo(String via, String civico, String localita, int cap, Comune comune,IndirizzoType indirizzoType, Provincia provincia) {
        this.via = via;
        this.civico = civico;
        this.localita = localita;
        this.cap = cap;
        this.comune = comune;
        this.indirizzoType = indirizzoType;
        this.provincia = provincia;
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
