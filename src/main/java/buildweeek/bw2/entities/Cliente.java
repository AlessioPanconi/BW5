package buildweeek.bw2.entities;

import buildweeek.bw2.enums.CustomerType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
//@JsonIgnoreProperties({"password","authorities","enabled","accountNonExpired","credentialsNonExpired","accountNonLocked"})
public class Cliente {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id_cliente")
    private UUID idCliente;
    private String email;
    private String pec;
    private String telefono;
    @Column(name = "partita_iva")
    private String partitaIva;
    @Column(name = "ragione_sociale")
    private String ragioneSociale;
    @Column(name = "data_inserimento")
    private LocalDate dataInserimento;
    @Column(name = "data_ultimo_contatto")
    private LocalDate dataUltimoContatto;
    @Column(name = "fatturato_annuale")
    private double fatturatoAnnuale;
    @Column(name = "email_contatto")
    private String emailContatto;
    @Column(name = "nome_contatto")
    private String nomeContatto;
    @Column(name = "cognome_contatto")
    private String cognomeContatto;
    @Column(name = "telefono_contatto")
    private String telefonoContatto;
    @Column(name = "logo_aziendale")
    private String logoAziendaleURL;
    @Column(name = "customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    @OneToMany
    @JoinColumn(name = "id_cliente")
    private List<Indirizzo> indirizzi;


    @OneToMany
    @JoinColumn(name = "id_cliente")
    private List<Fattura> fatture;

    public Cliente(String email, String pec, String telefono, String partitaIva, String ragioneSociale, LocalDate dataInserimento, LocalDate dataUltimoContatto, double fatturatoAnnuale, String emailContatto, String nomeContatto, String cognomeContatto, String telefonoContatto, CustomerType customerType) {
        this.email = email;
        this.pec = pec;
        this.telefono = telefono;
        this.partitaIva = partitaIva;
        this.ragioneSociale = ragioneSociale;
        this.dataInserimento = dataInserimento;
        this.dataUltimoContatto = dataUltimoContatto;
        this.fatturatoAnnuale = fatturatoAnnuale;
        this.emailContatto = emailContatto;
        this.nomeContatto = nomeContatto;
        this.cognomeContatto = cognomeContatto;
        this.telefonoContatto = telefonoContatto;
        this.customerType = customerType;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "cognomeContatto='" + cognomeContatto + '\'' +
                ", idCliente=" + idCliente +
                ", email='" + email + '\'' +
                ", pec='" + pec + '\'' +
                ", telefono='" + telefono + '\'' +
                ", partitaIva='" + partitaIva + '\'' +
                ", ragioneSociale='" + ragioneSociale + '\'' +
                ", dataInserimento=" + dataInserimento +
                ", dataUltimoContatto=" + dataUltimoContatto +
                ", fatturatoAnnuale=" + fatturatoAnnuale +
                ", emailContatto='" + emailContatto + '\'' +
                ", nomeContatto='" + nomeContatto + '\'' +
                ", telefonoContatto='" + telefonoContatto + '\'' +
                ", logoAziendaleURL='" + logoAziendaleURL + '\'' +
                ", customerType=" + customerType +
                '}';
    }
}
