package buildweeek.bw2.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "utente")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"password","authorities","enabled","accountNonExpired","credentialsNonExpired","accountNonLocked"})
public class Utente implements UserDetails{

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "utente_ruolo",
            joinColumns = @JoinColumn(name = "id_utente", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "id_ruolo", nullable = false)
    )
    private List<Ruolo> ruoli;


    public Utente(String username, String email, String password, String nome, String cognome, String avatarUrl, List<Ruolo> ruoli) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.avatarUrl = avatarUrl;
        this.ruoli = ruoli;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "avatarUrl='" + avatarUrl + '\'' +
                ", idUtente=" + idUtente +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", provincia='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ruoli.stream()
                .map(ruolo -> new SimpleGrantedAuthority(ruolo.getNomeRuolo()))
                .collect(Collectors.toList());
    }

}
