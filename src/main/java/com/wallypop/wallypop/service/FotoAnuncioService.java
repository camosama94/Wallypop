package com.wallypop.wallypop.service;

import com.wallypop.wallypop.entity.Anuncio;
import com.wallypop.wallypop.entity.FotoAnuncio;
import com.wallypop.wallypop.repository.AnuncioRepository;
import com.wallypop.wallypop.repository.FotoAnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FotoAnuncioService {
    private static final List<String> PERMITTED_TYPES = List.of("image/jpeg", "image/png", "image/gif", "image/avif", "image/webp");
    private static final long MAX_FILE_SIZE = 10000000;
    private static final String UPLOADS_DIRECTORY = "uploads/imagesAnuncios";

    @Autowired
    private AnuncioRepository anuncioRepository;
    @Autowired
    private FotoAnuncioRepository fotoAnuncioRepository;

    public List<FotoAnuncio> guardarFotos(List<MultipartFile> fotos, Anuncio anuncio) {

        List<FotoAnuncio> listaFotos = new ArrayList<>();

        for (MultipartFile foto : fotos) {
            if (!foto.isEmpty()) {
                validarArchivo(foto);
                String nombreFoto = generarNombreUnico(foto);
                guardarArchivo(foto, nombreFoto);

                FotoAnuncio fotoProducto = FotoAnuncio.builder()
                        .nombre(nombreFoto)
                        .anuncio(anuncio).build();

                listaFotos.add(fotoProducto);
            }
        }

        anuncio.setFotos(listaFotos);
        return listaFotos;
    }

    public void addFoto(MultipartFile foto, Anuncio anuncio) {

        if (!foto.isEmpty()) {
            validarArchivo(foto);
            String nombreFoto = generarNombreUnico(foto);
            guardarArchivo(foto, nombreFoto);

            FotoAnuncio fotoProducto = FotoAnuncio.builder()
                    .nombre(nombreFoto)
                    .anuncio(anuncio).build();


            anuncio.getFotos().add(fotoProducto);
            anuncioRepository.save(anuncio);
        }
    }

    public void validarArchivo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Archivo no seleccionado");
        }
        if (!PERMITTED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("El archivo seleccionado no es una imagen.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Archivo demasiado grande. Sólo se admiten archivos < 10MB");
        }
    }

    public String generarNombreUnico(MultipartFile file) {
        UUID nombreUnico = UUID.randomUUID();
        String extension;
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
            extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        } else {
            throw new IllegalArgumentException("El archivo seleccionado no es una imagen.");
        }
        return nombreUnico + extension;
    }

    public void guardarArchivo(MultipartFile file, String nuevoNombreFoto) {
        Path ruta = Paths.get(UPLOADS_DIRECTORY + File.separator + nuevoNombreFoto);
        //Movemos el archivo a la carpeta y guardamos su nombre en el objeto catgoría
        try {
            byte[] contenido = file.getBytes();
            Files.write(ruta, contenido);
        } catch (
                IOException e) {
            throw new RuntimeException("Error al guardar archivo", e);
        }
    }

    public void deleteFoto(Long idFoto) {
        Optional<FotoAnuncio> fotoAnuncioOptional = fotoAnuncioRepository.findById(idFoto);
        if(fotoAnuncioOptional.isPresent()){
            FotoAnuncio fotoAnuncio = fotoAnuncioOptional.get();
            Path archivoFoto = Paths.get("./uploads/imagesAnuncios/"+fotoAnuncio.getNombre());
            try {
                Files.deleteIfExists(archivoFoto);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fotoAnuncioRepository.deleteById(idFoto);
        }
    }

}