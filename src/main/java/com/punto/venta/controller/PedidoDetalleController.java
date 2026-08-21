package com.punto.venta.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.punto.venta.dto.PedidoDetalleDTO;
import com.punto.venta.service.PedidoDetalleService;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/pedidos-detalles")
@CrossOrigin(origins = "*")
public class PedidoDetalleController {

    private final PedidoDetalleService pedidoDetalleService;

    public PedidoDetalleController(
        PedidoDetalleService pedidoDetalleService) {
            
            this.pedidoDetalleService = pedidoDetalleService;
        }
    
@GetMapping
    public List<PedidoDetalleDTO> listarTodos() {
        return pedidoDetalleService.listarDetalles();
    }

@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public PedidoDetalleDTO crearDetalle(
       @RequestBody PedidoDetalleDTO pedidoDetalleDTO) {

    return pedidoDetalleService.crear(pedidoDetalleDTO);
       }
}