package com.wallypop.wallypop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{usuario.email.notempty}")
    @Email(message = "{usuario.email.valido}")
    @Column(unique = true)
    private String email;
    @NotEmpty(message = "{usuario.password.notempty}")
    @Length(min = 4, message = "{usuario.password.length}")
    private String password;
    private String nombre;
    private String telefono;
    private String poblacion;

    @OneToMany(targetEntity = Anuncio.class, cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<Anuncio> anuncios = new ArrayList<Anuncio>();
}
