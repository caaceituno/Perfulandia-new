package com.example.perfulandiaspa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.perfulandiaspa.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    
}