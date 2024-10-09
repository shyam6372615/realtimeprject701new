package com.sparrow.Finance.controller;


import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sparrow.Finance.Exception.RecordNotFoundException;
import com.sparrow.Finance.constants.AppConstants;
import com.sparrow.Finance.dto.AddOnDiscountDataDTO;
import com.sparrow.Finance.dto.AddOnDiscountDataResponseDTO;
import com.sparrow.Finance.dto.AddOnDiscountFilterDTO;
import com.sparrow.Finance.dto.AddOnDiscountResponseDTO;
import com.sparrow.Finance.dto.BankDTO;
import com.sparrow.Finance.dto.CashierDataDTO;
import com.sparrow.Finance.dto.CountryRequestDTO;
import com.sparrow.Finance.dto.CourseDetailsDTO;
import com.sparrow.Finance.dto.DefaultFeeSettingsConfigDTO;
import com.sparrow.Finance.dto.DiscountResponseDTO;
import com.sparrow.Finance.dto.FeeConfigOutputDTO;
import com.sparrow.Finance.dto.FeeHeadDetailsDTO;
import com.sparrow.Finance.dto.FeeHeadMasterDTO;
import com.sparrow.Finance.dto.FeeHeadMasterOutputDTO;
import com.sparrow.Finance.dto.FeeSetupInputsDTO;
import com.sparrow.Finance.dto.FeeTypeMasterDTO;
import com.sparrow.Finance.dto.FinanceAccountSetupDTO;
import com.sparrow.Finance.dto.PaymentModeConfigDTO;
import com.sparrow.Finance.dto.ProfileDiscountDataDTO;
import com.sparrow.Finance.dto.ProfileDiscountGetDTO;
import com.sparrow.Finance.dto.ReminderConfigDTO;
import com.sparrow.Finance.dto.Response;
import com.sparrow.Finance.dto.ResponseData;
import com.sparrow.Finance.dto.StudentInfoDTO;
import com.sparrow.Finance.feignclient.StudentServiceFeignClient;
import com.sparrow.Finance.feignclient.WebClientService;
import com.sparrow.Finance.service.CountryProducer;
import com.sparrow.Finance.service.IFinanceSevice;
import com.sparrow.finance.entity.FeeConfig;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin
@RequestMapping("/api/finance")
@Slf4j
public class FinanceController {
	
	
	
	 @Autowired

	 private WebClientService webClientService;
	 @Autowired
	 private StudentServiceFeignClient studentServiceFeignClient;
	 @Autowired
	 private IFinanceSevice financeService;
	 
	 @Autowired
	 private IFinanceSevice financeSevice;
	 @Autowired
	 private  CountryProducer countryProducer;

	   

	 @PostMapping("/addOrUpdateFeeSetup")
	 public ResponseEntity<Response<?>> addOrUpdateFeeSetup(@RequestBody FeeSetupInputsDTO feeSetupInputsDTO) {
	     log.info("Received request to add/update fee setup for Institute ID: {}", feeSetupInputsDTO.getInstituteId());
	     try {
	         Response<?> response = financeService.addOrUpdateFeeSetup(feeSetupInputsDTO);
	         return new ResponseEntity<>(response, HttpStatus.OK);
	     } catch (Exception e) {
	         log.error("Error occurred while processing fee setup: ", e);
	         Response<String> errorResponse = new Response<>( AppConstants.SERVER_ERROR_CODE.getCode(),AppConstants.FEE_SETUP_FAILURE.getMessage(), e.getMessage());
	         return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	     }
	 }
	 
	 @GetMapping("/getFeeSetupById/{id}/{instituteId}")
	 public ResponseEntity<?> getFeeSetupById(@PathVariable Long id, @PathVariable Long instituteId) {
	     log.info("Received request to fetch FeeSetup with ID: {} and Institute ID: {}", id, instituteId);
	     
	     try {
	         Response feeSetupInputsDTO = financeService.getFeeSetupById(id, instituteId);
	         log.info("FeeSetupInputsDTO fetched successfully for ID: {} and Institute ID: {}", id, instituteId);
	         return ResponseEntity.ok(feeSetupInputsDTO);
	     } catch (Exception e) {
	         log.error("Error occurred while fetching FeeSetup for ID: {} and Institute ID: {}", id, instituteId, e);
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body(new Response<>(AppConstants.SERVER_ERROR_CODE.getCode(), "Failed to fetch data. Please try again later."));
	     }
	 }


	 
	 @GetMapping("/getAllFeeSetup")
	 public ResponseEntity<?> getAllFeeSetup(
	            @RequestParam Long instituteId,
	            @RequestParam(value = "feeType", defaultValue = "none") String feeType) {
	       
	            Response feeSetupInputs = financeService.getAllFeeSetup(instituteId, feeType);
	               
	            return new ResponseEntity<>(feeSetupInputs, HttpStatus.OK);
	       
	    }
	 @DeleteMapping("/deactivateFeeSetup/{id}/{instituteId}")
	    public Response<?> deactivateFeeSetup(
	            @PathVariable Long id,
	            @PathVariable Long instituteId) {
	        Response<?> response = financeService.deactivateFeeSetup(id, instituteId);
	        return response;
	    }


