package com.jzs.ms_registros_usuarios.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.jzs.ms_registros_usuarios.model.Usuario;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RegUsuRepositoryTest {
    @Autowired
    private RegUsuRepository regUsuRepository;

    @Test
    public void guardarUsuarioTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("usuario_prueba");
        usuario.setPassword("password_prueba");

        Usuario usuarioGuardado = regUsuRepository.save(usuario);

        assertNotNull(usuarioGuardado.getId());
        assertEquals("usuario_prueba", usuarioGuardado.getUsername());
        assertEquals("password_prueba", usuarioGuardado.getPassword());
    }

    @Test
    public void buscarUsuarioPorUsernameTest() {
        Usuario usuario = new Usuario();
        usuario.setUsername("usuario_prueba");
        usuario.setPassword("password_prueba");

        regUsuRepository.save(usuario);

        Usuario usuarioEncontrado = regUsuRepository.findByUsername("usuario_prueba");

        assertNotNull(usuarioEncontrado);
        assertEquals("usuario_prueba", usuarioEncontrado.getUsername());
        assertEquals("password_prueba", usuarioEncontrado.getPassword());
    }
}
