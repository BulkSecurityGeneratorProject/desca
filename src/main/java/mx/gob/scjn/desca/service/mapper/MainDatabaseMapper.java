package mx.gob.scjn.desca.service.mapper;

import mx.gob.scjn.desca.domain.*;
import mx.gob.scjn.desca.service.dto.MainDatabaseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MainDatabase and its DTO MainDatabaseDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MainDatabaseMapper extends EntityMapper<MainDatabaseDTO, MainDatabase> {



    default MainDatabase fromId(Long id) {
        if (id == null) {
            return null;
        }
        MainDatabase mainDatabase = new MainDatabase();
        mainDatabase.setId(id);
        return mainDatabase;
    }
}
