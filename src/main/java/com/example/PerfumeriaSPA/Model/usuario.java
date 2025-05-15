package com.example.PerfumeriaSPA.Model;


import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class usuario {
    private BigInteger id;
    private String usermane;
    private String password;
    private String email;
    
    
}
