package com.example.perfulandiaspa.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.perfulandiaspa.model.Vendedor;
import com.example.perfulandiaspa.service.VendedorService;

@RestController
@RequestMapping("api/v1/vendedores")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    // obtener todos los vendedores
    @GetMapping
    public ResponseEntity<List<Vendedor>> listarVendedores() {
        try {
            List<Vendedor> vendedores = vendedorService.findAll();
            return ResponseEntity.ok(vendedores);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // crear un nuevo vendedor
    @PostMapping("/registro-vendedor")
    public ResponseEntity<?> registroCompleto(@RequestBody Vendedor vendedor) {
        try {
            Vendedor nuevoVendedor = vendedorService.registrarUsuarioYVendedor(vendedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVendedor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // crear solo vendedor (requiere usuario ya existente)
    @PostMapping("/registro-vendedor-id")
    public ResponseEntity<?> registroVendedor(@RequestBody Vendedor vendedor) {
        try {
            Vendedor nuevoVendedor = vendedorService.registroVendedor(vendedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVendedor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // editar un usuario por ID (PUT)
    @PutMapping("/editar-vendedor/{id}")
    public ResponseEntity<?> actualizarVendedor(@PathVariable int id, @RequestBody Vendedor vendedor) {
        try {
            Vendedor vendedorActualizado = vendedorService.actualizarVendedor(id, vendedor);
            return ResponseEntity.ok(vendedorActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}