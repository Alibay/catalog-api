package net.nomia.catalog.mapper;

import com.google.common.collect.Lists;
import net.nomia.catalog.domain.Product;
import net.nomia.catalog.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class ProductMapper {

   public Product managedProductDtoToProduct(final ProductDto productDto) {
       Product product = new Product();
       product.setName(productDto.getName());

       return product;
   }

   public ProductDto productToProductDto(final Product product) {
       ProductDto productDto = new ProductDto();
       productDto.setId(product.getId());
       productDto.setName(product.getName());
       productDto.setCategoryId(product.getCategory().getId());

       return productDto;
   }

    public List<ProductDto> productsToProductDtos(final Iterable<Product> products) {
        return Lists.newArrayList(products)
                .stream()
                .map(this::productToProductDto)
                .collect(toList());
    }
}
