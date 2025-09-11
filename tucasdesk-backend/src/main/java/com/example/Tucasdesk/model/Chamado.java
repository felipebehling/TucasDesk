package com.example.Tucasdesk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a support ticket in the system.
 * This entity is mapped to the "chamados" table in the database.
 */
@Entity
@Table(name = "chamados")
public class Chamado {
    /**
     * The unique identifier for the ticket.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idChamado;

    /**
     * The title of the ticket.
     */
    private String titulo;

    /**
     * A detailed description of the issue or request.
     */
    @Column(columnDefinition = "TEXT")
    private String descricao;

    /**
     * The category of the ticket (e.g., Hardware, Software).
     */
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    /**
     * The current status of the ticket (e.g., Open, In Progress, Closed).
     */
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    /**
     * The priority level of the ticket (e.g., Low, Medium, High).
     */
    @ManyToOne
    @JoinColumn(name = "prioridade")
    private Prioridade prioridade;

    /**
     * The user who created the ticket.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    /**
     * The technician assigned to the ticket.
     */
    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Usuario tecnico;

    /**
     * The timestamp when the ticket was opened.
     */
    private LocalDateTime dataAbertura;

    /**
     * The timestamp when the ticket was closed. Can be null if the ticket is not yet closed.
     */
    private LocalDateTime dataFechamento;

    //getters e setters
    public Integer getIdChamado() {
        return idChamado;
    }

    public void setIdChamado(Integer idChamado) {
        this.idChamado = idChamado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getTecnico() {
        return tecnico;
    }

    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    
}
