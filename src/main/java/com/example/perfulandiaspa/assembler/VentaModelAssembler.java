package com.example.perfulandiaspa.assembler;

import com.example.perfulandiaspa.controller.VentaControllerV2;
import com.example.perfulandiaspa.model.Venta;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class VentaModelAssembler implements RepresentationModelAssembler<Venta, EntityModel<Venta>> {

    @Override
    public EntityModel<Venta> toModel(Venta venta) {
        return EntityModel.of(venta,
            linkTo(methodOn(VentaControllerV2.class).obtenerVentaPorId(venta.getId())).withSelfRel(),
            linkTo(methodOn(VentaControllerV2.class).listarVentas()).withRel("ventas")
        );
    }

}

