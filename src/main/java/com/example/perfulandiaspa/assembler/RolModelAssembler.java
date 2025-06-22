package com.example.perfulandiaspa.assembler;

import com.example.perfulandiaspa.controller.RolControllerV2;
import com.example.perfulandiaspa.model.Rol;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RolModelAssembler implements RepresentationModelAssembler<Rol, EntityModel<Rol>> {

    @Override
    public EntityModel<Rol> toModel(Rol rol) {
        return EntityModel.of(rol,
            linkTo(methodOn(RolControllerV2.class).listarRoles()).withSelfRel()
        );
    }
}
