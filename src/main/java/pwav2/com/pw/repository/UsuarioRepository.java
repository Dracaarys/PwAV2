package pwav2.com.pw.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pwav2.com.pw.domain.Usuario;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByUsername(String username);

}