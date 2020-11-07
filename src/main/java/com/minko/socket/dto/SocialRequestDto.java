package com.minko.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialRequestDto {

    private String token;
    private String firstName;
    private String lastName;

}
