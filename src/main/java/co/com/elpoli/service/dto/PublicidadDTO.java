package co.com.elpoli.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Publicidad entity.
 */
public class PublicidadDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 100)
    private String titulo;

    @NotNull
    @Size(min = 10, max = 100)
    private String descripcion;

    @NotNull
    @Size(min = 10, max = 100)
    private String premiodescripcion;

    private Long empresasId;

    private String empresasRazonSocial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPremiodescripcion() {
        return premiodescripcion;
    }

    public void setPremiodescripcion(String premiodescripcion) {
        this.premiodescripcion = premiodescripcion;
    }

    public Long getEmpresasId() {
        return empresasId;
    }

    public void setEmpresasId(Long empresasId) {
        this.empresasId = empresasId;
    }

    public String getEmpresasRazonSocial() {
        return empresasRazonSocial;
    }

    public void setEmpresasRazonSocial(String empresasRazonSocial) {
        this.empresasRazonSocial = empresasRazonSocial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PublicidadDTO publicidadDTO = (PublicidadDTO) o;
        if(publicidadDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), publicidadDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PublicidadDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", premiodescripcion='" + getPremiodescripcion() + "'" +
            "}";
    }
}
