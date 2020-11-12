package com.minko.socket.mapper;

import com.minko.socket.dto.OrderItemResponseDto;
import com.minko.socket.dto.OrderRequestDto;
import com.minko.socket.dto.OrderResponseDto;
import com.minko.socket.entity.Account;
import com.minko.socket.entity.Order;
import com.minko.socket.entity.OrderItem;
import com.minko.socket.entity.Product;
import com.minko.socket.service.AccountService;
import com.minko.socket.service.ProductService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Autowired
    private ProductService productService;

    @Autowired
    private AccountService accountService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "count", source = "orderItem.count")
    @Mapping(target = "product", expression = "java(getProduct(orderItem.getProductId()))")
    public abstract OrderItem mapFromDto(com.minko.socket.dto.OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "account", expression = "java(getAccount(orderRequestDto.getEmail()))")
    public abstract Order mapFromDtoToOrder(OrderRequestDto orderRequestDto);

    @Mapping(target = "createdDate", expression = "java(java.util.Date.from(order.getCreatedDate()))")
    public abstract OrderResponseDto mapToDto(Order order);

    @Mapping(target = "productId", expression = "java(orderItem.getProduct().getId())")
    @Mapping(target = "name", expression = "java(orderItem.getProduct().getName())")
    @Mapping(target = "imageUrl", expression = "java(orderItem.getProduct().getImageUrl())")
    @Mapping(target = "description", expression = "java(orderItem.getProduct().getDescription())")
    @Mapping(target = "price", expression = "java(orderItem.getProduct().getPrice())")
    public abstract OrderItemResponseDto mapToDtoFromOrderItem(OrderItem orderItem);

    Product getProduct(Long id) {
        return productService.getProductById(id);
    }

    Account getAccount(String email) {
        return accountService.getByEmail(email);
    }

}
