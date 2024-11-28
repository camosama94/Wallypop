package com.wallypop.wallypop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "anuncios")
public class Anuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{anuncio.precio.notNull}")
    @Min(value = 1, message = "{anuncio.precio.Min}")
    private Double precio;
    @NotEmpty(message = "{anuncio.titulo.notEmpty}")
    private String titulo;
    private String descripcion;

    @ManyToOne(targetEntity = Usuario.class)
    @JoinColumn(name = "id_usuario")
    @NotNull(message = "Debe pertenecer a un usuario")
    private Usuario usuario;

    @OneToMany(targetEntity = FotoAnuncio.class, cascade = CascadeType.ALL, mappedBy = "anuncio")
    private List<FotoAnuncio> fotos = new ArrayList<>();

    private LocalDateTime fechaCreacion;
}
