package com.wallypop.wallypop.repository;

import com.wallypop.wallypop.entity.FotoAnuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoAnuncioRepository extends JpaRepository<FotoAnuncio, Long> {
}
