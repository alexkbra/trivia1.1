package co.com.elpoli.service.mapper;

import co.com.elpoli.domain.*;
import co.com.elpoli.service.dto.PreguntaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Pregunta and its DTO PreguntaDTO.
 */
@Mapper(componentModel = "spring", uses = {CuestionarioMapper.class, TipoPreguntaMapper.class, })
public interface PreguntaMapper extends EntityMapper <PreguntaDTO, Pregunta> {

    @Mapping(source = "cuestionario.id", target = "cuestionarioId")
    @Mapping(source = "cuestionario.nombre", target = "cuestionarioNombre")

    @Mapping(source = "tipoPregunta.id", target = "tipoPreguntaId")
    @Mapping(source = "tipoPregunta.nombre", target = "tipoPreguntaNombre")
    PreguntaDTO toDto(Pregunta pregunta); 
    @Mapping(target = "respuestas", ignore = true)
    @Mapping(target = "pistas", ignore = true)

    @Mapping(source = "cuestionarioId", target = "cuestionario")

    @Mapping(source = "tipoPreguntaId", target = "tipoPregunta")
    Pregunta toEntity(PreguntaDTO preguntaDTO); 
    default Pregunta fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pregunta pregunta = new Pregunta();
        pregunta.setId(id);
        return pregunta;
    }
}
