package com.sparrow.Finance.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersInRolesDTO {
	private String roleName;
    private Long roleId;
    private List<UserRolePrivilegeDTO> listAccessPolicyUser;

}
