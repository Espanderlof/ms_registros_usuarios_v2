package com.jzs.ms_registros_usuarios.controller;

import com.jzs.ms_registros_usuarios.model.Usuario;
import com.jzs.ms_registros_usuarios.service.RegUsuService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(RegUsuController.class)
public class RegUsuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegUsuService regUsuServiceMock;

    @Test
    public void getAllUsuariosTest() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setUsername("usuario1");
        usuario1.setPassword("password1");
    
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("usuario2");
        usuario2.setPassword("password2");
    
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);
    
        when(regUsuServiceMock.getAllUsuarios()).thenReturn(usuarios);
    
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.usuarioList", Matchers.hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.usuarioList[0].username", Matchers.is("usuario1")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.usuarioList[1].username", Matchers.is("usuario2")))
            .andExpect(MockMvcResultMatchers.jsonPath("$._links.usuarios.href", Matchers.endsWith("/usuarios")));
    }

    @Test
    public void autenticarUsuarioTest() throws Exception {
        String username = "usuario";
        String password = "password";

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername(username);
        usuario.setPassword(password);

        when(regUsuServiceMock.findByUsername(username)).thenReturn(usuario);

        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/usuarios/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.respuesta", Matchers.is(true)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Autenticación exitosa.")));
    }

    @Test
    public void getUsuarioByIdTest() throws Exception {
        Long usuarioId = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setUsername("usuario1");
        usuario.setPassword("password1");
    
        when(regUsuServiceMock.getUsuarioById(usuarioId)).thenReturn(Optional.of(usuario));
    
        mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/{id}", usuarioId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("usuario1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", Matchers.endsWith("/usuarios/" + usuarioId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.all-usuarios.href", Matchers.endsWith("/usuarios")));
    }

    @Test
    public void createUsuarioTest() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setUsername("newUser");
        usuario.setPassword("newPassword");
    
        Usuario usuarioCreado = new Usuario();
        usuarioCreado.setId(3L);
        usuarioCreado.setUsername("newUser");
        usuarioCreado.setPassword("newPassword");
    
        when(regUsuServiceMock.createUsuario(usuario)).thenReturn(usuarioCreado);
    
        mockMvc.perform(MockMvcRequestBuilders.post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"newUser\",\"password\":\"newPassword\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("newUser")))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href", Matchers.endsWith("/usuarios/3")))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.all-usuarios.href", Matchers.endsWith("/usuarios")));
    }

}
