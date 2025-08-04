package buildweeek.bw2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "fattura")
@Getter
@Setter
@NoArgsConstructor
public class Fattura {

    @Id
    @GeneratedValue
    @Column(name = "id_fattura")
    private UUID idFattura;
    @Column(name = "data_fattura")
    private LocalDate dataFattura;
    private double importo;
    private long numero;

    @ManyToOne
    @JoinColumn(name = "id_fattura")
    private StatoFattura statoFattura;

    public Fattura(LocalDate dataFattura, double importo, long numero, StatoFattura statoFattura) {
        this.dataFattura = dataFattura;
        this.importo = importo;
        this.numero = numero;
        this.statoFattura = statoFattura;
    }

    @Override
    public String toString() {
        return "Fattura{" +
                "dataFattura=" + dataFattura +
                ", idFattura=" + idFattura +
                ", importo=" + importo +
                ", numero=" + numero +
                '}';
    }
}
