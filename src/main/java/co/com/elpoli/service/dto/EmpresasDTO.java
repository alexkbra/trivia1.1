package co.com.elpoli.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Empresas entity.
 */
public class EmpresasDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 100)
    private String nombre;

    @NotNull
    @Size(min = 4, max = 100)
    private String razonSocial;

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

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmpresasDTO empresasDTO = (EmpresasDTO) o;
        if(empresasDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), empresasDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmpresasDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", razonSocial='" + getRazonSocial() + "'" +
            "}";
    }
}
