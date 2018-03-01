package co.com.elpoli.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Expedicion entity.
 */
public class ExpedicionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 10, max = 100)
    private String nombre;

    private Set<PublicidadDTO> publicidads = new HashSet<>();

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

    public Set<PublicidadDTO> getPublicidads() {
        return publicidads;
    }

    public void setPublicidads(Set<PublicidadDTO> publicidads) {
        this.publicidads = publicidads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExpedicionDTO expedicionDTO = (ExpedicionDTO) o;
        if(expedicionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), expedicionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExpedicionDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
