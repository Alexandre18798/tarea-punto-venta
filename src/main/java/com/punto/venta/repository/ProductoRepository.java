package com.punto.venta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.punto.venta.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByEstadoTrue();

    List<Producto> findByEstadoTrueAndNombreContainingIgnoreCase(String nombre);

    List<Producto> findTop2ByEstadoTrueAndNombreContainingIgnoreCase(String nombre);
}