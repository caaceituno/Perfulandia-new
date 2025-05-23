package com.example.perfulandiaspa.controller;

import java.util.List;

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

import com.example.perfulandiaspa.model.Cliente;
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
        List<Vendedor> vendedores = vendedorService.findAll();
        return ResponseEntity.ok(vendedores);
    }

    // crear un nuevo vendedor
    @PostMapping("/registro-vendedor")
    public ResponseEntity<Vendedor> registroCompleto(@RequestBody Vendedor vendedor) {
        Vendedor nuevoVendedor = vendedorService.registrarUsuarioYVendedor(vendedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVendedor);
    }

    // crear solo vendedor (requiere usuario ya existente)
    @PostMapping("/registro-vendedor-id")
    public ResponseEntity<Vendedor> registroVendedor(@RequestBody Vendedor vendedor) {
        Vendedor nuevoVendedor = vendedorService.registroVendedor(vendedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVendedor);
    }

    // editar un usuario por ID (PUT)
    @PutMapping("/editar-vendedor/{id}")
    public ResponseEntity<Vendedor> actualizarVendedor(@PathVariable int id, @RequestBody Vendedor vendedor) {
        Vendedor vendedorActualizado = vendedorService.actualizarVendedor(id, vendedor);
        return ResponseEntity.ok(vendedorActualizado);
    }
}