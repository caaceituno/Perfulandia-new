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

    //metodo para encontrar un inventario por id
    public Inventario findById(int id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    //metodo para guardar un inventario
    public Inventario registroInventario(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    //borra el inventario con ID
    public void eliminarInventario(int id) {
        inventarioRepository.deleteById(id);
    }

    // Actualizar todos los campos editables de un inventario existente
    public Inventario actualizarInventario(int id, Inventario inventarioActualizado) {
        Inventario inventarioExistente = inventarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Inventario con ID " + id + " no encontrado"));
            
        if (inventarioActualizado.getCantidad() != null) {
            if (inventarioActualizado.getCantidad() < 0) {
                throw new IllegalArgumentException("La cantidad no puede ser negativa");
            }
            inventarioExistente.setCantidad(inventarioActualizado.getCantidad());
        }
        
        if (inventarioActualizado.getProducto() != null) {
            inventarioExistente.setProducto(inventarioActualizado.getProducto());
        }
        if (inventarioActualizado.getSucursal() != null) {
            inventarioExistente.setSucursal(inventarioActualizado.getSucursal());
        }

        return inventarioRepository.save(inventarioExistente);
    }

}
