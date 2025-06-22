package com.example.perfulandiaspa.assembler;

import com.example.perfulandiaspa.controller.InventarioController;
import com.example.perfulandiaspa.model.Inventario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InventarioModelAssembler implements RepresentationModelAssembler<Inventario, EntityModel<Inventario>> {
    @Override
    public EntityModel<Inventario> toModel(Inventario inventario) {
        return EntityModel.of(inventario,
            linkTo(methodOn(InventarioController.class).getInventarioById(inventario.getId())).withSelfRel(),
            linkTo(methodOn(InventarioController.class).listarInventarios()).withRel("inventarios")
        );
    }
}
