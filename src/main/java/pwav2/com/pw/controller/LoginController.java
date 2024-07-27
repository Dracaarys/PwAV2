package pwav2.com.pw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pwav2.com.pw.service.UsuarioService;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model) {

        // Simplesmente redireciona para a página inicial após login bem-sucedido
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String home(Model model) {

        return "index"; //
    }
}