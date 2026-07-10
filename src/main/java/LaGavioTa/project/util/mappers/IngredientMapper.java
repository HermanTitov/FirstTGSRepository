package LaGavioTa.project.util.mappers;

import LaGavioTa.project.dto.IngredientDTO;
import LaGavioTa.project.models.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/// Интерфейс маппинга для ингредиентов

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IngredientMapper {
    Ingredient convertToEntity(IngredientDTO dto);
    IngredientDTO convertToDTO(Ingredient entity);
}
