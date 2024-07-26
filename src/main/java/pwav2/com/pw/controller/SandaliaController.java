package pwav2.com.pw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pwav2.com.pw.domain.Sandalia;
import pwav2.com.pw.repository.SandaliaRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

        return "redirect:/admin";
    }

    @GetMapping("/")
    public String listarSandaliasParaVenda(Model model) {
        List<Sandalia> sandalias = sandaliaRepository.findByIsDeletedIsFalse();
        model.addAttribute("sandalias", sandalias);
        return "index";
    }
}
