package com.example.Tucasdesk.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Represents a status change event registered for a {@link Chamado}.
 */
@Entity
@Table(name = "historico_status")
public class HistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorico;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "chamado_id")
    private Chamado chamado;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;

    public Integer getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(Integer idHistorico) {
        this.idHistorico = idHistorico;
    }

    public Chamado getChamado() {
        return chamado;
    }

    public void setChamado(Chamado chamado) {
        this.chamado = chamado;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
