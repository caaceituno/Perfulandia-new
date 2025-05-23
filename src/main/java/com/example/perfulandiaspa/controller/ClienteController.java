package com.example.perfulandiaspa.controller;

import java.util.List;
import java.util.Map;

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

import com.example.perfulandiaspa.model.Cliente;
import com.example.perfulandiaspa.service.ClienteService;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        try {
            List<Cliente> clientes = clienteService.findAll();
            return ResponseEntity.ok(clientes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // crear un nuevo cliente
    @PostMapping("/registro-cliente")
    public ResponseEntity<?> registroCompleto(@RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.registrarUsuarioYCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // crear solo cliente (requiere usuario ya existente)
    @PostMapping("/registro-cliente-id")
    public ResponseEntity<?> registroCliente(@RequestBody Cliente cliente) {
        try {
            Cliente nuevoCliente = clienteService.registroCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    //eliminar un cliente y su usuario asociado por ID (DELETE)
    @DeleteMapping("/eliminar-cliente/{id}")
    public ResponseEntity<?> eliminarClienteYUsuario(@PathVariable int id) {
        try {
            clienteService.eliminarClienteYUsuario(id);
            return ResponseEntity.noContent().build(); // 204 si sale bien
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage())); //404 si no se encuentra el cliente
        } 
    }

    // editar un cliente por ID (PUT)
    @PutMapping("/editar-cliente/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable int id, @RequestBody Cliente cliente) {
        try {
            Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }  
    }
}
