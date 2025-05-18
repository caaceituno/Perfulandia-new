package com.example.perfulandiaspa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.perfulandiaspa.model.Rol;
import com.example.perfulandiaspa.repository.RolRepository;

@Service
public class RolService {
    @Autowired
    private RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
    this.rolRepository = rolRepository;
    }

    public List<Rol> findAll() {
    return rolRepository.findAll();
    }   
}