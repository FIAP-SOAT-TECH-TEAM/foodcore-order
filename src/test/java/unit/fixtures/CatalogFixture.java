package unit.fixtures;

import java.math.BigDecimal;
import java.util.List;

import com.soat.fiap.food.core.order.core.interfaceadapters.dto.product.ProductDTO;
import com.soat.fiap.food.core.order.core.interfaceadapters.dto.product.StockDTO;

/**
 * Fixture utilitária para criação de catálogos e produtos simulados utilizados
 * em testes unitários da camada de pedidos.
 * <p>
 * Fornece métodos estáticos que geram produtos válidos, múltiplos produtos e
 * instâncias customizadas para cenários específicos.
 */
public class CatalogFixture {

	/**
	 * Cria um catálogo contendo um único produto ativo e com categoria ativa.
	 *
	 * @return lista com um único {@link ProductDTO} válido
	 */
	public static List<ProductDTO> createCatalogWithProducts() {
		return List.of(new ProductDTO(1L, "Hambúrguer", BigDecimal.valueOf(25.00), true, true, new StockDTO(10)));
	}

	/**
	 * Cria um catálogo contendo múltiplos produtos ativos com estoque suficiente.
	 *
	 * @return lista com múltiplos {@link ProductDTO} válidos
	 */
	public static List<ProductDTO> createCatalogWithMultipleProducts() {
		return List.of(new ProductDTO(1L, "Hambúrguer", BigDecimal.valueOf(25.00), true, true, new StockDTO(10)),
				new ProductDTO(2L, "Refrigerante", BigDecimal.valueOf(8.00), true, true, new StockDTO(20)));
	}

	/**
	 * Cria um produto de catálogo totalmente customizado para cenários de teste.
	 * <p>
	 * Permite controlar todos os atributos do produto: ID, nome, preço, flags de
	 * ativação e quantidade de estoque.
	 *
	 * @param id
	 *            identificador único do produto
	 * @param name
	 *            nome do produto
	 * @param price
	 *            preço do produto
	 * @param active
	 *            define se o produto está ativo
	 * @param categoryActive
	 *            define se a categoria do produto está ativa
	 * @param stockQty
	 *            quantidade disponível de estoque
	 * @return instância configurada de {@link ProductDTO}
	 */
	public static ProductDTO createCatalogProduct(Long id, String name, BigDecimal price, boolean active,
			boolean categoryActive, int stockQty) {
		return new ProductDTO(id, name, price, active, categoryActive, new StockDTO(stockQty));
	}

}
