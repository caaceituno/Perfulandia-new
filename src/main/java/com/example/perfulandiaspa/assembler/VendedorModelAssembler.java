package com.example.perfulandiaspa.assembler;

import com.example.perfulandiaspa.controller.VendedorControllerV2;
import com.example.perfulandiaspa.model.Vendedor;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class VendedorModelAssembler implements RepresentationModelAssembler<Vendedor, EntityModel<Vendedor>> {

    @Override
    public EntityModel<Vendedor> toModel(Vendedor vendedor) {
        return EntityModel.of(vendedor,
            linkTo(methodOn(VendedorControllerV2.class).obtenerVendedorPorId(vendedor.getId())).withSelfRel(),
            linkTo(methodOn(VendedorControllerV2.class).listarVendedores()).withRel("vendedores")
        );
    }
}
