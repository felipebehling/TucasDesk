package com.example.Tucasdesk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.example.Tucasdesk.security.AuthorityUtils;

/**
 * Represents a user in the system.
 * This entity is mapped to the "usuarios" table in the database.
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    /**
     * The name of the user.
     */
    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    /**
     * The email address of the user. This is used for login and communication.
     */
    @NotBlank(message = "O email não pode estar em branco")
    private String email;

    /**
     * The password for the user's account.
     */
    @NotBlank(message = "A senha não pode estar em branco")
    private String senha;

    /**
     * The profile associated with the user, which defines their role and permissions.
     */
    @ManyToOne
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    /**
     * The timestamp when the user account was created.
     */
    private LocalDateTime dataCriacao;

    /**
     * A flag indicating whether the user account is active.
     */
    private Boolean ativo;

    /**
     * Gets the unique identifier for the user.
     * @return The unique identifier for the user.
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Sets the unique identifier for the user.
     * @param idUsuario The unique identifier for the user.
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Gets the name of the user.
     * @return The name of the user.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets the name of the user.
     * @param nome The name of the user.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Gets the email address of the user.
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     * @param email The email address of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password for the user's account.
     * @return The password for the user's account.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Sets the password for the user's account.
     * @param senha The password for the user's account.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Gets the profile associated with the user.
     * @return The profile associated with the user.
     */
    public Perfil getPerfil() {
        return perfil;
    }

    /**
     * Sets the profile associated with the user.
     * @param perfil The profile associated with the user.
     */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    /**
     * Gets the timestamp when the user account was created.
     * @return The timestamp when the user account was created.
     */
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    /**
     * Sets the timestamp when the user account was created.
     * @param dataCriacao The timestamp when the user account was created.
     */
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    /**
     * Gets a flag indicating whether the user account is active.
     * @return A flag indicating whether the user account is active.
     */
    public Boolean getAtivo() {
        return ativo;
    }

    /**
     * Sets a flag indicating whether the user account is active.
     * @param ativo A flag indicating whether the user account is active.
     */
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String profileName = this.perfil != null && StringUtils.hasText(this.perfil.getNome())
                ? this.perfil.getNome()
                : null;
        return List.of(new SimpleGrantedAuthority(AuthorityUtils.createRoleAuthority(profileName)));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.ativo;
    }
}
