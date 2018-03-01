package co.com.elpoli.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Cuestionario entity.
 */
public class CuestionarioDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 100)
    private String nombre;

    private Set<NivelDTO> nivels = new HashSet<>();

    private Long expedicionId;

    private String expedicionNombre;

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

    public Set<NivelDTO> getNivels() {
        return nivels;
    }

    public void setNivels(Set<NivelDTO> nivels) {
        this.nivels = nivels;
    }

    public Long getExpedicionId() {
        return expedicionId;
    }

    public void setExpedicionId(Long expedicionId) {
        this.expedicionId = expedicionId;
    }

    public String getExpedicionNombre() {
        return expedicionNombre;
    }

    public void setExpedicionNombre(String expedicionNombre) {
        this.expedicionNombre = expedicionNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CuestionarioDTO cuestionarioDTO = (CuestionarioDTO) o;
        if(cuestionarioDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cuestionarioDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CuestionarioDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
