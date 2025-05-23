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
import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.service.ClienteService;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
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
        //Cliente nuevoCliente = clienteService.registrarUsuarioYCliente(cliente);
        //return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
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

    /*// eliminar un cliente por ID (DELETE)
    @DeleteMapping("/eliminar-cliente-only/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable int id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build(); //204 si sale bien
    }*/

    // eliminar un cliente y su usuario asociado por ID (DELETE)
    @DeleteMapping("/eliminar-cliente/{id}")
    public ResponseEntity<Void> eliminarClienteYUsuario(@PathVariable int id) {
        clienteService.eliminarClienteYUsuario(id);
        return ResponseEntity.noContent().build(); // 204 si sale bien
    }

    // editar un usuario por ID (PUT)
    @PutMapping("/editar-cliente/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable int id, @RequestBody Cliente cliente) {
        Cliente clienteActualizado = clienteService.actualizarCliente(id, cliente);
        return ResponseEntity.ok(clienteActualizado);
    }
}
