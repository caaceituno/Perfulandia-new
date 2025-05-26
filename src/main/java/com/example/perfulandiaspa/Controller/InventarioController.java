package com.example.perfulandiaspa.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.perfulandiaspa.model.Inventario;
import com.example.perfulandiaspa.service.InventarioService;

@RestController
@RequestMapping("api/v1/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    //obtener todos los inventarios
    @GetMapping
    public ResponseEntity<List<Inventario>> listarInventarios() {
        try {
            List<Inventario> inventarios = inventarioService.findAll();
            return ResponseEntity.ok(inventarios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //obtener inventario por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getInventarioById(@PathVariable int id) {
        Inventario inventario = inventarioService.findById(id);
        if (inventario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Inventario con ID " + id + " no encontrado"));
        }
        return ResponseEntity.ok(inventario);
    }

    //crear nuevo inventario
    @PostMapping("/registro-inventario")
    public ResponseEntity<?> registroInventario(@RequestBody Inventario inventario) {
        try {
            Inventario nuevoInventario = inventarioService.registroInventario(inventario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInventario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    //borrar inventario por id
    @DeleteMapping("/eliminar-inventario/{id}")
    public ResponseEntity<?> eliminarInventario(@PathVariable int id) {
        try {
            inventarioService.eliminarInventario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } 
    }
    
    //actualizar inventario
    @PutMapping("{id}")
    public ResponseEntity<?> actualizarInventario(@PathVariable int id, @RequestBody Inventario inventario) {
        try {
            Inventario inventarioActualizado = inventarioService.actualizarInventario(id, inventario);
            return ResponseEntity.ok(inventarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }  
    }
}