	 @GetMapping("/courses/{instituteId}")
     public Flux<CourseDetailsDTO> getCourses(@PathVariable Long instituteId) {
	     return webClientService.getCoursesByInstituteId(instituteId);
	  }
	 
	 @PostMapping("/add-or-update/feeTypeMaster")
	    public ResponseEntity<?> addOrUpdateFeeTypeMaster(@RequestBody List<FeeTypeMasterDTO> feeTypeMasterDTO) {
	        try {
	        	 Response<?> response = financeService.addorUpdateFeeTypeMaster(feeTypeMasterDTO);
		        
		         return new ResponseEntity<>(response, HttpStatus.OK);
		     } catch (Exception e) {
		         log.error("Error occurred while processing fee setup: ", e);
		         Response<String> errorResponse = new Response<>( HttpStatus.INTERNAL_SERVER_ERROR.value(),AppConstants.FEE_SETUP_FAILURE.getMessage(), e.getMessage());
		         return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		     }
	    }
	 @PostMapping("/feeTypeMaster/add-or-update")
	    public ResponseEntity<?> addOrUpdateFeeTypeMaster(@RequestBody FeeTypeMasterDTO feeTypeMasterDTO) {
	        Response<?> response = financeService.addorUpdateFeeTypeMaster(feeTypeMasterDTO);
	        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
	    }
	 @GetMapping("/getAllFeeTypeMasters/{instituteId}")
	    public ResponseEntity<?> getAllFeeTypeMasters(
	            @PathVariable Long instituteId) {
	        try {
	            List<FeeTypeMasterDTO> feeTypeMasterDTOs = financeService.getAllFeeTypeMaster(instituteId);
	            if (feeTypeMasterDTOs.isEmpty()) {
	                return new ResponseEntity<>("No fee type master records found.", HttpStatus.NOT_FOUND);
	            }
	            return new ResponseEntity<>(feeTypeMasterDTOs, HttpStatus.OK);
	        } catch (Exception e) {
	            log.error("An error occurred while fetching fee type master data", e);
	            return new ResponseEntity<>(AppConstants.PROCESSING_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	 @GetMapping("/getFeeTypeMasterById/{id}/{instituteId}")
	 public ResponseEntity<Response<FeeTypeMasterDTO>> getFeeTypeMasterById(@PathVariable Long id, @PathVariable Long instituteId) {
	     try {
	         log.info("Received request to fetch FeeTypeMaster with ID: {} and Institute ID: {}", id, instituteId);
	         FeeTypeMasterDTO feeTypeMasterDTO = financeService.getFeeTypeMasterById(id, instituteId);
	         log.info("FeeTypeMasterDTO fetched successfully for ID: {} and Institute ID: {}", id, instituteId);
	         return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), "FeeTypeMaster fetched successfully", feeTypeMasterDTO));
	     } catch (RecordNotFoundException e) {
	         log.error("Record not found for FeeTypeMaster with ID: {} and Institute ID: {}", id, instituteId);
	         return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                 .body(new Response<>(HttpStatus.NOT_FOUND.value(), AppConstants.RECORD_NOT_FOUND.getMessage() + " ID: " + id+" And institute ID: "+instituteId, null));
	     } catch (Exception e) {
	         log.error("An error occurred while fetching FeeTypeMaster with ID: {} and Institute ID: {}", id, instituteId, e);
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                 .body(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PROCESSING_ERROR.getMessage(), null));
	     }
	 }
	 
	 @DeleteMapping("/deactivateFeeTypeMaster/{id}/{instituteId}")
	    public Response<?> deactivateFeeTypeMaster(
	            @PathVariable Long id,
	            @PathVariable Long instituteId) {
	        Response<?> response = financeService.deactivateFeeTypeMaster(id, instituteId);
	        return response;
	    }


		
		 @GetMapping("/getAllFeeConfigs")
		    public ResponseEntity<List<FeeConfigOutputDTO>> getAllFeeConfig() {
		        try {
		            List<FeeConfigOutputDTO> feeConfigs = financeSevice.getAllFeeConfig();
		            return new ResponseEntity<>(feeConfigs, HttpStatus.OK);
		        } catch (Exception e) {
		            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		        }
		 }
		 
		 @PostMapping("/addorUpdate/feeHeadMaster")
		    public ResponseEntity<Response<?>> addFeeHeadMaster(@RequestBody List<FeeHeadMasterDTO> listFeeHeadMasterDTO) {
		        Response<?> response = financeSevice.addorUpdateFeeHeadMaster(listFeeHeadMasterDTO);
		        return ResponseEntity.status(response.getCode()).body(response);
		    }
		 @GetMapping("/getFeeTypeMaster")
		    public ResponseEntity<?> getFeeTypeMasterByFeeTypeName(
		            @RequestParam Long instituteId,
		            @RequestParam String feeType) {
		        try {
		            Response<?> feeTypeMasterDTO = financeService.geetypeMasterByFeeTypeName(instituteId, feeType);
		            return ResponseEntity.ok(feeTypeMasterDTO);
		        } catch (RecordNotFoundException e) {
		        	 return ResponseEntity.status(HttpStatus.NOT_FOUND)
			                 .body(new Response<>(HttpStatus.NOT_FOUND.value(), AppConstants.RECORD_NOT_FOUND.getMessage() + " Fee Type: " + feeType+" And institute ID: "+instituteId, null));
			     } catch (Exception e) {
			         log.error("An error occurred while fetching FeeTypeMaster : {} and Institute ID: {}", feeType, instituteId, e);
			         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			                 .body(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PROCESSING_ERROR.getMessage(), null));
			     }
		 }
		 
		 @PostMapping("/send-country-request")
		    public String sendCountryRequest(@RequestBody CountryRequestDTO countryRequestDTO) {
		        String dynamicTopic = "country-topic-" + countryRequestDTO.getCountryName().toLowerCase();
		        countryProducer.sendMessage(dynamicTopic, countryRequestDTO);
		        return "Request sent to topic: " + dynamicTopic;
		    }
		    
		 
		 @GetMapping("/getAllFeeHeadMasters/{instituteId}")
		    public ResponseEntity<?> getAllFeeHeadMasters(
		            @PathVariable Long instituteId) {
		       
		            Response respponse=  financeService.getAllFeeHeadMaster(instituteId);
		            return new ResponseEntity<>(respponse, HttpStatus.OK);
		    }
		 @GetMapping("/getFeeTypeHeadById/{id}/{instituteId}")
		 public ResponseEntity<Response<FeeHeadMasterOutputDTO>> getFeeTypeHeadById(@PathVariable Long id, @PathVariable Long instituteId) {
		     try {
		         log.info("Received request to fetch FeeHeadMaster with ID: {} and Institute ID: {}", id, instituteId);
		         FeeHeadMasterOutputDTO feeHeadMasterOutputDTO = financeService.getFeeHeadMasterById(id, instituteId);
		         log.info("feeHeadMasterOutputDTO fetched successfully for ID: {} and Institute ID: {}", id, instituteId);
		         return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), "FeeHeadMaster fetched successfully", feeHeadMasterOutputDTO));
		     } catch (RecordNotFoundException e) {
		         log.error("Record not found for FeeTypeMaster with ID: {} and Institute ID: {}", id, instituteId);
		         return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                 .body(new Response<>(HttpStatus.NOT_FOUND.value(), AppConstants.RECORD_NOT_FOUND.getMessage() + " ID: " + id+" And institute ID: "+instituteId, null));
		     } catch (Exception e) {
		         log.error("An error occurred while fetching FeeTypeMaster with ID: {} and Institute ID: {}", id, instituteId, e);
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                 .body(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PROCESSING_ERROR.getMessage(), null));
		     }
		 }
		 
		 @DeleteMapping("/deactivateFeeHeadMaster/{id}/{instituteId}")
		    public Response<?> deactivateFeeHeadMaster(
		            @PathVariable Long id,
		            @PathVariable Long instituteId) {
		        Response<?> response = financeService.deactivateFeeHeadMaster(id, instituteId);
		        return response;
		    }
		 @PostMapping("/addOrUpdate/addOnDiscount")
		 public ResponseEntity<Response<?>> addOrUpdateaddOnDiscount(@RequestBody AddOnDiscountDataDTO addOnDiscountDTO) {
		     log.info("Received request to add/update fee setup for Institute ID: {}", addOnDiscountDTO.getFkInstituteId());
		     try {
		         Response<?> response = financeService.addOrUpdateAddOnDiscount(addOnDiscountDTO);
		         return new ResponseEntity<>(response, HttpStatus.OK);
		     } catch (Exception e) {
		         log.error("Error occurred while processing Add on Discount: ", e);
		         Response<String> errorResponse = new Response<>( HttpStatus.INTERNAL_SERVER_ERROR.value(),AppConstants.ADD_ON_DISCOUNT_FAILURE.getMessage(), e.getMessage());
		         return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		     }
		 }
		 
		 @GetMapping("/getAddOnDiscountById/{id}/{instituteId}")
		    public ResponseEntity<Response<AddOnDiscountDataDTO>> getAddOnDiscountById(@PathVariable Long id, @PathVariable Long instituteId) {
		        try {
		            log.info("Received request to fetch Add On Discount with ID: {} and Institute ID: {}", id, instituteId);
		            AddOnDiscountDataResponseDTO addOnDiscountDataDTO = financeService.getAddOnDiscountById(id, instituteId);
		            log.info("AddOnDiscountDataDTO fetched successfully for ID: {} and Institute ID: {}", id, instituteId);
		            return ResponseEntity.ok(new Response(HttpStatus.OK.value(),AppConstants.DATA_RERIEVED_SUCCESSFULLY.getMessage(),addOnDiscountDataDTO) );
		        } catch (RecordNotFoundException e) {
		            log.error("Record not found: {}", e.getMessage());
		            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		        } catch (Exception e) {
		            log.error("An error occurred while fetching Add On Discount with ID: {} and Institute ID: {}", id, instituteId, e);
		            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, AppConstants.FAILED_TO_FETCH_DATA.getMessage(), e);
		        }
		    }
		 
		 @PostMapping("/getAllAddOnDiscount")
		    public ResponseEntity<Response<AddOnDiscountDataDTO>> getAllAddOnDiscount(@RequestBody AddOnDiscountFilterDTO addOnDiscountFilterDTO ) {
		        try {
		            log.info("Received request to fetch Add On Discount with  Institute ID: {}", addOnDiscountFilterDTO.getInstituteId());
		            Response listaddOnDiscountDataDTO = financeService.getAllAddOnDiscount(addOnDiscountFilterDTO);
		            log.info("AddOnDiscountDataDTO fetched successfully for  Institute ID: {}", addOnDiscountFilterDTO.getInstituteId());
		           
		            	return ResponseEntity.ok(listaddOnDiscountDataDTO) ; 
		          
		        } catch (RecordNotFoundException e) {
		            log.error("Record not found: {}", e.getMessage());
		            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		        } catch (Exception e) {
		            log.error("An error occurred while fetching Add On Discount with  Institute ID: {}" ,addOnDiscountFilterDTO.getInstituteId(), e);
		            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, AppConstants.FAILED_TO_FETCH_DATA.getMessage(), e);
		        }
		    }
		 
		 @DeleteMapping("/deactivateAddOnDiscount/{id}/{instituteId}")
		    public Response<?> deactivateAddOnDiscount(
		            @PathVariable Long id,
		            @PathVariable Long instituteId) {
		        Response<?> response = financeService.deActivateAddOnDiscount(id, instituteId);
		        return response;
		    }
		 
		 
		 @PostMapping("/addOrUpdate/profileDiscount")
		 public ResponseEntity<Response<?>> addOrUpdateprofileDiscount(@RequestBody ProfileDiscountDataDTO profileDiscountDataDTO) {
		     log.info("Received request to add/update profileDiscount for Institute ID: {}", profileDiscountDataDTO.getFkInstituteId());
		     try {
		         Response<?> response = financeService.addOrUpdateProfileDiscount(profileDiscountDataDTO);
		         return new ResponseEntity<>(response, HttpStatus.OK);
		     } catch (Exception e) {
		         log.error("Error occurred while processing profile Discount: ", e);
		         Response<String> errorResponse = new Response<>( HttpStatus.INTERNAL_SERVER_ERROR.value(),AppConstants.ADD_ON_DISCOUNT_FAILURE.getMessage(), e.getMessage());
		         return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		     }
		 }
		 
		 @GetMapping("/getProfileDiscountById/{id}/{instituteId}")
		    public ResponseEntity<Response<ProfileDiscountDataDTO>> getProfileDiscountById(@PathVariable Long id, @PathVariable Long instituteId) {
		        try {
		            log.info("Received request to fetch Profile Discount with ID: {} and Institute ID: {}", id, instituteId);
		            ProfileDiscountDataDTO profileDiscountDataDTO = financeService.getProfileDiscountsById(id, instituteId);
		            log.info("ProfileDiscountDataDTO fetched successfully for ID: {} and Institute ID: {}", id, instituteId);
		            return ResponseEntity.ok(new Response(HttpStatus.OK.value(),AppConstants.DATA_RERIEVED_SUCCESSFULLY.getMessage(),profileDiscountDataDTO) );
		        } catch (RecordNotFoundException e) {
		            log.error("Record not found: {}", e.getMessage());
		            throw e;
		        } catch (Exception e) {
		            log.error("An error occurred while fetching Aprofile Discount with ID: {} and Institute ID: {}", id, instituteId, e);
		            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, AppConstants.FAILED_TO_FETCH_DATA.getMessage(), e);
		        }
		    }
		 
		 @GetMapping("/getAllProfileDiscount/{instituteId}")
		    public ResponseEntity<Response<ProfileDiscountDataDTO>> getAllProfileDiscount(@PathVariable Long instituteId) {
		      //  try {
		            log.info("Received request to fetch Profile Discount with  Institute ID: {}", instituteId);
		            List<ProfileDiscountGetDTO> listProfileDiscountDataDTO = financeService.getAllProfileDiscounts(instituteId);
		            log.info("AddOnDiscountDataDTO fetched successfully for  Institute ID: {}", instituteId);
		            if(!(listProfileDiscountDataDTO==null)) {
		            	return ResponseEntity.ok(new Response(AppConstants.SUCCESS_CODE.getCode(),AppConstants.DATA_RERIEVED_SUCCESSFULLY.getMessage(),listProfileDiscountDataDTO) ); 
		            }else {
		            return ResponseEntity.ok(new Response(AppConstants.FAILURE_CODE.getCode(),AppConstants.DATA_NOT_FOUND.getMessage()+ instituteId,listProfileDiscountDataDTO) );
		            }
		       /* } catch (RecordNotFoundException e) {
		            log.error("Record not found: {}", e.getMessage());
		            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		        } catch (Exception e) {
		            log.error("An error occurred while fetching  Profile Discount with  Institute ID: {}" ,instituteId, e);
		            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, AppConstants.FAILED_TO_FETCH_DATA.getMessage(), e);
		        }*/
		    }
		 
		 @DeleteMapping("/deactivateProfileDiscount/{id}/{instituteId}")
		    public Response<?> deactivateProfileDiscount(
		            @PathVariable Long id,
		            @PathVariable Long instituteId) {
		        Response<?> response = financeService.deActivateProfileDiscount(id, instituteId);
		        return response;
		    }

	@PostMapping("/addOrUpdate/cashier")
	public ResponseEntity<Response<Object>> addOrUpdateCashier(@RequestBody CashierDataDTO cashierDataDTO) {
		log.info("Received request to add/update cashier for Institute ID: {}", cashierDataDTO.getInstituteId());


		Response<?> response = financeService.addOrUpdateCashier(cashierDataDTO);


		if (response.getCode() == 0) {
			return ResponseEntity.ok(new Response<>(0, "Successfully added/updated Cashier data", null));
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<>(1, "Failed to add/update Cashier data", response.getMessage()));
		}
	}
		 
		 @GetMapping("/api/user/{instituteId}/{id}")
		    public StudentInfoDTO getUserRoles(@PathVariable Long id ,@PathVariable Long instituteId) {
		        return webClientService.getStudentInfo(instituteId, id); 
		    }
		 
		 @GetMapping("/getAllAddOndiscount/{instituteId}")
		    public ResponseEntity<Response<ProfileDiscountDataDTO>> getAlldiscount(@PathVariable Long instituteId) {
		        try {
		            log.info("Received request to fetch Profile Discount with  Institute ID: {}", instituteId);
		            List<AddOnDiscountResponseDTO> listProfileDiscountDataDTO = financeService.getAllAddOnDiscountData(instituteId);
		            log.info("AddOnDiscountDataDTO fetched successfully for  Institute ID: {}", instituteId);
		            if(!(listProfileDiscountDataDTO==null)) {
		            	return ResponseEntity.ok(new Response(HttpStatus.OK.value(),AppConstants.DATA_RERIEVED_SUCCESSFULLY.getMessage(),listProfileDiscountDataDTO) ); 
		            }else {
		            return ResponseEntity.ok(new Response(HttpStatus.NOT_FOUND.value(),AppConstants.DATA_NOT_FOUND.getMessage()+ instituteId,listProfileDiscountDataDTO) );
		            }
		        } catch (RecordNotFoundException e) {
		            log.error("Record not found: {}", e.getMessage());
		            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
		        } catch (Exception e) {
		            log.error("An error occurred while fetching  Profile Discount with  Institute ID: {}" ,instituteId, e);
		            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, AppConstants.FAILED_TO_FETCH_DATA.getMessage(), e);
		        }
		    }
		 
		 @GetMapping("/getDiscountsByStudentId/{studentId}/{instituteId}")
		 public ResponseEntity<Response<List<DiscountResponseDTO>>> getDiscountsByStudentId(@PathVariable Long studentId, @PathVariable Long instituteId) {
		     try {
		         log.info("Received request to fetch discounts for Student ID: {} and Institute ID: {}", studentId, instituteId);
		         List<DiscountResponseDTO> discountData = financeService.getAllDiscountsByStudentId(studentId, instituteId);
		         log.info("Discounts fetched successfully for Student ID: {} and Institute ID: {}", studentId, instituteId);
		         if(!discountData.isEmpty()) {
		         return ResponseEntity.ok(new Response<>(HttpStatus.OK.value(), "Discounts fetched successfully", discountData));
		         }else {
		         log.error("No discounts found for Student ID: {} and Institute ID: {}", studentId, instituteId);
		         return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                 .body(new Response<>(HttpStatus.NOT_FOUND.value(), AppConstants.RECORD_NOT_FOUND.getMessage() + " for Student ID: " + studentId + " and Institute ID: " + instituteId, null));
		         }
		     }catch (Exception e) {
		         log.error("An error occurred while fetching discounts for Student ID: {} and Institute ID: {}", studentId, instituteId, e);
		         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                 .body(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PROCESSING_ERROR.getMessage(), null));
		     }
		 }

		 
		 

	@GetMapping("/getAllbankdetails/{instituteId}")
	public ResponseEntity<?> getAllbankdetails(
			@PathVariable Long instituteId) {
		try {
			List<BankDTO> bankDTO = financeService.getBankDetails(instituteId);
			if (bankDTO.isEmpty()) {
				return new ResponseEntity<>("No fee type master records found.", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(bankDTO, HttpStatus.OK);
		} catch (Exception e) {
			log.error("An error occurred while fetching bankdetail data", e);
			return new ResponseEntity<>(AppConstants.PROCESSING_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getBankDetailsById")
	public ResponseEntity<BankDTO> getBankDetailsById(
			@RequestParam("accSetupId") Long accSetupId,
			@RequestParam("instituteId") Long instituteId) {

		Optional<BankDTO> bankDetailsOpt = financeService.getBankDetailsById(accSetupId, instituteId);

		return bankDetailsOpt
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	@PostMapping("/saveOrUpdateBankDetails")
	public ResponseEntity<BankDTO> saveOrUpdateBankDetails(@RequestBody BankDTO bankDTO) {
		log.info("Received request to save or update bank details for accSetupId: {}", bankDTO.getAccSetupId());

		BankDTO resultDTO = financeService.saveOrUpdateBankDetails(bankDTO);

		if (resultDTO != null) {
			if (bankDTO.getAccSetupId() != null) {
				log.info("Successfully updated bank details for accSetupId: {}", resultDTO.getAccSetupId());
				return ResponseEntity.ok(resultDTO);
			} else {
				log.info("Successfully created new bank details with accSetupId: {}", resultDTO.getAccSetupId());
				return ResponseEntity.status(HttpStatus.CREATED).body(resultDTO);
			}
		} else {
			log.warn("Bank details with accSetupId: {} not found, update failed.", bankDTO.getAccSetupId());
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/deactivateBank/{id}/{instituteId}")
	public Response<?> deactivateBank(
			@PathVariable Long id,
			@PathVariable Long instituteId) {
		Response<?> response = financeService.deactivateBank(id, instituteId);
		return response;
	}
	
	@PostMapping("/add-or-update/PaymentModeConfig")
    public ResponseEntity<?> addOrUpdatePaymentModeConfig(@RequestBody List<PaymentModeConfigDTO> listPaymentModeConfigDTO) {
        try {
            log.info("Received request to add or update Payment Mode Config.");
            Response<?> response = financeService.addOrUpdatePaymentModeConfig(listPaymentModeConfigDTO);
            log.info("Payment Mode Config processed successfully.");
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
        } catch (Exception e) {
            log.error("Error occurred while adding or updating Payment Mode Config: ", e);
            return new ResponseEntity<>(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to Save PaymentModeConfig" ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	

	    @GetMapping("/get-by-id/PaymentModeConfig/{id}/{instituteId}")
	    public ResponseEntity<?> getPaymentModeConfigById(
	            @PathVariable Long id,
	            @PathVariable Long instituteId) {

	        log.info("Received request to get Payment Mode Config with ID: {} and Institute ID: {}", id, instituteId);

	        try {
	            Response<?> response = financeService.getPaymentModeConfigById(id, instituteId);

	            HttpStatus status = HttpStatus.valueOf(response.getCode());
	            log.info("Response status: {} - {}", status, response.getMessage());

	           return  ResponseEntity.ok(response);
	        } catch (Exception e) {
	            log.error("Error occurred while retrieving Payment Mode Config: ", e);
	            return new ResponseEntity<>(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while processing the request. Please try again later."), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	    @GetMapping("/get-all/PaymentModeConfig/{instituteId}")
	    public ResponseEntity<?> getAllPaymentModeConfig(@PathVariable Long instituteId) {
	        log.info("Received request to get all Payment Mode Configs for Institute ID: {}", instituteId);

	        try {
	            Response<?> response = financeService.getAllPaymentModeConfig(instituteId);
	            return  ResponseEntity.ok(response);
	        } catch (Exception e) {
	            log.error("Error occurred while retrieving all Payment Mode Configs: ", e);
	            return new ResponseEntity<>(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while processing the request. Please try again later."), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	    @DeleteMapping("/deactivate/{id}/{instituteId}")
	    public ResponseEntity<?> deactivatePaymentModeConfig(
	            @PathVariable Long id,
	            @PathVariable Long instituteId) {

	        log.info("Received request to deactivate Payment Mode Config with ID: {} and Institute ID: {}", id, instituteId);

	        try {
	            Response<?> response = financeService.deactivatePaymentModeconfig(id, instituteId);

	           
	            return  ResponseEntity.ok(response);
	        } catch (Exception e) {
	            log.error("Error occurred while deactivating Payment Mode Config: ", e);
	            return new ResponseEntity<>(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while processing the request. Please try again later."), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	    @GetMapping("/get-all/DefaultFeeSettingsConfig")
	    public ResponseEntity<?> getAllDefaultFeeSettingsConfig(@RequestParam(defaultValue = "0") Long instituteId) {
	        log.info("Received request to fetch all Default Fee Settings Config for Institute ID: {}", instituteId);

	        try {
	            Response<?> response = financeService.getAllDefaultFeeSettingsConfig(instituteId);

	            return  ResponseEntity.ok(response);
	        } catch (Exception e) {
	            log.error("Error occurred while fetching Default Fee Settings Config: ", e);
	            return new ResponseEntity<>(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                    "An error occurred while processing the request. Please try again later."),
	                    HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    @PostMapping("/add_or_update/DefaultFeeSettingsConfig")
	    public ResponseEntity<?> addOrUpdateDefaultFeeSettingsConfig(@RequestBody DefaultFeeSettingsConfigDTO defaultFeeSettingsConfigDTO) {
	        log.info("Received request to save or update DefaultFeeSettingsConfig for Institute ID: {}", defaultFeeSettingsConfigDTO.getInstituteId());

	        Response<?> response = financeService.addOrUpdateDefaultFeeSettingsConfig(defaultFeeSettingsConfigDTO);

	        if (AppConstants.SUCCESS_CODE.getCode() == response.getCode()) {
	            log.info("DefaultFeeSettingsConfig saved/updated successfully for Institute ID: {}", defaultFeeSettingsConfigDTO.getInstituteId());
	            return ResponseEntity.ok(response);
	        } else {
	            log.error("Failed to save/update DefaultFeeSettingsConfig for Institute ID: {}", defaultFeeSettingsConfigDTO.getInstituteId());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }
	    
	    @PostMapping("/add_or_update/ReminderConfig")
	    public ResponseEntity<?> addOrUpdateReminderConfig(@RequestBody List<ReminderConfigDTO> listDTO) {
	        log.info("Received request to save or update {} ReminderConfig entities", listDTO.size());

	        Response<?> response = financeService.addOrUpdateReminderConfig(listDTO);

	        if (AppConstants.SUCCESS_CODE.getCode() == response.getCode()) {
	            log.info("Successfully saved/updated {} ReminderConfig entities", listDTO.size());
	            return ResponseEntity.ok(response);
	        } else {
	            log.error("Failed to save/update ReminderConfig entities. Error: {}", response.getData());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }
	    
	    @GetMapping("/get-by-id/ReminderConfig/{id}/{instituteId}")
	    public ResponseEntity<?> getReminderConfigById(
	            @PathVariable Long id,
	            @PathVariable Long instituteId) {
	        log.info("Received request to get ReminderConfig with ID: {} and Institute ID: {}", id, instituteId);
	        try {
	            Response<?> response = financeService.getReminderConfigById(id, instituteId);
	            return  ResponseEntity.ok(response);
	        } catch (Exception e) {
	            log.error("Error occurred while retrieving ReminderConfig: ", e);
	            return new ResponseEntity<>(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while processing the request. Please try again later."), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	    @GetMapping("/get-all/ReminderConfig/{instituteId}")
	    public ResponseEntity<?> getAllReminderConfig(@PathVariable Long instituteId) {
	        log.info("Received request to fetch all ReminderConfigs for Institute ID: {}", instituteId);
	        try {
	            Response<?> response = financeService.getAllReminderConfig(instituteId);
	            HttpStatus status = HttpStatus.valueOf(response.getCode());
	            log.info("Response status: {} - {}", status, response.getMessage());
	            return new ResponseEntity<>(response, status);
	        } catch (Exception e) {
	            log.error("Error occurred while fetching all ReminderConfigs: ", e);
	            return new ResponseEntity<>(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                    "An error occurred while processing the request. Please try again later."),
	                    HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
	    
	    @DeleteMapping("/deactivate/ReminderConfig/{id}/{instituteId}")
	    public ResponseEntity<?> deactivateReminderConfig(
	            @PathVariable Long id,
	            @PathVariable Long instituteId) {
	        log.info("Received request to deactivate ReminderConfig with ID: {} and Institute ID: {}", id, instituteId);
	        try {
	            Response<?> response = financeService.deactivateReminderConfig(id, instituteId);
	            return  ResponseEntity.ok(response);
	        } catch (Exception e) {
	            log.error("Error occurred while deactivating ReminderConfig with ID: {} and Institute ID: {}", id, instituteId, e);
	            return new ResponseEntity<>(new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                    "An error occurred while processing the request. Please try again later."),
	                    HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	


//	@GetMapping("/getCashierDataByInstituteId")
//	public ResponseEntity<List<CashierDataDTO>> getCashierDataByInstituteId(@RequestParam Long instituteId) {
//		List<CashierDataDTO> cashierData = financeService.getCashierDataByInstituteId(instituteId);
//
//		if (cashierData.isEmpty()) {
//			return ResponseEntity.noContent().build();
//		} else {
//			return ResponseEntity.ok(cashierData);
//		}
//	}

/*
	@DeleteMapping("/deactivateCashier/{id}/{instituteId}")
	public ResponseEntity<?> deactivateFinanceAccountSetup(@PathVariable Long id, @PathVariable Long instituteId) {
		try {

			financeService.deactivateFinanceAccountSetupById(id, instituteId);
			return ResponseEntity.ok("Successfully deactivated FinanceAccountSetup");
		} catch (EntityNotFoundException e) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to deactivate FinanceAccountSetup");
		}
	}
	
	*/



	@DeleteMapping("/deactivateCashier/{id}/{instituteId}")
	public ResponseEntity<Response<Object>> deactivateFinanceAccountSetup(
			@PathVariable Long id,
			@PathVariable Long instituteId) {
		try {
			financeService.deactivateFinanceAccountSetupById(id, instituteId);
			return ResponseEntity.ok(new Response<>(0, "Successfully deactivated FinanceAccountSetup and associated configurations", null));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new Response<>(1, e.getMessage(), null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new Response<>(1, "Failed to deactivate FinanceAccountSetup", e.getMessage()));
		}
	}



	@GetMapping("/cashier/{instituteId}")
	public ResponseEntity<ResponseData<List<FinanceAccountSetupDTO>>> getAllAccountSetupsWithConfigs(@PathVariable("instituteId") Long instituteId){
	
		try {
		List<FinanceAccountSetupDTO> setupsWithConfigs = financeService.getFinanceAccountSetupsWithConfigs(instituteId);
        if (setupsWithConfigs != null && !setupsWithConfigs.isEmpty()) {
        	return ResponseEntity.ok(new ResponseData<>(0, "Successfully retrieved the data", setupsWithConfigs));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseData<>(1, "No finance account setups found", null));
        }
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseData<>(1, "Failed to retrieve data", null));
		}
	}

	@GetMapping("/getAllCashier/Access")
	public ResponseEntity<ResponseData<List<FeeConfig>>> getAllFeeConfig(@RequestParam(name = "type", required = false) String type) {
		List<FeeConfig> list = financeService.getAllFeeConfig(type);
		if (!list.isEmpty()) {
			return ResponseEntity.ok(new ResponseData<>(0, "Success", list));  // Successful response with code 0
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseData<>(1, "No Fee Config data found", null));  // Not found response with code 1
		}
	}
	


	@GetMapping("/getAllCashierData/{instituteId}/{accSetupId}")
    public ResponseEntity<ResponseData<CashierDataDTO>> getCashierByAccSetupId(
            @PathVariable("instituteId") Long instituteId, 
            @PathVariable("accSetupId") Long accSetupId) {
        try {
            CashierDataDTO cashierDataDTO = financeService.getCashierByAccSetupId(instituteId, accSetupId);
            return ResponseEntity.ok(new ResponseData<>(0, "Retrieve Data Successfully", cashierDataDTO));
        } catch (RecordNotFoundException rnf) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData<>(0, rnf.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseData<>(1, "Failed to Retrieve", null));
        }
       }
	
	   @GetMapping("/getFeeAmountByFeeHead/{instituteId}/{feeHeadId}")
	    public ResponseEntity<?> getFeeAmountByFeeHead(@PathVariable Long instituteId,@PathVariable Long feeHeadId) {
	       
	            Double response = financeService.getFeeAmountByFeeHead(instituteId, feeHeadId);
	            return ResponseEntity.ok(new ResponseData<>(AppConstants.SUCCESS_CODE.getCode(), "Success", response));
	   }

	   @GetMapping("/distinct/FeeHeads/{instituteId}")
	    public ResponseEntity<?> getDistinctFeeHeads(@PathVariable Long instituteId) {
	        log.info("Request received to get distinct fee heads for instituteId: {}", instituteId);
	        try {
	            List<FeeHeadDetailsDTO> feeHeadDetails = financeService.getAllDistinctFeeHeadsInFeeSetup(instituteId);
	            log.info("Successfully fetched fee heads for instituteId: {}", instituteId);
	            return ResponseEntity.status(HttpStatus.CREATED)
		                 .body(new Response<>(AppConstants.SUCCESS_CODE.getCode(), AppConstants.DATA_RERIEVED_SUCCESSFULLY.getMessage(), feeHeadDetails));
		     
	        } catch (Exception e) {
	            log.error("Error while fetching distinct fee heads for instituteId: {}", instituteId, e);
	            return ResponseEntity.status(HttpStatus.CREATED)
		                 .body(new Response<>(AppConstants.FAILURE_CODE.getCode(), AppConstants.DATA_NOT_FOUND.getMessage()+instituteId , null)); 
	        }
	    }

	@GetMapping("/getCashierTree/{accSetupId}/{instituteId}")
	public ResponseEntity<?> getCashierTreeStructure(@PathVariable("accSetupId") Long accSetupId, @PathVariable("instituteId") Long instituteId) {
		List<CashierDataDTO> cashierData = financeService.getCashierByAccSetupAndInstitute(accSetupId, instituteId);
		return ResponseEntity.ok(new ResponseData<>(0, "success", cashierData));
	}


}
