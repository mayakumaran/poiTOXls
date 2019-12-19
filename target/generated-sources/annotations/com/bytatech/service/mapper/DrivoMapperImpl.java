package com.bytatech.service.mapper;

import com.bytatech.domain.Drivo;
import com.bytatech.service.dto.DrivoDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-12-19T19:36:03+0530",
    comments = "version: 1.2.0.Final, compiler: javac, environment: Java 1.8.0_222 (Private Build)"
)
@Component
public class DrivoMapperImpl implements DrivoMapper {

    @Override
    public Drivo toEntity(DrivoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Drivo drivo = new Drivo();

        drivo.setId( dto.getId() );
        drivo.setRegNo( dto.getRegNo() );
        drivo.setOwnerName( dto.getOwnerName() );
        drivo.setMobileNo( dto.getMobileNo() );
        drivo.setVehdecscr( dto.getVehdecscr() );

        return drivo;
    }

    @Override
    public DrivoDTO toDto(Drivo entity) {
        if ( entity == null ) {
            return null;
        }

        DrivoDTO drivoDTO = new DrivoDTO();

        drivoDTO.setId( entity.getId() );
        drivoDTO.setRegNo( entity.getRegNo() );
        drivoDTO.setOwnerName( entity.getOwnerName() );
        drivoDTO.setMobileNo( entity.getMobileNo() );
        drivoDTO.setVehdecscr( entity.getVehdecscr() );

        return drivoDTO;
    }

    @Override
    public List<Drivo> toEntity(List<DrivoDTO> dtoList) {
        if ( dtoList == null ) {
            return null;
        }

        List<Drivo> list = new ArrayList<Drivo>( dtoList.size() );
        for ( DrivoDTO drivoDTO : dtoList ) {
            list.add( toEntity( drivoDTO ) );
        }

        return list;
    }

    @Override
    public List<DrivoDTO> toDto(List<Drivo> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<DrivoDTO> list = new ArrayList<DrivoDTO>( entityList.size() );
        for ( Drivo drivo : entityList ) {
            list.add( toDto( drivo ) );
        }

        return list;
    }
}
