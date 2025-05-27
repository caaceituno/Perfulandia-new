package com.example.perfulandiaspa.service;

import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional

public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> findAll() {
    return usuarioRepository.findAll();
    }

    //metodo para obtener un usuario por ID
    public Optional<Usuario> findById(int id) {
        return usuarioRepository.findById(id);
    }

    //crear un nuevo usuario
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    //eliminar un usuario por ID
    public void eliminarUsuario(int id) {
        usuarioRepository.deleteById(id);
    }

    //actualizar un usuario por ID
    public Usuario actualizarUsuario(int id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario " + id + " no encontrado"));

                if (usuarioActualizado.getUsername() != null) {
            usuarioExistente.setUsername(usuarioActualizado.getUsername());
        }

        if (usuarioActualizado.getPassword() != null) {
            usuarioExistente.setPassword(usuarioActualizado.getPassword());
        }

        if (usuarioActualizado.getEmail() != null) {
            usuarioExistente.setEmail(usuarioActualizado.getEmail());
        }

        usuarioExistente.setEnabled(usuarioActualizado.isEnabled());

        return usuarioRepository.save(usuarioExistente);
    }
}

