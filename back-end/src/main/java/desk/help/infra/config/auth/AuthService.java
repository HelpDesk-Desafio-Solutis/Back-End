package desk.help.infra.config.auth;

import desk.help.core.app.dto.login.UserDetailsDto;
import desk.help.core.app.dto.login.UserLoginDto;
import desk.help.core.app.dto.login.UserTokenDto;
import desk.help.core.domains.UserDomain;
import desk.help.core.gateway.UserGateway;
import desk.help.infra.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserGateway gateway;
    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return gateway.findByEmail(username)
                .map(UserDetailsDto::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    public UserTokenDto autenticar(UserLoginDto loginDto, AuthenticationManager manager) {
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication auth = manager.authenticate(credentials);
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetailsDto userDetails = (UserDetailsDto) auth.getPrincipal();

        String token = jwtService.generateToken(
                userDetails.getUsername(),
                userDetails.getRole(),
                userDetails.getUuid()
        );

        UserDomain user = gateway.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(404, "E-Mail não encontrado", null));

        return UserMapper.of(user, token);
    }

}
