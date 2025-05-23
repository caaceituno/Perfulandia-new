package com.example.perfulandiaspa.service;

import java.util.List;

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

    //crear solo cliente (se necesita poner manualmente el id para asociar)
    public Cliente registroCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    //registro usuario y cliente
    public Cliente registrarUsuarioYCliente(Cliente cliente) {
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

    //eliminar cliente
    public void eliminarCliente(int id) {
        clienteRepository.deleteById(id);
    }
    
    //eliminar cliente y usuario asociado
    @Transactional//si falla una eliminacion se revierten todas para que la base de datos quede limpia y consistente.
    public void eliminarClienteYUsuario(int clienteId) {
        //buscar el cliente por su ID
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + clienteId));
        
        //obtener el usuario asociado al cliente
        Usuario usuario = cliente.getUsuario();
        
        //primero eliminar el cliente para evitar problemas de integridad referencial
        clienteRepository.deleteById(clienteId);
        
        //si existe un usuario asociado, eliminarlo
        if (usuario != null) {
            usuarioRepository.deleteById(usuario.getId());
        }
    }

    //editar cliente
    public Cliente actualizarCliente(int id, Cliente clienteActualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente con ID " + id + " no encontrado"));

        clienteExistente.setNombreCompleto(clienteActualizado.getNombreCompleto());
        clienteExistente.setRut(clienteActualizado.getRut());
        clienteExistente.setTelefono(clienteActualizado.getTelefono());
        clienteExistente.setDireccion(clienteActualizado.getDireccion());

        return clienteRepository.save(clienteExistente);
    }
}
