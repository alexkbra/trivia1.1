package co.com.elpoli.service.mapper;

import co.com.elpoli.domain.*;
import co.com.elpoli.service.dto.ReglaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Regla and its DTO ReglaDTO.
 */
@Mapper(componentModel = "spring", uses = {CuestionarioMapper.class, })
public interface ReglaMapper extends EntityMapper <ReglaDTO, Regla> {

    @Mapping(source = "cuestionario.id", target = "cuestionarioId")
    @Mapping(source = "cuestionario.nombre", target = "cuestionarioNombre")
    ReglaDTO toDto(Regla regla); 

    @Mapping(source = "cuestionarioId", target = "cuestionario")
    Regla toEntity(ReglaDTO reglaDTO); 
    default Regla fromId(Long id) {
        if (id == null) {
            return null;
        }
        Regla regla = new Regla();
        regla.setId(id);
        return regla;
    }
}
