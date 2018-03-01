package co.com.elpoli.service.mapper;

import co.com.elpoli.domain.*;
import co.com.elpoli.service.dto.ParticiparDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Participar and its DTO ParticiparDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ParticiparMapper extends EntityMapper <ParticiparDTO, Participar> {
    
    
    default Participar fromId(Long id) {
        if (id == null) {
            return null;
        }
        Participar participar = new Participar();
        participar.setId(id);
        return participar;
    }
}
