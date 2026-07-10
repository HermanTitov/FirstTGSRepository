package LaGavioTa.project.util.mappers;

import LaGavioTa.project.dto.CategoryDTO;
import LaGavioTa.project.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/// Интерфейс маппинга для категорий

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category convertToEntity(CategoryDTO dto);
    CategoryDTO convertToDTO(Category entity);
}
