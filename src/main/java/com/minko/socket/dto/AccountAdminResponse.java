package com.minko.socket.dto;

import com.minko.socket.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountAdminResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Date createdDate;
    private String photoUrl;
    private Boolean enabled;
    private List<RoleType> roles;

}
