package com.bytatech.service.mapper;

import com.bytatech.domain.*;
import com.bytatech.service.dto.DrivoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Drivo and its DTO DrivoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DrivoMapper extends EntityMapper<DrivoDTO, Drivo> {



    default Drivo fromId(Long id) {
        if (id == null) {
            return null;
        }
        Drivo drivo = new Drivo();
        drivo.setId(id);
        return drivo;
    }
}
