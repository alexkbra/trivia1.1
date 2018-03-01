package co.com.elpoli.service.mapper;

import co.com.elpoli.domain.*;
import co.com.elpoli.service.dto.PistaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Pista and its DTO PistaDTO.
 */
@Mapper(componentModel = "spring", uses = {PreguntaMapper.class, })
public interface PistaMapper extends EntityMapper <PistaDTO, Pista> {

    @Mapping(source = "pregunta.id", target = "preguntaId")
    @Mapping(source = "pregunta.cortaDescripcion", target = "preguntaCortaDescripcion")
    PistaDTO toDto(Pista pista); 

    @Mapping(source = "preguntaId", target = "pregunta")
    Pista toEntity(PistaDTO pistaDTO); 
    default Pista fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pista pista = new Pista();
        pista.setId(id);
        return pista;
    }
}
