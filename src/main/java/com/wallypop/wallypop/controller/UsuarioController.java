package com.wallypop.wallypop.controller;

import com.wallypop.wallypop.entity.Anuncio;
import com.wallypop.wallypop.entity.Usuario;
import com.wallypop.wallypop.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioController {


    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping ("/wallypop/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/wallypop/alta")
    public String alta(Model model, @Valid Usuario usuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registro";
        }else{
            String encryptedPassword = passwordEncoder.encode(usuario.getPassword());
            usuario.setPassword(encryptedPassword);
            usuarioService.saveUsuario(usuario);
            return "redirect:/";
        }

    }
}
