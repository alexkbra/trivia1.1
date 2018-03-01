package co.com.elpoli.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import co.com.elpoli.domain.enumeration.TipoArchivo;

/**
 * A DTO for the Galerias entity.
 */
public class GaleriasDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 500)
    private String uri;

    @NotNull
    private TipoArchivo tipoArchivo;

    private Long publicidadId;

    private String publicidadTitulo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public TipoArchivo getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(TipoArchivo tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public Long getPublicidadId() {
        return publicidadId;
    }

    public void setPublicidadId(Long publicidadId) {
        this.publicidadId = publicidadId;
    }

    public String getPublicidadTitulo() {
        return publicidadTitulo;
    }

    public void setPublicidadTitulo(String publicidadTitulo) {
        this.publicidadTitulo = publicidadTitulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GaleriasDTO galeriasDTO = (GaleriasDTO) o;
        if(galeriasDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), galeriasDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GaleriasDTO{" +
            "id=" + getId() +
            ", uri='" + getUri() + "'" +
            ", tipoArchivo='" + getTipoArchivo() + "'" +
            "}";
    }
}
