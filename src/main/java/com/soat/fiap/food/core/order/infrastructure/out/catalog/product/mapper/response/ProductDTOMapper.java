package com.soat.fiap.food.core.order.infrastructure.out.catalog.product.mapper.response;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.soat.fiap.food.core.order.core.interfaceadapters.dto.product.ProductDTO;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.product.StockDTO;
import com.soat.fiap.food.core.order.infrastructure.out.catalog.product.entity.ProductEntity;
import com.soat.fiap.food.core.order.infrastructure.out.catalog.product.entity.StockEntity;

/**
 * Mapper responsável por converter {@link ProductEntity} em {@link ProductDTO}
 * e {@link StockEntity} em {@link StockDTO}.
 * <p>
 * Esse mapper é utilizado para extrair apenas os dados relevantes das entidades
 * de catálogo que serão utilizados na lógica de aplicação (caso de uso).
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDTOMapper {

	/**
	 * Converte uma entidade de produto em um Dto contendo apenas os dados
	 * necessários para a lógica de aplicação.
	 *
	 * @param entity
	 *            entidade de produto retornada pela API do catálogo
	 * @return objeto de saída da camada de aplicação com os dados mapeados
	 */
	@Mapping(target = "stock", source = "stock")
	ProductDTO toDto(ProductEntity entity);

	/**
	 * Converte uma lista de entidades de produto em uma lista de Dtos.
	 *
	 * @param entities
	 *            lista de entidades de produto
	 * @return lista de objetos de saída da camada de aplicação com os dados
	 *         mapeados
	 */
	List<ProductDTO> toDtoList(List<ProductEntity> entities);
}
