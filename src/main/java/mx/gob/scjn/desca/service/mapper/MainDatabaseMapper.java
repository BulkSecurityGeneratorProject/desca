package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.MainDatabaseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MainDatabase and its DTO MainDatabaseDTO.
 */
@Mapper(componentModel = "spring", uses = {MemberStateMapper.class, JudicialProcessTypeMapper.class})
public interface MainDatabaseMapper extends EntityMapper<MainDatabaseDTO, MainDatabase> {

    @Mapping(source = "memberState.id", target = "memberStateId")
    @Mapping(source = "judicialProcessType.id", target = "judicialProcessTypeId")
    MainDatabaseDTO toDto(MainDatabase mainDatabase);

    @Mapping(source = "memberStateId", target = "memberState")
    @Mapping(source = "judicialProcessTypeId", target = "judicialProcessType")
    MainDatabase toEntity(MainDatabaseDTO mainDatabaseDTO);

    default MainDatabase fromId(Long id) {
        if (id == null) {
            return null;
        }
        MainDatabase mainDatabase = new MainDatabase();
        mainDatabase.setId(id);
        return mainDatabase;
    }
}
