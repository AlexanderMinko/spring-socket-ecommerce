package com.minko.socket.mapper;

import com.minko.socket.dto.CartItem;
import com.minko.socket.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "quantity", constant = "1")
    CartItem mapToCartItemFromProduct(Product product);

}
