package LaGavioTa.project.util.mappers;

import LaGavioTa.project.dto.DishFullDTO;
import LaGavioTa.project.dto.DishShortDTO;
import LaGavioTa.project.models.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DishMapper {
    DishShortDTO convertToShortDTO(Dish entity);
    DishFullDTO convertToFullDTO(Dish entity);
    Dish convertToEntity(DishFullDTO dto);
}
