package desk.help.core.app.dto.login;

import desk.help.core.domains.UserDomain;
import desk.help.core.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class UserDetailsDto implements UserDetails {

    private final UUID uuid;
    private final String nome;
    private final String email;
    private final String senha;
    private final Role role;

    public UserDetailsDto(UserDomain user) {
        this.uuid = user.getUuid();
        this.nome = user.getName();
        this.email = user.getEmail();
        this.senha = user.getPassword();
        this.role = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return List.of();
        }

        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getRole() {
        return role != null ? role.name() : null;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
