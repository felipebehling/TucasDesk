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
    
    /**
     * Gets the unique identifier for the interaction.
     * @return The unique identifier for the interaction.
     */
    public Integer getIdInteracao() {
        return idInteracao;
    }

    /**
     * Sets the unique identifier for the interaction.
     * @param idInteracao The unique identifier for the interaction.
     */
    public void setIdInteracao(Integer idInteracao) {
        this.idInteracao = idInteracao;
    }

    /**
     * Gets the ticket to which this interaction belongs.
     * @return The ticket to which this interaction belongs.
     */
    public Chamado getChamado() {
        return chamado;
    }

    /**
     * Sets the ticket to which this interaction belongs.
     * @param chamado The ticket to which this interaction belongs.
     */
    public void setChamado(Chamado chamado) {
        this.chamado = chamado;
    }

    /**
     * Gets the user who made the interaction.
     * @return The user who made the interaction.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Sets the user who made the interaction.
     * @param usuario The user who made the interaction.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Gets the content of the interaction message.
     * @return The content of the interaction message.
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Sets the content of the interaction message.
     * @param mensagem The content of the interaction message.
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * Gets the URL of an attachment, if any.
     * @return The URL of an attachment.
     */
    public String getAnexoUrl() {
        return anexoUrl;
    }

    /**
     * Sets the URL of an attachment.
     * @param anexoUrl The URL of an attachment.
     */
    public void setAnexoUrl(String anexoUrl) {
        this.anexoUrl = anexoUrl;
    }

    /**
     * Gets the timestamp when the interaction occurred.
     * @return The timestamp when the interaction occurred.
     */
    public LocalDateTime getDataInteracao() {
        return dataInteracao;
    }

    /**
     * Sets the timestamp when the interaction occurred.
     * @param dataInteracao The timestamp when the interaction occurred.
     */
    public void setDataInteracao(LocalDateTime dataInteracao) {
        this.dataInteracao = dataInteracao;
    }

    
}
