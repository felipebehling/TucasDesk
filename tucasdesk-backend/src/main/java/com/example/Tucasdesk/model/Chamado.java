package com.example.Tucasdesk.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * The list of interactions registered for this ticket.
     */
    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interacao> interacoes = new ArrayList<>();

    /**
     * The chronological status history of this ticket.
     */
    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dataRegistro ASC")
    private List<HistoricoStatus> historicoStatus = new ArrayList<>();

    /**
     * Gets the unique identifier for the ticket.
     * @return The unique identifier for the ticket.
     */
    public Integer getIdChamado() {
        return idChamado;
    }

    /**
     * Sets the unique identifier for the ticket.
     * @param idChamado The unique identifier for the ticket.
     */
    public void setIdChamado(Integer idChamado) {
        this.idChamado = idChamado;
    }

    /**
     * Gets the title of the ticket.
     * @return The title of the ticket.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Sets the title of the ticket.
     * @param titulo The title of the ticket.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Gets a detailed description of the issue or request.
     * @return A detailed description of the issue or request.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Sets a detailed description of the issue or request.
     * @param descricao A detailed description of the issue or request.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Gets the category of the ticket.
     * @return The category of the ticket.
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Sets the category of the ticket.
     * @param categoria The category of the ticket.
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Gets the current status of the ticket.
     * @return The current status of the ticket.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the current status of the ticket.
     * @param status The current status of the ticket..
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Gets the priority level of the ticket.
     * @return The priority level of the ticket.
     */
    public Prioridade getPrioridade() {
        return prioridade;
    }

    /**
     * Sets the priority level of the ticket.
     * @param prioridade The priority level of the ticket.
     */
    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    /**
     * Gets the user who created the ticket.
     * @return The user who created the ticket.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Sets the user who created the ticket.
     * @param usuario The user who created the ticket.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Gets the technician assigned to the ticket.
     * @return The technician assigned to the ticket.
     */
    public Usuario getTecnico() {
        return tecnico;
    }

    /**
     * Sets the technician assigned to the ticket.
     * @param tecnico The technician assigned to the ticket.
     */
    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }

    /**
     * Gets the timestamp when the ticket was opened.
     * @return The timestamp when the ticket was opened.
     */
    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    /**
     * Sets the timestamp when the ticket was opened.
     * @param dataAbertura The timestamp when the ticket was opened.
     */
    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    /**
     * Gets the timestamp when the ticket was closed.
     * @return The timestamp when the ticket was closed.
     */
    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    /**
     * Sets the timestamp when the ticket was closed.
     * @param dataFechamento The timestamp when the ticket was closed.
     */
    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public List<Interacao> getInteracoes() {
        return interacoes;
    }

    public void setInteracoes(List<Interacao> interacoes) {
        this.interacoes = interacoes;
    }

    public List<HistoricoStatus> getHistoricoStatus() {
        return historicoStatus;
    }

    public void setHistoricoStatus(List<HistoricoStatus> historicoStatus) {
        this.historicoStatus = historicoStatus;
    }


}
