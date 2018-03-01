package co.com.elpoli.service.mapper;

import co.com.elpoli.domain.*;
import co.com.elpoli.service.dto.EmpresasDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Empresas and its DTO EmpresasDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmpresasMapper extends EntityMapper <EmpresasDTO, Empresas> {
    
    @Mapping(target = "publicidads", ignore = true)
    Empresas toEntity(EmpresasDTO empresasDTO); 
    default Empresas fromId(Long id) {
        if (id == null) {
            return null;
        }
        Empresas empresas = new Empresas();
        empresas.setId(id);
        return empresas;
    }
}
