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

import com.punto.venta.dto.CategoriaDTO;
import com.punto.venta.dto.MessageResponse;
import com.punto.venta.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(
            CategoriaService categoriaService) {

        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<CategoriaDTO> listarTodas() {

        return categoriaService
                .listarCategorias();
    }

    @GetMapping("/mostrarActivos")
    public List<CategoriaDTO> mostrarActivos() {

        return categoriaService
                .mostrarActivos();
    }

    @GetMapping("/mostrarActivosFiltro")
    public List<CategoriaDTO> mostrarActivosFiltro(
            @RequestParam String nombre) {

        return categoriaService
                .mostrarActivosFiltro(nombre);
    }

    @GetMapping("/mostrarActivosFiltroTop")
    public List<CategoriaDTO> mostrarActivosFiltroTop(
            @RequestParam String nombre) {

        return categoriaService
                .mostrarActivosFiltroTop(nombre);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> crearCategoria(
            @RequestBody CategoriaDTO categoriaDTO) {

        categoriaService.crear(categoriaDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse(
                        "Categoría creada con éxito"
                ));
    }

    @PutMapping("/{idCategoria}")
    public ResponseEntity<MessageResponse> actualizarCategoria(
            @PathVariable Integer idCategoria,
            @RequestBody CategoriaDTO categoriaDTO) {

        categoriaService.actualizar(
                idCategoria,
                categoriaDTO
        );

        return ResponseEntity.ok(
                new MessageResponse(
                        "Categoría actualizada con éxito"
                )
        );
    }

    @PutMapping("/anular/{idCategoria}")
    public ResponseEntity<MessageResponse> anularCategoria(
            @PathVariable Integer idCategoria) {

        categoriaService.anular(idCategoria);

        return ResponseEntity.ok(
                new MessageResponse(
                        "Categoría anulada con éxito"
                )
        );
    }

    @DeleteMapping("/{idCategoria}")
    public ResponseEntity<MessageResponse> eliminarCategoria(
            @PathVariable Integer idCategoria) {

        categoriaService.eliminar(idCategoria);

        return ResponseEntity.ok(
                new MessageResponse(
                        "Categoría eliminada con éxito"
                )
        );
    }
}