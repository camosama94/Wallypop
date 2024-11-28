package com.wallypop.wallypop.repository;

import com.wallypop.wallypop.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Usuario findUsuarioByNombre(String nombre);

    Usuario findUsuarioByEmail(String email);
}
