package co.com.elpoli.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Empresas.
 */
@Entity
@Table(name = "empresas")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "empresas")
public class Empresas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @NotNull
    @Size(min = 4, max = 100)
    @Column(name = "razon_social", length = 100, nullable = false)
    private String razonSocial;

    @OneToMany(mappedBy = "empresas")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Publicidad> publicidads = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Empresas nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public Empresas razonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
        return this;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Set<Publicidad> getPublicidads() {
        return publicidads;
    }

    public Empresas publicidads(Set<Publicidad> publicidads) {
        this.publicidads = publicidads;
        return this;
    }

    public Empresas addPublicidad(Publicidad publicidad) {
        this.publicidads.add(publicidad);
        publicidad.setEmpresas(this);
        return this;
    }

    public Empresas removePublicidad(Publicidad publicidad) {
        this.publicidads.remove(publicidad);
        publicidad.setEmpresas(null);
        return this;
    }

    public void setPublicidads(Set<Publicidad> publicidads) {
        this.publicidads = publicidads;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Empresas empresas = (Empresas) o;
        if (empresas.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), empresas.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Empresas{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", razonSocial='" + getRazonSocial() + "'" +
            "}";
    }
}
