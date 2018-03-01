package co.com.elpoli.service.mapper;

import co.com.elpoli.domain.*;
import co.com.elpoli.service.dto.TipoPreguntaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TipoPregunta and its DTO TipoPreguntaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TipoPreguntaMapper extends EntityMapper <TipoPreguntaDTO, TipoPregunta> {
    
    @Mapping(target = "preguntas", ignore = true)
    TipoPregunta toEntity(TipoPreguntaDTO tipoPreguntaDTO); 
    default TipoPregunta fromId(Long id) {
        if (id == null) {
            return null;
        }
        TipoPregunta tipoPregunta = new TipoPregunta();
        tipoPregunta.setId(id);
        return tipoPregunta;
    }
}
