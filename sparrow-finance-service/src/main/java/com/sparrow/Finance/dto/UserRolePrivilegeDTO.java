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
public class UserRolePrivilegeDTO {
	private String userName;
    private Long appUserId;
    private Long profileId;
    private String userEmail;
    private String userPhoneNo;
    private List<RolePrivilegeDTO> listOfRolesAndPrivilege;

}
