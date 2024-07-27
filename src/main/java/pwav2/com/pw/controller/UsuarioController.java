package pwav2.com.pw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pwav2.com.pw.domain.Usuario;
import pwav2.com.pw.service.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/cadastrar")
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("nome") String nome,
            @RequestParam("cpf") String cpf,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "isAdmin", defaultValue = "false") boolean isAdmin,
            Model model) {

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setAdmin(isAdmin);

        usuarioService.create(usuario);

        model.addAttribute("message", "Usuário cadastrado com sucesso!");
        return "login";
    }


}
