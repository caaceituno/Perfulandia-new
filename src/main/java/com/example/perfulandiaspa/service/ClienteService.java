package com.example.perfulandiaspa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.perfulandiaspa.model.Cliente;
import com.example.perfulandiaspa.model.Rol;
import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.repository.ClienteRepository;
import com.example.perfulandiaspa.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

import com.example.perfulandiaspa.repository.RolRepository;

@Service
public class ClienteService {
    
    //trayendo dependencias necesarias
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;

    //recibiendo instancia de ClienteRepository como parámetro para asignar al atributo
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    //metodo para obtener todos los clientes
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    //metodo para obtener un cliente por ID
    public Optional<Cliente> findById(int id) {
        return clienteRepository.findById(id);
    }

    //registro usuario y cliente
    public Cliente crearCliente(Cliente cliente) {
        //asignando el rol de cliente
        Rol rolCliente = rolRepository.findByNombre("CLIENTE");
        cliente.getUsuario().setRol(rolCliente);
        cliente.getUsuario().setEnabled(true);

        //guardar el usuario
        Usuario nuevoUsuario = usuarioRepository.save(cliente.getUsuario());

        //asociar el usuario al cliente y guardar cliente
        cliente.setUsuario(nuevoUsuario);
        return clienteRepository.save(cliente);
    }
    
    //eliminar cliente y usuario asociado
    @Transactional//si falla una eliminacion se revierten todas para que la base de datos quede limpia
    public void eliminarCliente(int clienteId) {
        //buscar el cliente por su ID
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        
        //obtener el usuario asociado al cliente
        Usuario usuario = cliente.getUsuario();
        
        //primero eliminar el cliente para evitar problemas
        clienteRepository.deleteById(clienteId);
        
        //si existe un usuario asociado se elimina
        if (usuario != null) {
            usuarioRepository.deleteById(usuario.getId());
        }
    }

    @Transactional
    //editar cliente
    public Cliente actualizarCliente(int id, Cliente clienteActualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado"));

        //actualizando los campos del cliente
        if (clienteActualizado.getNombreCompleto() != null) {
            clienteExistente.setNombreCompleto(clienteActualizado.getNombreCompleto());
        }

        if (clienteActualizado.getRut() != null) {
            clienteExistente.setRut(clienteActualizado.getRut());
        }

        if (clienteActualizado.getTelefono() != null) {
            clienteExistente.setTelefono(clienteActualizado.getTelefono());
        }

        if (clienteActualizado.getDireccion() != null) {
            clienteExistente.setDireccion(clienteActualizado.getDireccion());
        }

        return clienteRepository.save(clienteExistente);
    }
}
