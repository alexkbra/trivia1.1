package co.com.elpoli.service.mapper;

import co.com.elpoli.domain.*;
import co.com.elpoli.service.dto.NivelDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Nivel and its DTO NivelDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NivelMapper extends EntityMapper <NivelDTO, Nivel> {
    
    @Mapping(target = "cuestionarios", ignore = true)
    Nivel toEntity(NivelDTO nivelDTO); 
    default Nivel fromId(Long id) {
        if (id == null) {
            return null;
        }
        Nivel nivel = new Nivel();
        nivel.setId(id);
        return nivel;
    }
}
