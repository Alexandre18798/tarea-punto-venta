package com.punto.venta.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.punto.venta.dto.ProductoDTO;
import com.punto.venta.entity.Categoria;
import com.punto.venta.entity.Producto;
import com.punto.venta.repository.CategoriaRepository;
import com.punto.venta.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository) {

        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProductoDTO> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO crear(ProductoDTO dto) {

        Categoria categoria = categoriaRepository
                .findById(dto.getIdCategoria())
                .orElseThrow(() ->
                        new RuntimeException("La categoría no existe"));

        Producto producto = convertToEntity(dto);
        producto.setIdCategoria(categoria);

        Producto productoGuardado =
                productoRepository.save(producto);

        return convertToDTO(productoGuardado);
    }

    private ProductoDTO convertToDTO(Producto producto) {

        ProductoDTO dto = new ProductoDTO();

        dto.setIdProducto(producto.getIdProducto());
        dto.setEstado(producto.getEstado());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());

        if (producto.getIdCategoria() != null) {
            dto.setIdCategoria(
                    producto.getIdCategoria().getIdCategoria());
        }

        return dto;
    }

    private Producto convertToEntity(ProductoDTO dto) {

        Producto producto = new Producto();

        producto.setEstado(
                dto.getEstado() != null ? dto.getEstado() : true);
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());

        return producto;
    }
}