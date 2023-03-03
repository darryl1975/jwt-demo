package sg.edu.nus.iss.JWT.demo.model;

import java.util.HashSet;
import java.util.Set;
// import java.util.Collection;
// import java.util.List;
// import java.util.ArrayList;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// @Entity
// @Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    // private String firstname;

    // private String lastname;

    @NotBlank
    @Size(max = 20)
    private String username;

    private String email;

    private String password;

    // @Enumerated(EnumType.STRING)
    // private ERole role;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", 
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {

    //     List<SimpleGrantedAuthority> lsga = new ArrayList<>();

    //     for(Role r : roles) {
    //         SimpleGrantedAuthority sga = new SimpleGrantedAuthority(r.getName().toString());
    //         System.out.println("SimpleGrantedAuthority: " + sga.toString());
    //         lsga.add(sga);
    //     }
    //     return lsga;
    // }

    // @Override
    // public String getUsername() {
    //     return email;
    // }

    // @Override
    // public boolean isAccountNonExpired() {
    //     return true;
    // }

    // @Override
    // public boolean isAccountNonLocked() {
    //     return true;
    // }

    // @Override
    // public boolean isCredentialsNonExpired() {
    //     return true;
    // }

    // @Override
    // public boolean isEnabled() {
    //     return true;
    // }
}
