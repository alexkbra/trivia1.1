package co.com.elpoli.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Pista entity.
 */
public class PistaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 10, max = 200)
    private String cortaDescripcion;

    @NotNull
    @Size(min = 10, max = 500)
    private String descripcion;

    private Long preguntaId;

    private String preguntaCortaDescripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCortaDescripcion() {
        return cortaDescripcion;
    }

    public void setCortaDescripcion(String cortaDescripcion) {
        this.cortaDescripcion = cortaDescripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(Long preguntaId) {
        this.preguntaId = preguntaId;
    }

    public String getPreguntaCortaDescripcion() {
        return preguntaCortaDescripcion;
    }

    public void setPreguntaCortaDescripcion(String preguntaCortaDescripcion) {
        this.preguntaCortaDescripcion = preguntaCortaDescripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PistaDTO pistaDTO = (PistaDTO) o;
        if(pistaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pistaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PistaDTO{" +
            "id=" + getId() +
            ", cortaDescripcion='" + getCortaDescripcion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
