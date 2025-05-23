package com.example.perfulandiaspa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.perfulandiaspa.model.Vendedor;

@Repository
public interface VendedorRepository extends JpaRepository <Vendedor, Integer> {

}
