package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolePrivilegeDTO {
    private Long roleId;
    private String roleName;
    private String roleDescription;
    private Object accessPolicy; 

}

