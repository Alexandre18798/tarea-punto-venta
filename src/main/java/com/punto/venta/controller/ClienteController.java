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

import com.punto.venta.dto.ClienteDTO;
import com.punto.venta.dto.MessageResponse;
import com.punto.venta.service.ClienteService;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteDTO> listarTodos() {
        return clienteService.listarTodos();
    }

    @GetMapping("/mostrarActivos")
    public List<ClienteDTO> mostrarActivos() {
        return clienteService.mostrarActivos();
    }

    @GetMapping("/mostrarActivosFiltro")
    public List<ClienteDTO> mostrarActivosFiltro(
            @RequestParam String nombre) {
        return clienteService.mostrarActivosFiltro(nombre);
    }

    @GetMapping("/mostrarActivosFiltroTop")
    public List<ClienteDTO> mostrarActivosFiltroTop(
            @RequestParam String nombre) {
        return clienteService.mostrarActivosFiltroTop(nombre);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> crearCliente(
            @RequestBody ClienteDTO clienteDTO) {

        clienteService.crear(clienteDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse(
                        "Cliente creado con éxito"
                ));
    }

    @PutMapping("/{idCliente}")
    public ResponseEntity<MessageResponse> actualizarCliente(
            @PathVariable Integer idCliente,
            @RequestBody ClienteDTO clienteDTO) {

        clienteService.actualizar(
                idCliente,
                clienteDTO
        );

        return ResponseEntity.ok(
                new MessageResponse(
                        "Cliente actualizado con éxito"
                )
        );
    }

    @PutMapping("/anular/{idCliente}")
    public ResponseEntity<MessageResponse> anularCliente(
            @PathVariable Integer idCliente) {

        clienteService.anular(idCliente);

        return ResponseEntity.ok(
                new MessageResponse(
                        "Cliente anulado con éxito"
                )
        );
    }

    @DeleteMapping("/{idCliente}")
    public ResponseEntity<MessageResponse> eliminarCliente(
            @PathVariable Integer idCliente) {

        clienteService.eliminar(idCliente);

        return ResponseEntity.ok(
                new MessageResponse(
                        "Cliente eliminado con éxito"
                )
        );
    }
}