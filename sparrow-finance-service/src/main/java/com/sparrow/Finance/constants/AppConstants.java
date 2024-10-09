package com.sparrow.Finance.constants;

public enum AppConstants {
	FEE_SETUP_SUCCESS("Fee setup processed successfully"),
	FEE_SETUP_FAILURE("Failed to process fee setup"),
	RECORD_NOT_FOUND("Record not found with ID: "),
	RECORD_FOUND("Record fetched successfully"),
	PROCESSING_ERROR("An error occurred while processing the request. Please try again later."),
	 FEE_MASTER_SETUP_NOT_FOUND("Error: Fee Master Setup record not found for ID: "),
	 NULL_FEE_DETAILS_DTO("FeeMasterDetailsInputsDTO cannot be null"),
	FEE_TYPE_SETUP_SUCCESS("Fee type processed  successfully"),
	FEE_TYPE_SETUP_FAILURE("Failed to process Fee type"),
	DEFAULT_RECORD_UPDATE_NOT_ALLOWED("Update not allowed for default records Field with ID: "),
	FEE_TYPE_SETUP_PARTIAL_SUCCESS("Some fee types were processed successfully, but there were issues with some records."),
	FEE_HEAD_MASTER_NOT_FOUND("FeeHeadMaster not found with ID: "),
	FEE_HEAD_SETUP_SUCCESS("Fee head setup completed successfully."),
	FEE_HEAD_SETUP_FAILURE("Fee head setup failed. Please try again."),
	FEE_HEAD_SETUP_PARTIAL_SUCCESS("Fee head setup partially successful. Some records may not have been processed."),
	FEE_HEAD_DEACTIVATION_NOT_ALLOWED("You cannot deactivate this Record as it has associated dependencies."),
	DEFAULT_RECORD_DELETION_NOT_ALLOWED("Default records are not allowed to delete"),
	DUPLICATE_FEETYPE_NAME("Fee type Name already exists with name. "),
	DUPLICATE_FEETHEAD_NAME("Fee Head Name already exists with name. "),
	ADD_ON_DISCOUNT_FAILURE("Failed to save Add On Discount"),
	ADD_ON_DISCOUNT_SUCCESS("Add on Discount Proccessed Successfully"),
	ERROR_CONVERTING_ADD_ON_DISCOUNT("Error converting AddOnDiscount to AddOnDiscountDataDTO"),
	UNEXPECTED_ERROR_RETRIEVING_ADD_ON_DISCOUNT("An unexpected error occurred while retrieving the add-on discount"),
	DATA_NOT_FOUND("Data not Found With Instituite Id : "),
	DATA_RERIEVED_SUCCESSFULLY("Data Retrieved Successfully"),
	FAILED_TO_FETCH_DATA("Failed to Fetch Data"),
	RECORD_DEACTIVATION_SUCESSFULL("Record Deactivated Successfully."),
	PROFILE_DISCOUNT_FAILURE("Failed to save Profile Discount"),
	PROFILE_DISCOUNT_SUCCESS("Profile Discount Proccessed Successfully"),
	DATA_SAVED_SUCCESSFULLY("Data Saved Successfully"),
	USER_ROLES_PRIVILEGE_FETCH_FAILED("Failed to fetch user roles with privilege"),
	 STUDENT_INFO_FETCH_FAILED("Failed to fetch student info"),
	 DISCOUNT_TYPE("Add-on Discount"),
	 PROFILE_DISCOUNT("Profile Discount"),
	 DATA_NOT_FOUND_WITH_ID("Record not Found with Provided Id : "),
	 SUCCESS_CODE(200),
	  FAILURE_CODE(410),
	  SERVER_ERROR_CODE(500),
	  DUPLICATE_CODE(415),
	  FAILED_TO_SAVE_DATA("Failed To save Data"),
	  DUPLICATE_FEE_HEAD("Data already present with the selected feeHead");
	 
	

	    private final String message;
	    private final Integer code;

	    AppConstants(String message) {
	        this.message = message;
	        this.code = null;
	    }

	    AppConstants(Integer code) {
	        this.code = code;
	        this.message = null;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public Integer getCode() {
	        return code;
	    }

}
