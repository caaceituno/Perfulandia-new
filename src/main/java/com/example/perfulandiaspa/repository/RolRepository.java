package com.example.perfulandiaspa.repository;

import com.example.perfulandiaspa.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
 
}