package com.example.perfulandiaspa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private int id;
    private String username;
    private String password;
    private String email;
    private boolean enabled;
    private int rolId;
}
