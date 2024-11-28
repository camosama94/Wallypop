package com.wallypop.wallypop.service;

import com.wallypop.wallypop.entity.Usuario;
import com.wallypop.wallypop.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService( UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;

        //this.usuarioRepository = usuarioRepository; (,UsuarioRepository usuarioRepository)
    }


    public Usuario findByEmail(String email) { return usuarioRepository.findUsuarioByEmail(email); }

    public Usuario saveUsuario(Usuario usuario) { return usuarioRepository.save(usuario); }
}
