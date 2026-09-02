package com.punto.venta.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.punto.venta.dto.CategoriaDTO;
import com.punto.venta.entity.Categoria;
import com.punto.venta.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(
            CategoriaRepository categoriaRepository) {

        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaDTO> listarCategorias() {

        return categoriaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<CategoriaDTO> mostrarActivos() {

        return categoriaRepository.findByEstadoTrue()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<CategoriaDTO> mostrarActivosFiltro(
            String nombre) {

        return categoriaRepository
                .findByEstadoTrueAndNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<CategoriaDTO> mostrarActivosFiltroTop(
            String nombre) {

        return categoriaRepository
                .findTop2ByEstadoTrueAndNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO crear(CategoriaDTO dto) {

        boolean duplicado =
                categoriaRepository
                        .existsByNombreIgnoreCase(
                                dto.getNombre()
                        );

        if (duplicado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La categoría ya existe"
            );
        }

        Categoria categoria =
                convertirAEntidad(dto);

        Categoria guardada =
                categoriaRepository.save(categoria);

        return convertirADTO(guardada);
    }

    public CategoriaDTO actualizar(
            Integer idCategoria,
            CategoriaDTO dto) {

        Categoria categoriaExistente =
                buscarCategoria(idCategoria);

        if (dto.getNombre() != null) {
            categoriaExistente.setNombre(
                    dto.getNombre()
            );
        }

        if (dto.getDescripcion() != null) {
            categoriaExistente.setDescripcion(
                    dto.getDescripcion()
            );
        }

        if (dto.getEstado() != null) {
            categoriaExistente.setEstado(
                    dto.getEstado()
            );
        }

        Categoria actualizada =
                categoriaRepository.save(
                        categoriaExistente
                );

        return convertirADTO(actualizada);
    }

    public CategoriaDTO anular(Integer idCategoria) {

        Categoria categoriaExistente =
                buscarCategoria(idCategoria);

        categoriaExistente.setEstado(false);

        Categoria anulada =
                categoriaRepository.save(
                        categoriaExistente
                );

        return convertirADTO(anulada);
    }

    public void eliminar(Integer idCategoria) {

        Categoria categoriaExististente =
                buscarCategoria(idCategoria);

        categoriaRepository.delete(
                categoriaExististente
        );
    }

    private Categoria buscarCategoria(
            Integer idCategoria) {

        return categoriaRepository
                .findById(idCategoria)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Categoría no encontrada"
                        )
                );
    }

    private CategoriaDTO convertirADTO(
            Categoria categoria) {

        CategoriaDTO dto =
                new CategoriaDTO();

        dto.setIdCategoria(
                categoria.getIdCategoria()
        );
        dto.setNombre(
                categoria.getNombre()
        );
        dto.setDescripcion(
                categoria.getDescripcion()
        );
        dto.setEstado(
                categoria.getEstado()
        );

        return dto;
    }

    private Categoria convertirAEntidad(
            CategoriaDTO dto) {

        Categoria categoria =
                new Categoria();

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(
                dto.getDescripcion()
        );
        categoria.setEstado(
                dto.getEstado() != null
                        ? dto.getEstado()
                        : true
        );

        return categoria;
    }
}