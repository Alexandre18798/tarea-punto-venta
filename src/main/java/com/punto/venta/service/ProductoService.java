package com.punto.venta.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public List<ProductoDTO> mostrarActivos() {

        return productoRepository.findByEstadoTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> mostrarActivosFiltro(
            String nombre) {

        return productoRepository
                .findByEstadoTrueAndNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> mostrarActivosFiltroTop(
            String nombre) {

        return productoRepository
                .findTop2ByEstadoTrueAndNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO crear(ProductoDTO dto) {

        Categoria categoria =
                buscarCategoria(dto.getIdCategoria());

        Producto producto =
                convertirAEntidad(dto);

        producto.setIdCategoria(categoria);

        Producto guardado =
                productoRepository.save(producto);

        return convertToDTO(guardado);
    }

    public ProductoDTO actualizar(
            Integer idProducto,
            ProductoDTO dto) {

        Producto productoExistente =
                buscarProducto(idProducto);

        if (dto.getNombre() != null) {
            productoExistente.setNombre(
                    dto.getNombre()
            );
        }

        if (dto.getDescripcion() != null) {
            productoExistente.setDescripcion(
                    dto.getDescripcion()
            );
        }

        if (dto.getPrecio() != null) {
            productoExistente.setPrecio(
                    dto.getPrecio()
            );
        }

        if (dto.getStock() != null) {
            productoExistente.setStock(
                    dto.getStock()
            );
        }

        if (dto.getEstado() != null) {
            productoExistente.setEstado(
                    dto.getEstado()
            );
        }

        if (dto.getIdCategoria() != null) {

            Categoria categoria =
                    buscarCategoria(dto.getIdCategoria());

            productoExistente.setIdCategoria(categoria);
        }

        Producto actualizado =
                productoRepository.save(productoExistente);

        return convertToDTO(actualizado);
    }

    public ProductoDTO anular(Integer idProducto) {

        Producto productoExistente =
                buscarProducto(idProducto);

        productoExistente.setEstado(false);

        Producto anulado =
                productoRepository.save(productoExistente);

        return convertToDTO(anulado);
    }

    public void eliminar(Integer idProducto) {

        Producto productoExistente =
                buscarProducto(idProducto);

        productoRepository.delete(productoExistente);
    }

    private Producto buscarProducto(
            Integer idProducto) {

        return productoRepository
                .findById(idProducto)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Producto no encontrado"
                        )
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

    private ProductoDTO convertToDTO(
            Producto producto) {

        ProductoDTO dto =
                new ProductoDTO();

        dto.setIdProducto(producto.getIdProducto());
        dto.setEstado(producto.getEstado());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());

        if (producto.getIdCategoria() != null) {
            dto.setIdCategoria(
                    producto.getIdCategoria().getIdCategoria()
            );
        }

        return dto;
    }

    private Producto convertirAEntidad(
            ProductoDTO dto) {

        Producto producto =
                new Producto();

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setEstado(
                dto.getEstado() != null
                        ? dto.getEstado()
                        : true
        );

        return producto;
    }
}