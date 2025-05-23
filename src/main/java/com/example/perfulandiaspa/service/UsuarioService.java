package com.example.perfulandiaspa.service;

import com.example.perfulandiaspa.model.Rol;
import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.repository.RolRepository;
import com.example.perfulandiaspa.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional

public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> findAll() {
    return usuarioRepository.findAll();
    }   

    // Crear un nuevo usuario
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Eliminar un usuario por ID
    public void eliminarUsuario(int id) {
        usuarioRepository.deleteById(id);
    }

    // Actualizar un usuario por ID
    public Usuario actualizarUsuario(int id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario " + id + " no encontrado"));

        usuarioExistente.setUsername(usuarioActualizado.getUsername());
        usuarioExistente.setPassword(usuarioActualizado.getPassword());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setEnabled(usuarioActualizado.isEnabled());

        return usuarioRepository.save(usuarioExistente);
    }

    // Actualizar email de un usuario
    public Usuario actualizarEmail(String username, String emailActual, String nuevoEmail) {
        Usuario usuario = usuarioRepository.findByUsernameAndEmail(username, emailActual);
        if (usuario == null) {
            throw new RuntimeException("Email o usuario no encontrado");
        }

        usuario.setEmail(nuevoEmail);
        return usuarioRepository.save(usuario);
    }

    // Actualizar contraseña de un usuario
    public Usuario actualizarPass(String username, String passActual, String nuevaPass) {
        Usuario usuario = usuarioRepository.findByPasswordAndUsername(passActual, username);
        if (usuario == null) {
            throw new RuntimeException("Contraseña actual incorrecta o usuario no encontrado");
        }

        usuario.setPassword(nuevaPass);
        return usuarioRepository.save(usuario);
    }

    // Verificar si el usuario existe por username
    public Usuario encontrarPorUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        return usuario;
    }

    // Actualizar rol de un usuario
    public Usuario actualizarRol(String username, int nuevoRolId) {
        Usuario usuario = encontrarPorUsername(username);
        Rol nuevoRol = rolRepository.findById(nuevoRolId)
            .orElseThrow(() -> new RuntimeException("Rol con ID " + nuevoRolId + " no encontrado"));

        usuario.setRol(nuevoRol);
        return usuarioRepository.save(usuario);
    }
}

