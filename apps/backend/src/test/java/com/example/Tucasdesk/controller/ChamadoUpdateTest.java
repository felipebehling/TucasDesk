package com.example.Tucasdesk.controller;

import com.example.Tucasdesk.config.ApiExceptionHandler;
import com.example.Tucasdesk.config.SecurityConfig;
import com.example.Tucasdesk.security.CustomJwtAuthenticationConverter;
import com.example.Tucasdesk.service.ChamadoService;
import com.example.Tucasdesk.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChamadoController.class, properties = {
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=https://test.issuer.com"
})
@Import({SecurityConfig.class, ApiExceptionHandler.class, CustomJwtAuthenticationConverter.class})
class ChamadoUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChamadoService chamadoService;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void shouldReturnBadRequestWhenTitleIsEmpty() throws Exception {
        String requestBody = "{\"titulo\": \"\"}";

        mockMvc.perform(put("/chamados/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(jwt().authorities(new SimpleGrantedAuthority("TECNICO"))))
                .andExpect(status().isBadRequest());
    }
}
