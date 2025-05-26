package com.example.perfulandiaspa.repository;

import com.example.perfulandiaspa.model.Inventario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventarioRepository extends JpaRepository < Inventario, Integer> {
    List<Inventario> findByIdAndCantidad(int id, int cantidad);
}