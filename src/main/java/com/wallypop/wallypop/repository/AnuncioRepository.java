package com.wallypop.wallypop.repository;

import com.wallypop.wallypop.entity.Anuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioRepository extends JpaRepository <Anuncio, Long> {
    List<Anuncio> findByUsuario_IdOrderByFechaCreacionDesc(Long usuarioId);

    List<Anuncio> findAllByOrderByFechaCreacionDesc();

    //Anuncio findById(Integer id);
}
