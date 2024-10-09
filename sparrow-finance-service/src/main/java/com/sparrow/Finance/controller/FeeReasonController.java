package com.sparrow.Finance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparrow.Finance.dto.FeeReasonDTO;
import com.sparrow.Finance.dto.ResponseData;
import com.sparrow.Finance.service.FeeReasonService;
import com.sparrow.finance.entity.FeeReasonEntity;

import jakarta.persistence.EntityNotFoundException;

@RestController
@CrossOrigin
public class FeeReasonController {

	@Autowired
	private FeeReasonService feeReasonService;
	
	
	
	@PostMapping("/addOrUpdateFeeReason")
    public ResponseEntity<ResponseData<Object>> addOrUpdateFeeReason(@RequestBody FeeReasonDTO feeReasonDTO) {
        try {
            FeeReasonEntity createdOrUpdatedFeeReason = feeReasonService.addOrUpdateFeeReason(feeReasonDTO);
            return ResponseEntity.ok(new ResponseData<>(0, "Successfully Added or Updated Fee Reason", createdOrUpdatedFeeReason));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ResponseData<>(1, e.getMessage(), null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData<>(1, "Fee Reason not found", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData<>(1, "Failed to Add or Update Fee Reason", null));
        }
    }
    
	
	 @DeleteMapping("/deleteFee_Delete_cancel_Reason/{instituteId}/{reasonId}")
	public ResponseEntity<ResponseData<Object>> deleteFeeReason(
	        @PathVariable Long instituteId,
	        @PathVariable Long reasonId) {
	    try {
	        boolean isDeleted = feeReasonService.deleteFeeReasonByInstituteIdAndReasonId(instituteId, reasonId);
	        if (isDeleted) {
	            return ResponseEntity.ok(new ResponseData<>(0, "Successfully Deleted Fee Reason", null));
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ResponseData<>(1, "Fee Reason Not Found", null));
	        }
	    } catch (IllegalStateException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body(new ResponseData<>(1, e.getMessage(), null));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ResponseData<>(1, "Failed to Delete Fee Reason", null));
	    }
	}
    
    
    
    @GetMapping("/getFeeReasonById/{instituteId}/{reasonId}")
    public ResponseEntity<ResponseData<Object>> getFeeReasonByInstituteIdAndReasonId(
            @PathVariable Long instituteId,
            @PathVariable Long reasonId) {
        try {
            FeeReasonDTO feeReasonDTO = feeReasonService.getFeeReasonByInstituteIdAndReasonId(instituteId, reasonId);
            return ResponseEntity.ok(new ResponseData<>(0, "Successfully fetched Fee Reason", feeReasonDTO));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData<>(1, "Fee Reason not found", null));
        }
    }
    
    
    @GetMapping("/getAllFeeReasons/{instituteId}/{type}")
    public ResponseEntity<ResponseData<List<FeeReasonDTO>>> getFeeReasonsByInstituteIdAndType(
            @PathVariable Long instituteId,
            @PathVariable String type) {
        try {
            List<FeeReasonDTO> feeReasonDTOs = feeReasonService.getFeeReasonsByInstituteIdAndType(instituteId, type);
            return ResponseEntity.ok(new ResponseData<>(0, "Successfully fetched Fee Reasons", feeReasonDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData<>(1, "Failed to fetch Fee Reasons", null));
        }
    }
    

}
