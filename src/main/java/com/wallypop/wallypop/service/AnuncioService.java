package com.wallypop.wallypop.service;

import com.wallypop.wallypop.entity.Anuncio;
import com.wallypop.wallypop.repository.AnuncioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnuncioService {
    private final AnuncioRepository anuncioRepository;
    //private final UsuarioRepository usuarioRepository;

    public AnuncioService(AnuncioRepository anuncioRepository/*, UsuarioRepository usuarioRepository*/) {
        this.anuncioRepository = anuncioRepository;
        //this.usuarioRepository = usuarioRepository;
    }

    public List<Anuncio> findAllAnuncios() {return anuncioRepository.findAllByOrderByFechaCreacionDesc();}

    public Anuncio findAnuncioById(Long id) {
        return anuncioRepository.findById(id).orElse(null);
    }

    public Anuncio saveAnuncio(Anuncio anuncio) {
        return anuncioRepository.save(anuncio);
    }

    public void deleteAnuncioById(Long id) {
        anuncioRepository.deleteById(id);
    }

    public List<Anuncio> findAnunciosByUsuarioIdFechaDesc(Long ID) {
        return anuncioRepository.findByUsuario_IdOrderByFechaCreacionDesc(ID);
    }

}
