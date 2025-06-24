package com.example.perfulandiaspa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.perfulandiaspa.model.Vendedor;
import com.example.perfulandiaspa.model.Rol;
import com.example.perfulandiaspa.model.Usuario;
import com.example.perfulandiaspa.repository.VendedorRepository;
import com.example.perfulandiaspa.repository.RolRepository;
import com.example.perfulandiaspa.repository.UsuarioRepository;

@Service
public class VendedorService {

    //trayendo dependencias necesarias
    @Autowired
    private VendedorRepository vendedorRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;

    //recibiendo instancia de VendedorRepository como parámetro para asignar al atributo
    public VendedorService(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    //metodo para obtener todos los clientes
    public List<Vendedor> findAll() {
        return vendedorRepository.findAll();
    }

    //metodo para obtener un vendedor por ID
    public Optional<Vendedor> findById(int id) {
        return vendedorRepository.findById(id);
    }

    //crear solo vendedor (se necesita poner manualmente el id para asociar)
    public Vendedor registroVendedor(Vendedor vendedor) {
        return vendedorRepository.save(vendedor);
    }

    //eliminar un vendedor y su usuario asociado
    @Transactional
    public void eliminarVendedor(int vendedorId) {
        //buscar el vendedor por su ID
        Vendedor vendedor = vendedorRepository.findById(vendedorId)
            .orElseThrow(() -> new RuntimeException("Vendedor no encontrado con ID: " + vendedorId));
        //obtener el usuario asociado al vendedor
        Usuario usuario = vendedor.getUsuario();
        //primero eliminar el vendedor para evitar problemas
        vendedorRepository.deleteById(vendedorId);
        //si existe un usuario asociado se elimina
        if (usuario != null) {
            usuarioRepository.deleteById(usuario.getId());
        }
    }

    //registro usuario y vendedor
    public Vendedor registrarUsuarioYVendedor(Vendedor vendedor) {
        //asignando el rol de vendedor
        Rol rolVendedor = rolRepository.findByNombre("VENDEDOR");
        vendedor.getUsuario().setRol(rolVendedor);
        vendedor.getUsuario().setEnabled(true);

        //guardar el usuario
        Usuario nuevoUsuario = usuarioRepository.save(vendedor.getUsuario());

        //asociar el usuario al vendedor y guardar vendedor
        vendedor.setUsuario(nuevoUsuario);
        return vendedorRepository.save(vendedor);
    }

    //editar vendedor
    public Vendedor actualizarVendedor(int id, Vendedor vendedorActualizado) {
        Vendedor vendedorExistente = vendedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vendedor con ID " + id + " no encontrado"));

        if (vendedorActualizado.getNombreCompleto() != null) {
            vendedorExistente.setNombreCompleto(vendedorActualizado.getNombreCompleto());
        }

        if (vendedorActualizado.getSucursal() != null) {
            vendedorExistente.setSucursal(vendedorActualizado.getSucursal());
        }

        if (vendedorActualizado.getMetaMensual() > 0) {
            vendedorExistente.setMetaMensual(vendedorActualizado.getMetaMensual());
        }

        return vendedorRepository.save(vendedorExistente);
    }
}
