package com.wallypop.wallypop.controller;

import com.wallypop.wallypop.entity.Anuncio;
import com.wallypop.wallypop.entity.FotoAnuncio;
import com.wallypop.wallypop.entity.Usuario;
import com.wallypop.wallypop.service.AnuncioService;
import com.wallypop.wallypop.service.FotoAnuncioService;
import com.wallypop.wallypop.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class AnuncioController {

    private final AnuncioService anuncioService;
    private final FotoAnuncioService fotoAnuncioService;
    @Autowired
    private UsuarioService usuarioService;


    public AnuncioController(AnuncioService anuncioService, FotoAnuncioService fotoAnuncioService) {
        this.anuncioService = anuncioService;
        this.fotoAnuncioService = fotoAnuncioService;
        //this.usuarioRepository = usuarioRepository; (,UsuarioRepository usuarioRepository)
    }


    @GetMapping ("/")
    public String index(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
        String nombre = authentication.getName();
        Long id = usuarioService.findByEmail(nombre).getId();



            if(id != null) {
                model.addAttribute("userID", id);
                model.addAttribute("anuncios", anuncioService.findAllAnuncios());
                model.addAttribute("total", anuncioService.findAllAnuncios().size());
                return "anuncios.html";
            }
        }
            model.addAttribute("anuncios", anuncioService.findAllAnuncios());
            model.addAttribute("total", anuncioService.findAllAnuncios().size());
            return "anuncios.html";



    }

    @GetMapping("/wallypop/anuncio/ver/{id}")
    public String show(@PathVariable Long id, Model model, Authentication authentication) {

        Anuncio anuncio = anuncioService.findAnuncioById(id);

        if (anuncio != null) {
            if (authentication != null && authentication.isAuthenticated()) {
                String nombre = authentication.getName();
                Long userId = usuarioService.findByEmail(nombre).getId();
                model.addAttribute("userID", userId);
            }
            Usuario usuario = anuncio.getUsuario();

            // Añade los atributos al modelo
            model.addAttribute("anuncio", anuncio);
            model.addAttribute("usuario", usuario);

            return "ver-anuncio";
        } else {

            return "redirect:/";
        }
    }

    @GetMapping ("/wallypop/anuncio/new")
    public String newAnuncio(Model model) {
        model.addAttribute("anuncio", new Anuncio());
        return "new-anuncio";
    }

    @PostMapping("/wallypop/anuncio/new")
    public String newAnuncioInsert(Authentication authentication, Model model, @Valid Anuncio anuncio, BindingResult bindingResult,
                                   @RequestParam(value = "archivosFotos", required = false) List<MultipartFile> fotos) {

        String nombre = authentication.getName();
        Optional <Usuario> user = Optional.ofNullable(usuarioService.findByEmail(nombre));

        if (bindingResult.hasErrors()) {
            return "new-anuncio";
        }

        if(user.isPresent()) {
            Usuario usu = user.get();
            anuncio.setUsuario(usu);

            anuncio.setFechaCreacion(LocalDateTime.now());

            try {
                fotoAnuncioService.guardarFotos(fotos, anuncio);
            }catch (IllegalArgumentException ex) {
                model.addAttribute("mensaje", ex.getMessage());
                return "producto-new";
            }
            anuncioService.saveAnuncio(anuncio);
            return "redirect:/wallypop/misanuncios";
        }else{
            model.addAttribute("error", "Usuario no encontrado"+nombre+"");
            return "error";
        }

        //Optional<Usuario> anunciante = usuarioRepository.findById(2L);
        //anuncio.setUsuario(anunciante.get());
        //anuncio.setUsuario(user);
        //anuncioService.saveAnuncio(anuncio);

    }



    @GetMapping("/wallypop/borrar/{id}")
    public String borrarAnuncio(@PathVariable Long id, Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        if (authentication != null && authentication.isAuthenticated()) {
            String nombre = authentication.getName();
            Long userId = usuarioService.findByEmail(nombre).getId();

            Anuncio anuncio =anuncioService.findAnuncioById(id);
            if(userId == anuncio.getUsuario().getId()) {
                List<FotoAnuncio> anuncios = anuncio.getFotos();
                for(FotoAnuncio fotoAnuncio : anuncios) {
                    fotoAnuncioService.deleteFoto(fotoAnuncio.getId());
                }
                anuncioService.deleteAnuncioById(id);
                return "redirect:/wallypop/misanuncios";
            }
            redirectAttributes.addFlashAttribute("error", "No puedes borrar anuncios que no te pertenecen.");
            return "redirect:/wallypop/misanuncios";
        }
        return "redirect:/";
    }

    @GetMapping ("/wallypop/anuncio/edit/{id}")
    public String editAnuncio(Model model, @PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {

        if (authentication != null && authentication.isAuthenticated()) {
            String nombre = authentication.getName();
            Long userId = usuarioService.findByEmail(nombre).getId();

            Anuncio anuncio = anuncioService.findAnuncioById(id);
            if (anuncio != null) {
                if (userId == anuncio.getUsuario().getId()) {

                    model.addAttribute("anuncio", anuncio);
                    return "edit-anuncio";

                }
                redirectAttributes.addFlashAttribute("error", "No puedes editar anuncios que no te pertenecen.");
                return "redirect:/wallypop/misanuncios";
            }
            redirectAttributes.addFlashAttribute("error", "No existe el anuncio.");
            return "redirect:/wallypop/misanuncios";
        }
        return "redirect:/";
    }

    @PostMapping ("/wallypop/anuncio/edit/{id}")
    public String editAnuncioInsert(@PathVariable Long id ,@Valid Anuncio anuncio, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-anuncio";
        }
        anuncio.setId(id);
        anuncioService.saveAnuncio(anuncio);
        return "redirect:/wallypop/misanuncios";
    }

    @GetMapping ("/wallypop/misanuncios")
    public String misAnuncios(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String nombre = authentication.getName();
            Long id = usuarioService.findByEmail(nombre).getId();



            if(id != null) {
                model.addAttribute("userID", id);
                model.addAttribute("anuncios", anuncioService.findAnunciosByUsuarioIdFechaDesc(id));
                model.addAttribute("total", anuncioService.findAnunciosByUsuarioIdFechaDesc(id).size());
                model.addAttribute("activar",true);
                return "anuncios.html";
            }
        }
        model.addAttribute("anuncios", anuncioService.findAllAnuncios());
        model.addAttribute("total", anuncioService.findAllAnuncios().size());
        return "anuncios.html";



    }
}
