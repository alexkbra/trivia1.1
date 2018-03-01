package co.com.elpoli.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TipoPregunta entity.
 */
public class TipoPreguntaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 100)
    private String nombre;

    @NotNull
    @Size(min = 4, max = 100)
    private String constant;

    @NotNull
    @Size(min = 10, max = 100)
    private String descripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TipoPreguntaDTO tipoPreguntaDTO = (TipoPreguntaDTO) o;
        if(tipoPreguntaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoPreguntaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoPreguntaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", constant='" + getConstant() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
