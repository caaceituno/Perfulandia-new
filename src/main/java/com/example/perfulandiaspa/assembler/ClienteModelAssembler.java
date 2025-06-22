package com.example.perfulandiaspa.assembler;

import com.example.perfulandiaspa.controller.ClienteControllerV2;
import com.example.perfulandiaspa.model.Cliente;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<Cliente, EntityModel<Cliente>> {

    @Override
    public EntityModel<Cliente> toModel(Cliente cliente) {
        return EntityModel.of(cliente,
            linkTo(methodOn(ClienteControllerV2.class).obtenerClientePorId(cliente.getId())).withSelfRel(),
            linkTo(methodOn(ClienteControllerV2.class).listarClientes()).withRel("clientes")
        );
    }
}
