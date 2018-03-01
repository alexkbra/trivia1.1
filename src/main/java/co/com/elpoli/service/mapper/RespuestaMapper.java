package co.com.elpoli.service.mapper;

import co.com.elpoli.domain.*;
import co.com.elpoli.service.dto.RespuestaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Respuesta and its DTO RespuestaDTO.
 */
@Mapper(componentModel = "spring", uses = {PreguntaMapper.class, })
public interface RespuestaMapper extends EntityMapper <RespuestaDTO, Respuesta> {

    @Mapping(source = "pregunta.id", target = "preguntaId")
    @Mapping(source = "pregunta.cortaDescripcion", target = "preguntaCortaDescripcion")
    RespuestaDTO toDto(Respuesta respuesta); 

    @Mapping(source = "preguntaId", target = "pregunta")
    Respuesta toEntity(RespuestaDTO respuestaDTO); 
    default Respuesta fromId(Long id) {
        if (id == null) {
            return null;
        }
        Respuesta respuesta = new Respuesta();
        respuesta.setId(id);
        return respuesta;
    }
}
