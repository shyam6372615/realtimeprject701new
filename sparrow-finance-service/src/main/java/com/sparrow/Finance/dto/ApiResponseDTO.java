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
public class ApiResponseDTO {
	    private int code;
	    private String message;
	    private List<UserRolePrivilegeDTO> data;
	    private List<UsersInRolesDTO> usersInRoles;

}
