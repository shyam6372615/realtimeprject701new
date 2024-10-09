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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparrow.Finance.Exception.RecordNotFoundException;
import com.sparrow.Finance.dto.FeeDiscountDTO;
import com.sparrow.Finance.dto.FeeMasterGeneralSettingDTO;
import com.sparrow.Finance.dto.Response;
import com.sparrow.Finance.dto.ResponseData;
import com.sparrow.Finance.service.FeeDiscountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@RequestMapping("/api/finance")
@Slf4j
public class FeeDiscountController {

	@Autowired
	private FeeDiscountService feeDiscountService;
	
	
	@PostMapping("/addFeeDiscount")
	public ResponseEntity<ResponseData<FeeDiscountDTO>> addHoliday(@RequestBody FeeDiscountDTO feeDiscountDTO) {
	    try {
	    	FeeDiscountDTO feeDiscountDetails = feeDiscountService.addFeeDiscount(feeDiscountDTO);
	        return ResponseEntity.ok(new ResponseData<>(0, "FeeDiscount added successfully", feeDiscountDetails));
	    }catch(IllegalArgumentException iae) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData<>(0,iae.getMessage() , null));
	    } catch (Exception e) {
	        log.error("Error occurred while adding FeeDiscount: ", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(new ResponseData<>(1, "Failed to add FeeDiscount", null));
	    }
	}
	
	@GetMapping("/getAllFeeDiscount/{instituteId}")
  	public ResponseEntity<?> getAllFeeDiscount(@PathVariable("instituteId") Long instituteId) {
  	    List<FeeDiscountDTO> list = feeDiscountService.getAllFeeDiscount(instituteId);
  	    if (list != null && !list.isEmpty()) {
  	        return ResponseEntity.ok(new ResponseData<>(0, "FeeDiscount details fetched successfully", list));
  	    } else {
  	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData<>(1, "FeeDiscount details failed to retrieve", null));
  	    }
  	}
	@GetMapping("/getFeeDiscountByIdAndInstituteId/{instituteId}/{feeDiscountHeadId}")
	public ResponseEntity<?> getFeeDiscountByIdAndInstituteId(
	        @PathVariable("instituteId") Long instituteId,
	        @PathVariable("feeDiscountHeadId") Long feeDiscountHeadId) {
	    
	    FeeDiscountDTO feeDiscount = feeDiscountService.getFeeDiscountByIdAndInstituteId(feeDiscountHeadId, instituteId);
	    
	    if (feeDiscount != null) {
	        return ResponseEntity.ok(new ResponseData<>(0, "FeeDiscount details fetched successfully", feeDiscount));
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData<>(1, "FeeDiscount not found", null));
	    }
	}
	
	 @DeleteMapping("/deactivate/feeDiscount/{instituteId}/{feeDiscountHeadId}") 
	  public ResponseEntity<?> deactivateTransferBook(@PathVariable Long instituteId,@PathVariable Long feeDiscountHeadId){
		  Response<?> response=feeDiscountService.deactivateFeeDiscount(feeDiscountHeadId);
		  return new ResponseEntity<>(response,HttpStatus.OK);
	}

	 @PostMapping("/addOrUpdateFeeMasterGeneralSetting")
	    public ResponseEntity<ResponseData<FeeMasterGeneralSettingDTO>> addOrUpdateFeeMasterGeneralSetting(@RequestBody FeeMasterGeneralSettingDTO feeMasterGeneralSettingDTO) {
		
		 try {
		 FeeMasterGeneralSettingDTO feeMasterGeneralSetting = feeDiscountService.addOrUpdateFeeMasterGeneralSetting(feeMasterGeneralSettingDTO);
		 return ResponseEntity.ok(new ResponseData<>(0, "Successfully added FeeMasterGeneralSetting", feeMasterGeneralSetting));
		 }catch(RecordNotFoundException e) {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData<>(1,e.getMessage(), null));
		 }catch(Exception e) {
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData<>(1, "unable to add FeeMasterGeneralSetting", null));
	    }
	 }
	 
	 @GetMapping("/getFeeMasterGeneralSetting/{instituteId}")
		public ResponseEntity<ResponseData<FeeMasterGeneralSettingDTO>>   getFeeMasterGeneralSetting(@PathVariable("instituteId") Long instituteId){
			try {
				FeeMasterGeneralSettingDTO result = feeDiscountService.getFeeMasterByInstituteId(instituteId);
				
		            return ResponseEntity.ok(new ResponseData<>(0, "Successfully retrieved", result));
		        } catch(Exception e) {
		        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData<>(1, "FeeMaster General setting not found for this Institute", null));
		        }                          	 
		 }
}	
	
