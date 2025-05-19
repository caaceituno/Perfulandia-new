package com.example.perfulandiaspa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.perfulandiaspa.repository.ClienteRepository;
import com.example.perfulandiaspa.model.Cliente;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
    this.clienteRepository = clienteRepository;
    }

    public List<Cliente> findAll() {
    return clienteRepository.findAll();
    }  

    // Registro cliente
    public Cliente registroCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
