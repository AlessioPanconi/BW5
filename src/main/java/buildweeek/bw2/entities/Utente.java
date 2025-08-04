package buildweeek.bw2.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "utente")
@Getter
@Setter
@NoArgsConstructor
//@JsonIgnoreProperties({"password","authorities","enabled","accountNonExpired","credentialsNonExpired","accountNonLocked"})
public class Utente {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    @Column(name = "id_utente")
    private UUID idUtente;
    private String username;
    private String email;
    private String password;
    private String nome;
    private String cognome;
    @Column(name = "avatar_url")
    private String avatarUrl;

    @ManyToMany
    @JoinTable(
            name = "utente_ruolo",
            joinColumns = @JoinColumn(name = "id_utente", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "id_ruolo", nullable = false)
    )
    private List<Ruolo> ruoli;


    public Utente(String username, String email, String password, String nome, String cognome, String avatarUrl) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "avatarUrl='" + avatarUrl + '\'' +
                ", idUtente=" + idUtente +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                '}';
    }
}
