package com.example.perfulandiaspa.controller;

import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.service.UsuarioService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.findAll();
            return ResponseEntity.ok(usuarios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // crear un nuevo usuario (POST)
    @PostMapping("/registro-usuario")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    // eliminar un usuario por ID (DELETE)
    @DeleteMapping("/eliminar-usuario/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable int id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build(); //204 si sale bien
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage())); //404 si no se encuentra el usuario
        }
    }

    // encontrar usuario por username
    @PostMapping("/encontrar-usuario")
    public ResponseEntity<?> buscarPorUsername(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        try {
            Usuario usuario = usuarioService.encontrarPorUsername(username);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // editar un usuario por ID (PUT)
    @PutMapping("/editar-usuario/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable int id, @RequestBody Usuario usuario) {
        try {
            Usuario userActualizado = usuarioService.actualizarUsuario(id, usuario);
            return ResponseEntity.ok(userActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }

    }

    //email
    @PatchMapping("/cambio-email")
    public ResponseEntity<?> cambiarEmail(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String emailActual = body.get("emailActual");
        String nuevoEmail = body.get("nuevoEmail");

        try {
            Usuario actualizado = usuarioService.actualizarEmail(username, emailActual, nuevoEmail);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    //contraseña
    @PatchMapping("/cambio-pass")
    public ResponseEntity<?> cambiarPass(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String passActual = body.get("passActual");
        String nuevaPass = body.get("nuevaPass");

        try {
            Usuario actualizado = usuarioService.actualizarPass(username, passActual, nuevaPass);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/cambio-rol")
    public ResponseEntity<?> cambiarRol(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        int nuevoRolId = (int) body.get("nuevoRol");

        try {
            Usuario actualizado = usuarioService.actualizarRol(username, nuevoRolId);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}

