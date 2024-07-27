package pwav2.com.pw.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pwav2.com.pw.domain.Sandalia;
import pwav2.com.pw.repository.SandaliaRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class SandaliaController {

    @Autowired
    private SandaliaRepository sandaliaRepository;

    @GetMapping("/admin")
    public String listarSandalias(Model model) {
        List<Sandalia> sandalias = sandaliaRepository.findByIsDeletedIsFalse();
        model.addAttribute("sandalias", sandalias);
        return "admin";
    }

    @GetMapping("/cadastro")
    public String cadastroForm(Model model) {
        model.addAttribute("sandalia", new Sandalia());
        return "cadastro";
    }

    @PostMapping("/cadastrar")
    public String cadastrarSandalia(@RequestParam("imageUri") MultipartFile file,
                                    @RequestParam("marca") String marca,
                                    @RequestParam("modelo") String modelo,
                                    @RequestParam("tamanho") Integer tamanho,
                                    @RequestParam("preco") Float preco,
                                    @RequestParam("cor") String cor) throws IOException {
        Sandalia sandalia = new Sandalia();
        sandalia.setId(UUID.randomUUID().toString()); // Gerar ID único
        sandalia.setMarca(marca.toUpperCase());
        sandalia.setModelo(modelo.toUpperCase());
        sandalia.setTamanho(tamanho);
        sandalia.setPreco(preco);
        sandalia.setCor(cor);
        sandalia.setIsDeleted(false);

        // Defina o caminho onde a imagem será salva
        String uploadDir = "C:\\Users\\adler\\OneDrive\\Área de Trabalho\\PW AV2\\pw\\src\\main\\resources\\static\\img";

        // Salve a imagem no diretório especificado
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        File destFile = new File(uploadDir, fileName);
        file.transferTo(destFile);

        // Defina o URI da imagem na entidade Sandalia
        sandalia.setImageUri("/img/" + fileName);

        sandaliaRepository.save(sandalia);

        return "redirect:/cadastro";
    }

    @GetMapping("/")
    public String listarSandaliasParaVenda(Model model, HttpServletResponse response) {
        // Adiciona o cookie "visita" com a data e hora do acesso
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")); // substitui espaços por underscores
        Cookie visitCookie = new Cookie("visita", formattedDateTime);
        visitCookie.setMaxAge(24 * 60 * 60); // 24 horas em segundos
        visitCookie.setPath("/"); // Torna o cookie disponível para toda a aplicação
        response.addCookie(visitCookie);

        List<Sandalia> sandalias = sandaliaRepository.findByIsDeletedIsFalse();
        model.addAttribute("sandalias", sandalias);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);

        return "index";
    }

    @GetMapping("/admin/editar/{id}")
    public String editarForm(@PathVariable String id, Model model) {
        Optional<Sandalia> sandaliaOpt = sandaliaRepository.findById(id);
        if (sandaliaOpt.isPresent()) {
            model.addAttribute("sandalia", sandaliaOpt.get());
            return "editar";
        } else {
            return "redirect:/admin";
        }
    }

    @PostMapping("/admin/editar/{id}")
    public String editarSandalia(@PathVariable String id,
                                 @RequestParam("imageUri") MultipartFile file,
                                 @RequestParam("marca") String marca,
                                 @RequestParam("modelo") String modelo,
                                 @RequestParam("tamanho") Integer tamanho,
                                 @RequestParam("preco") Float preco,
                                 @RequestParam("cor") String cor) throws IOException {
        Optional<Sandalia> sandaliaOpt = sandaliaRepository.findById(id);
        if (sandaliaOpt.isPresent()) {
            Sandalia sandalia = sandaliaOpt.get();
            sandalia.setMarca(marca.toUpperCase());
            sandalia.setModelo(modelo.toUpperCase());
            sandalia.setTamanho(tamanho);
            sandalia.setPreco(preco);
            sandalia.setCor(cor);

            if (!file.isEmpty()) {
                // Defina o caminho onde a imagem será salva
                String uploadDir = "C:\\Users\\adler\\OneDrive\\Área de Trabalho\\PW AV2\\pw\\src\\main\\resources\\static\\img";

                // Salve a imagem no diretório especificado
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                File destFile = new File(uploadDir, fileName);
                file.transferTo(destFile);

                // Defina o URI da imagem na entidade Sandalia
                sandalia.setImageUri("/img/" + fileName);
            }

            sandaliaRepository.save(sandalia);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/deletar/{id}")
    public String deletarSandalia(@PathVariable String id, RedirectAttributes redirectAttributes) {
        Optional<Sandalia> sandaliaOpt = sandaliaRepository.findById(id);
        if (sandaliaOpt.isPresent()) {
            Sandalia sandalia = sandaliaOpt.get();
            sandalia.setIsDeleted(true);
            sandalia.setDeletedAt(new Date().getTime());
            sandaliaRepository.save(sandalia);
            redirectAttributes.addFlashAttribute("successMessage", "Sandália deletada com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Sandália não encontrada.");
        }
        return "redirect:/admin";
    }

    @GetMapping("/adicionarCarrinho/{id}")
    public void adicionarAoCarrinho(@PathVariable(name = "id") String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        List<Sandalia> carrinho = (List<Sandalia>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }
        Optional<Sandalia> produtoOptional = sandaliaRepository.findById(id);
        if (produtoOptional.isPresent()) {
            Sandalia sandalia = produtoOptional.get();
            carrinho.add(sandalia);
        }
        session.setAttribute("carrinho", carrinho);
        response.sendRedirect("/");
    }

    @GetMapping("/carrinhoPage")
    public String exibirCarrinho(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        List<Sandalia> carrinho = (List<Sandalia>) session.getAttribute("carrinho");

        if (carrinho == null || carrinho.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "O seu carrinho está vazio.");
            return "redirect:/";
        } else {
            model.addAttribute("carrinho", carrinho);
            model.addAttribute("quantidadeProdutos", carrinho.size());
            return "carrinhoPage";
        }
    }

//    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
//    public String getIndex(Model model, HttpServletRequest request, HttpServletResponse response){
//
//        Cookie[] cookies = request.getCookies();
//        boolean visitCookieExists = false;
//        String lastAccess = null;
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("lastAccess")) {
//                    visitCookieExists = true;
//                    // Obter o valor do cookie que representa a última data e hora de acesso
//                    lastAccess = cookie.getValue();
//                    break;
//                }
//            }
//        }
//
//        // Se o cookie de data e hora de acesso não existir, criar um novo cookie com a data e hora atual
//        if (!visitCookieExists) {
//            // Obter a data e hora atual
//            LocalDateTime now = LocalDateTime.now();
//            // Converter a data e hora em uma string no formato desejado, substituindo espaços por underscores
//            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));
//            formattedDateTime = formattedDateTime.replace(" ", "_"); // Replace space with underscore
//            Cookie visitCookie = new Cookie("lastAccess", formattedDateTime);
//            response.addCookie(visitCookie);
//            lastAccess = formattedDateTime;
//        }
//
//        List<Sandalia> sandaliasList = sandaliaRepository.findByIsDeletedIsFalse();
//        model.addAttribute("sandaliasList", sandaliasList);
//
//
//        HttpSession session = request.getSession();
//        List<Sandalia> carrinho = (List<Sandalia>) session.getAttribute("carrinho");
//        int quantidadeProdutos = carrinho != null ? carrinho.size() : 0;
//        model.addAttribute("quantidadeProdutos", quantidadeProdutos);
//
//        model.addAttribute("lastAccess", lastAccess); // Adicionar a data e hora de acesso ao modelo
//        return "index";
//    } APLIQUEI OUTRO COOKIE EM USUARIO CONTROLLER

    @GetMapping("/finalizarCompra")
    public String finalizarCompra(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("carrinho"); // Remove os itens do carrinho da sessão
        }
        return "redirect:/"; // Redireciona para a página index
    }





}
