package co.com.elpoli.service.mapper;

import co.com.elpoli.domain.*;
import co.com.elpoli.service.dto.CuestionarioDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Cuestionario and its DTO CuestionarioDTO.
 */
@Mapper(componentModel = "spring", uses = {NivelMapper.class, ExpedicionMapper.class, })
public interface CuestionarioMapper extends EntityMapper <CuestionarioDTO, Cuestionario> {

    @Mapping(source = "expedicion.id", target = "expedicionId")
    @Mapping(source = "expedicion.nombre", target = "expedicionNombre")
    CuestionarioDTO toDto(Cuestionario cuestionario); 
    @Mapping(target = "reglas", ignore = true)
    @Mapping(target = "preguntas", ignore = true)

    @Mapping(source = "expedicionId", target = "expedicion")
    Cuestionario toEntity(CuestionarioDTO cuestionarioDTO); 
    default Cuestionario fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cuestionario cuestionario = new Cuestionario();
        cuestionario.setId(id);
        return cuestionario;
    }
}
