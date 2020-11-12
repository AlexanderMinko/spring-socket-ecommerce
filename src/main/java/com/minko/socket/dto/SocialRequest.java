package com.minko.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialRequest {

    private String firstName;
    private String lastName;
    private String token;
    private String photoUrl;

}
