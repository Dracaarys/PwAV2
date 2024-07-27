package pwav2.com.pw.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pwav2.com.pw.domain.Usuario;
import pwav2.com.pw.repository.UsuarioRepository;


import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    UsuarioRepository repository;

    BCryptPasswordEncoder encoder;



    @Autowired
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void create(Usuario u){
        u.setPassword(encoder.encode(u.getPassword()));
        this.repository.save(u);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> user = repository.findByUsername(username);
        if (user.isPresent()){
            return user.get();
        }else{
            throw new UsernameNotFoundException("Username not found");
        }
    }

    public List<Usuario> listAll(){
        return  repository.findAll();
    }

}
