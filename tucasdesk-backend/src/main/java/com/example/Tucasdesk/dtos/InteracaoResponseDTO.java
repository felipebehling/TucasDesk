package com.example.Tucasdesk.dtos;

import java.time.LocalDateTime;

/**
 * DTO returned when a new {@code Interacao} is created or retrieved.
 */
public class InteracaoResponseDTO {

    private Integer id;
    private String mensagem;
    private String anexoUrl;
    private LocalDateTime dataInteracao;
    private UsuarioResumoDTO usuario;

    public InteracaoResponseDTO() {
    }

    public InteracaoResponseDTO(Integer id, String mensagem, String anexoUrl, LocalDateTime dataInteracao, UsuarioResumoDTO usuario) {
        this.id = id;
        this.mensagem = mensagem;
        this.anexoUrl = anexoUrl;
        this.dataInteracao = dataInteracao;
        this.usuario = usuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getAnexoUrl() {
        return anexoUrl;
    }

    public void setAnexoUrl(String anexoUrl) {
        this.anexoUrl = anexoUrl;
    }

    public LocalDateTime getDataInteracao() {
        return dataInteracao;
    }

    public void setDataInteracao(LocalDateTime dataInteracao) {
        this.dataInteracao = dataInteracao;
    }

    public UsuarioResumoDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResumoDTO usuario) {
        this.usuario = usuario;
    }
}
