package com.jzs.ms_registros_usuarios.service;

import com.jzs.ms_registros_usuarios.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface RegUsuService {
    List<Usuario> getAllUsuarios();
    Optional<Usuario> getUsuarioById(Long id);
    Usuario createUsuario(Usuario usuario);
    Usuario updateUsuario(Long id, Usuario usuario);
    void deleteUsuario(Long id);
    Usuario findByUsername(String username);
}
