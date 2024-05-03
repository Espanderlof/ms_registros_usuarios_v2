package com.jzs.ms_registros_usuarios.service;

import com.jzs.ms_registros_usuarios.model.Usuario;
import com.jzs.ms_registros_usuarios.repository.RegUsuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegUsuServicelmpl implements RegUsuService {
    
    @Autowired
    private RegUsuRepository regUsuRepository;

    @Override
    public List<Usuario> getAllUsuarios() {
        return regUsuRepository.findAll();
    }

    @Override
    public Optional<Usuario> getUsuarioById(Long id) {
        return regUsuRepository.findById(id);
    }

    @Override
    public Usuario createUsuario(Usuario usuario) {
        return regUsuRepository.save(usuario);
    }

    // @Override
    // public Usuario updateUsuario(Long id, Usuario usuario) {
    //     if (regUsuRepository.existsById(id)) {
    //         usuario.setId(id);
    //         return regUsuRepository.save(usuario);
    //     }else{
    //         return null;
    //     }
    // }
    
    @Override
    public Usuario updateUsuario(Long id, Usuario usuario) {
        Optional<Usuario> usuarioExistente = regUsuRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario usuarioActualizado = usuarioExistente.get();
            usuarioActualizado.setUsername(usuario.getUsername());
            usuarioActualizado.setPassword(usuario.getPassword());
            return regUsuRepository.save(usuarioActualizado);
        } else {
            throw new IllegalArgumentException("Usuario no encontrado con el ID: " + id);
        }
    }

    @Override
    public void deleteUsuario(Long id) {
        regUsuRepository.deleteById(id);
    }

    @Override
    public Usuario findByUsername(String username) {
        return regUsuRepository.findByUsername(username);
    }
}
