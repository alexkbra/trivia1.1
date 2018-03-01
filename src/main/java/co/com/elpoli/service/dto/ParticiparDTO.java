package co.com.elpoli.service.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Participar entity.
 */
public class ParticiparDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParticiparDTO participarDTO = (ParticiparDTO) o;
        if(participarDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), participarDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ParticiparDTO{" +
            "id=" + getId() +
            "}";
    }
}
