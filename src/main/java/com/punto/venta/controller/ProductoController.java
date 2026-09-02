package com.punto.venta.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.punto.venta.dto.MessageResponse;
import com.punto.venta.dto.ProductoDTO;
import com.punto.venta.service.ProductoService;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(
            ProductoService productoService) {

        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoDTO> listarTodos() {

        return productoService
                .listarProductos();
    }

    @GetMapping("/mostrarActivos")
    public List<ProductoDTO> mostrarActivos() {

        return productoService
                .mostrarActivos();
    }

    @GetMapping("/mostrarActivosFiltro")
    public List<ProductoDTO> mostrarActivosFiltro(
            @RequestParam String nombre) {

        return productoService
                .mostrarActivosFiltro(nombre);
    }

    @GetMapping("/mostrarActivosFiltroTop")
    public List<ProductoDTO> mostrarActivosFiltroTop(
            @RequestParam String nombre) {

        return productoService
                .mostrarActivosFiltroTop(nombre);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> crearProducto(
            @RequestBody ProductoDTO productoDTO) {

        productoService.crear(productoDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse(
                        "Producto creado con éxito"
                ));
    }

    @PutMapping("/{idProducto}")
    public ResponseEntity<MessageResponse> actualizarProducto(
            @PathVariable Integer idProducto,
            @RequestBody ProductoDTO productoDTO) {

        productoService.actualizar(
                idProducto,
                productoDTO
        );

        return ResponseEntity.ok(
                new MessageResponse(
                        "Producto actualizado con éxito"
                )
        );
    }

    @PutMapping("/anular/{idProducto}")
    public ResponseEntity<MessageResponse> anularProducto(
            @PathVariable Integer idProducto) {

        productoService.anular(idProducto);

        return ResponseEntity.ok(
                new MessageResponse(
                        "Producto anulado con éxito"
                )
        );
    }

    @DeleteMapping("/{idProducto}")
    public ResponseEntity<MessageResponse> eliminarProducto(
            @PathVariable Integer idProducto) {

        productoService.eliminar(idProducto);

        return ResponseEntity.ok(
                new MessageResponse(
                        "Producto eliminado con éxito"
                )
        );
    }
}