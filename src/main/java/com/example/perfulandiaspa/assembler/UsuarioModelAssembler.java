package com.example.perfulandiaspa.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import com.example.perfulandiaspa.controller.UsuarioControllerV2;
import com.example.perfulandiaspa.model.Usuario;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioControllerV2.class).obtenerUsuarioPorId(usuario.getId())).withSelfRel(),
            linkTo(methodOn(UsuarioControllerV2.class).listarUsuarios()).withRel("usuarios"),
            linkTo(methodOn(UsuarioControllerV2.class).crearUsuario(null)).withRel("crear"),
            linkTo(methodOn(UsuarioControllerV2.class).actualizarUsuario(usuario.getId(), usuario)).withRel("actualizar"),
            linkTo(methodOn(UsuarioControllerV2.class).eliminarUsuario(usuario.getId())).withRel("eliminar")
        );
    }
}
