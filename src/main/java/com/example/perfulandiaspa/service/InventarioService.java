package com.example.perfulandiaspa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.perfulandiaspa.model.Inventario;
import com.example.perfulandiaspa.repository.InventarioRepository;   

@Service
public class InventarioService {
    
    @Autowired
    private InventarioRepository inventarioRepository;

    //recibiendo instancia de InventarioRepository como parámetro para asignar al atributo
    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    //metodo para obtener el inventario
    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }
    //metodo para eliminar el inventario
    public void eliminarInventario(int id) {
        throw new UnsupportedOperationException("Unimplemented method 'eliminarInventario'");
    }
    //metodo para guardar el inventario
    public Inventario guardarInventario(Inventario inventario) {
        throw new UnsupportedOperationException("Unimplemented method 'guardarInventario'");
    }
    //metodo para actualizar el inventario
    public Inventario actualizarInventario(Inventario inventario) {
        throw new UnsupportedOperationException("Unimplemented method 'actualizarInventario'");
    }

}
