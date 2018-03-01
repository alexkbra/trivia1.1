package co.com.elpoli.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Pregunta entity.
 */
public class PreguntaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 10, max = 200)
    private String cortaDescripcion;

    @NotNull
    @Size(min = 10, max = 500)
    private String descripcion;

    private Long cuestionarioId;

    private String cuestionarioNombre;

    private Long tipoPreguntaId;

    private String tipoPreguntaNombre;

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

    public Long getCuestionarioId() {
        return cuestionarioId;
    }

    public void setCuestionarioId(Long cuestionarioId) {
        this.cuestionarioId = cuestionarioId;
    }

    public String getCuestionarioNombre() {
        return cuestionarioNombre;
    }

    public void setCuestionarioNombre(String cuestionarioNombre) {
        this.cuestionarioNombre = cuestionarioNombre;
    }

    public Long getTipoPreguntaId() {
        return tipoPreguntaId;
    }

    public void setTipoPreguntaId(Long tipoPreguntaId) {
        this.tipoPreguntaId = tipoPreguntaId;
    }

    public String getTipoPreguntaNombre() {
        return tipoPreguntaNombre;
    }

    public void setTipoPreguntaNombre(String tipoPreguntaNombre) {
        this.tipoPreguntaNombre = tipoPreguntaNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PreguntaDTO preguntaDTO = (PreguntaDTO) o;
        if(preguntaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preguntaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PreguntaDTO{" +
            "id=" + getId() +
            ", cortaDescripcion='" + getCortaDescripcion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
