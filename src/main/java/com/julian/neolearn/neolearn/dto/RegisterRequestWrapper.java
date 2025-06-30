package com.julian.neolearn.neolearn.dto;

import com.julian.neolearn.neolearn.auth.RegisterRequest;

// Crear esta clase
public class RegisterRequestWrapper {
    private RegisterRequest user;
    private EmpresaDTO empresa;
    
    // Constructors
    public RegisterRequestWrapper() {}
    
    public RegisterRequestWrapper(RegisterRequest user, EmpresaDTO empresa) {
        this.user = user;
        this.empresa = empresa;
    }
    
    // Getters y Setters
    public RegisterRequest getUser() {
        return user;
    }
    
    public void setUser(RegisterRequest user) {
        this.user = user;
    }
    
    public EmpresaDTO getEmpresa() {
        return empresa;
    }
    
    public void setEmpresa(EmpresaDTO empresa) {
        this.empresa = empresa;
    }
}
