package com.example.perfulandiaspa.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.perfulandiaspa.model.Vendedor;
import com.example.perfulandiaspa.service.VendedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("api/v1/vendedores")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    // obtener todos los vendedores
    @Operation(summary = "Listar todos los vendedores")
    @GetMapping
    public ResponseEntity<List<Vendedor>> listarVendedores() {
        try {
            List<Vendedor> vendedores = vendedorService.findAll();
            return ResponseEntity.ok(vendedores);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //obtener un vendedor por ID
    @Operation(summary = "Obtener un vendedor por ID")
    @ApiResponse(responseCode = "200", description = "Vendedor encontrado")
    @ApiResponse(responseCode = "404", description = "Vendedor no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVendedorPorId(@PathVariable int id) {
        try {
            Optional<Vendedor> vendedor = vendedorService.findById(id);
            if (vendedor.isPresent()) {
                return ResponseEntity.ok(vendedor.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vendedor no encontrado");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //crear un nuevo vendedor
    @Operation(summary = "Registrar un nuevo vendedor")
    @ApiResponse(responseCode = "201", description = "Vendedor registrado exitosamente")
    @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    @PostMapping
    public ResponseEntity<?> registroCompleto(@RequestBody Vendedor vendedor) {
        try {
            Vendedor nuevoVendedor = vendedorService.registrarUsuarioYVendedor(vendedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVendedor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    //eliminar un vendedor por ID
    @Operation(summary = "Eliminar un vendedor por ID")
    @ApiResponse(responseCode = "204", description = "Vendedor eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Vendedor no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVendedor(@PathVariable int id) {
        try {
            vendedorService.eliminarVendedor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } 
    }

    //editar un usuario por ID (PUT)
    @Operation(summary = "Actualizar un vendedor por ID")
    @ApiResponse(responseCode = "200", description = "Vendedor actualizado exitosamente")
    @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVendedor(@PathVariable int id, @RequestBody Vendedor vendedor) {
        try {
            Vendedor vendedorActualizado = vendedorService.actualizarVendedor(id, vendedor);
            return ResponseEntity.ok(vendedorActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}