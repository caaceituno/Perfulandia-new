package com.example.perfulandiaspa.assembler;

import com.example.perfulandiaspa.controller.DetalleVentaControllerV2;
import com.example.perfulandiaspa.model.DetalleVenta;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class DetalleVentaModelAssembler implements RepresentationModelAssembler<DetalleVenta, EntityModel<DetalleVenta>> {
    @Override
    public EntityModel<DetalleVenta> toModel(DetalleVenta detalleVenta) {
        return EntityModel.of(detalleVenta,
            linkTo(methodOn(DetalleVentaControllerV2.class).obtenerDetalleVentaPorId(detalleVenta.getId())).withSelfRel(),
            linkTo(methodOn(DetalleVentaControllerV2.class).listarDetalleVentas()).withRel("detalle-ventas")
        );
    }
}
