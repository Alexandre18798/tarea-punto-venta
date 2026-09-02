package com.punto.venta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.punto.venta.entity.Categoria;

@Repository
public interface CategoriaRepository
        extends JpaRepository<Categoria, Integer> {

    boolean existsByNombreIgnoreCase(String nombre);

    List<Categoria> findByEstadoTrue();

    List<Categoria> findByEstadoTrueAndNombreContainingIgnoreCase(String nombre);

    List<Categoria> findTop2ByEstadoTrueAndNombreContainingIgnoreCase(String nombre);
}