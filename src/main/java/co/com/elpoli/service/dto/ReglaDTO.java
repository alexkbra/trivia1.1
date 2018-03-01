package co.com.elpoli.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Regla entity.
 */
public class ReglaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 100)
    private String nombre;

    private Long cuestionarioId;

    private String cuestionarioNombre;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReglaDTO reglaDTO = (ReglaDTO) o;
        if(reglaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reglaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReglaDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
