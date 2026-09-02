package com.punto.venta.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.punto.venta.dto.ClienteDTO;
import com.punto.venta.entity.Cliente;
import com.punto.venta.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ClienteDTO> mostrarActivos() {
        return clienteRepository.findByEstadoTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ClienteDTO> mostrarActivosFiltro(String nombre) {
        return clienteRepository
                .findByEstadoTrueAndNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ClienteDTO> mostrarActivosFiltroTop(String nombre) {
        return clienteRepository
                .findTop2ByEstadoTrueAndNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO crear(ClienteDTO dto) {

        boolean duplicado =
                clienteRepository
                        .existsByNombreIgnoreCaseAndApellidoIgnoreCase(
                                dto.getNombre(),
                                dto.getApellido()
                        );

        if (duplicado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El cliente ya existe"
            );
        }

        Cliente cliente = convertToEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);

        return convertToDTO(guardado);
    }

    public ClienteDTO actualizar(
            Integer idCliente,
            ClienteDTO dto) {

        Cliente clienteExistente =
                buscarCliente(idCliente);

        if (dto.getNombre() != null) {
            clienteExistente.setNombre(dto.getNombre());
        }

        if (dto.getApellido() != null) {
            clienteExistente.setApellido(dto.getApellido());
        }

        if (dto.getEmail() != null) {
            clienteExistente.setEmail(dto.getEmail());
        }

        if (dto.getTelefono() != null) {
            clienteExistente.setTelefono(dto.getTelefono());
        }

        if (dto.getEstado() != null) {
            clienteExistente.setEstado(dto.getEstado());
        }

        if (dto.getFechaRegistro() != null) {
            clienteExistente.setFechaRegistro(
                    dto.getFechaRegistro()
            );
        }

        Cliente actualizado =
                clienteRepository.save(clienteExistente);

        return convertToDTO(actualizado);
    }

    public ClienteDTO anular(Integer idCliente) {

        Cliente clienteExistente =
                buscarCliente(idCliente);

        clienteExistente.setEstado(false);

        Cliente anulado =
                clienteRepository.save(clienteExistente);

        return convertToDTO(anulado);
    }

    public void eliminar(Integer idCliente) {

        Cliente clienteExistente =
                buscarCliente(idCliente);

        clienteRepository.delete(clienteExistente);
    }

    private Cliente buscarCliente(Integer idCliente) {

        return clienteRepository.findById(idCliente)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Cliente no encontrado"
                        )
                );
    }

    private ClienteDTO convertToDTO(Cliente cliente) {

        ClienteDTO dto = new ClienteDTO();

        dto.setIdCliente(cliente.getIdCliente());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setEstado(cliente.getEstado());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        dto.setFechaRegistro(cliente.getFechaRegistro());

        return dto;
    }

    private Cliente convertToEntity(ClienteDTO dto) {

        Cliente cliente = new Cliente();

        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEstado(
                dto.getEstado() != null
                        ? dto.getEstado()
                        : true
        );
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        cliente.setFechaRegistro(dto.getFechaRegistro());

        return cliente;
    }
}