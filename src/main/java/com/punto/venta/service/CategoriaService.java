package com.punto.venta.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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

    public CategoriaDTO crear(CategoriaDTO dto) {

        Categoria categoria = new Categoria();

        categoria.setEstado(
                dto.getEstado() != null ? dto.getEstado() : true);
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        Categoria guardada =
                categoriaRepository.save(categoria);

        return convertirADTO(guardada);
    }

    private CategoriaDTO convertirADTO(
            Categoria categoria) {

        CategoriaDTO dto = new CategoriaDTO();

        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setEstado(categoria.getEstado());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());

        return dto;
    }
}