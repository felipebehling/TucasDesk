package com.example.Tucasdesk.dtos;

/**
 * DTO that represents a request to create a new {@code Interacao} for a {@code Chamado}.
 */
public class InteracaoRequest {

    private Integer usuarioId;
    private String mensagem;
    private String anexoUrl;

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
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
}
