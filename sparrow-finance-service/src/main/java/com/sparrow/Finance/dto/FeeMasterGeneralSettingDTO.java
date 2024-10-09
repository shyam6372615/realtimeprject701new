package com.sparrow.Finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FeeMasterGeneralSettingDTO {

	private Long feeGeneralSeetingId;
	private Long fkInstituteId;
	private Boolean isManual;
	private Boolean isAutomation;
	private String seqPredixText;
	private int seqNumDigit;
	private Long seqStartNumber;
	private String seqPreview;
	private Boolean cashDisabled;
	private String cashPrefixText;
	private int cashNumDigit;
	private Long cashStartNumber;
	private String cashPreview;
	private Boolean taxDisabled;
	private Boolean isActive=true;
}
