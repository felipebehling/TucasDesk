package com.example.Tucasdesk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents an interaction or comment on a support ticket.
 * This entity is mapped to the "interacoes" table in the database.
 */
@Entity
@Table(name = "interacoes")
public class Interacao {
    /**
     * The unique identifier for the interaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInteracao;

    /**
     * The ticket to which this interaction belongs.
     */
    @ManyToOne
    @JoinColumn(name = "chamado_id")
    private Chamado chamado;

    /**
     * The user who made the interaction.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    /**
     * The content of the interaction message.
     */
    @Column(columnDefinition = "TEXT")
    private String mensagem;

    /**
     * The URL of an attachment, if any.
     */
    private String anexoUrl;

    /**
     * The timestamp when the interaction occurred.
     */
    private LocalDateTime dataInteracao;
    
    // getters e setters
    public Integer getIdInteracao() {
        return idInteracao;
    }

    public void setIdInteracao(Integer idInteracao) {
        this.idInteracao = idInteracao;
    }

    public Chamado getChamado() {
        return chamado;
    }

    public void setChamado(Chamado chamado) {
        this.chamado = chamado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    
}
