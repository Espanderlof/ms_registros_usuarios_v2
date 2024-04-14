package com.jzs.ms_registros_usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jzs.ms_registros_usuarios.model.Usuario;

public interface RegUsuRepository extends JpaRepository<Usuario, Long>{
    Usuario findByUsername(String username);
}
