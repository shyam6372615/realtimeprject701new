package com.sparrow.Finance.service;


import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparrow.Finance.Exception.DiscountRetrievalException;
import com.sparrow.Finance.Exception.InternalServerException;
import com.sparrow.Finance.Exception.RecordNotFoundException;
import com.sparrow.Finance.constants.AppConstants;
import com.sparrow.Finance.dto.AccessDTO;
import com.sparrow.Finance.dto.AccessDataDTO;
import com.sparrow.Finance.dto.AddOnDiscountDTO;
import com.sparrow.Finance.dto.AddOnDiscountDataDTO;
import com.sparrow.Finance.dto.AddOnDiscountDataResponseDTO;
import com.sparrow.Finance.dto.AddOnDiscountFilterDTO;
import com.sparrow.Finance.dto.AddOnDiscountResponseDTO;
import com.sparrow.Finance.dto.ApiResponseDTO;
import com.sparrow.Finance.dto.BankDTO;
import com.sparrow.Finance.dto.CashierDTO;
import com.sparrow.Finance.dto.CashierDataDTO;
import com.sparrow.Finance.dto.DefaultFeeSettingsConfigDTO;
import com.sparrow.Finance.dto.DiscountResponseDTO;
import com.sparrow.Finance.dto.FeeConfigOutputDTO;
import com.sparrow.Finance.dto.FeeHeadDetailsDTO;
import com.sparrow.Finance.dto.FeeHeadMasterDTO;
import com.sparrow.Finance.dto.FeeHeadMasterOutputDTO;
import com.sparrow.Finance.dto.FeeMasterDetailsInputsDTO;
import com.sparrow.Finance.dto.FeeMasterSetupInputsDTO;
import com.sparrow.Finance.dto.FeeSetUpResponseDTO;
import com.sparrow.Finance.dto.FeeSetUpTypeResponseDTO;
import com.sparrow.Finance.dto.FeeSetupInputsDTO;
import com.sparrow.Finance.dto.FeeTypeMasterDTO;
import com.sparrow.Finance.dto.FinanceAccountSetupDTO;
import com.sparrow.Finance.dto.FinanceAccountsConfigDTO;
import com.sparrow.Finance.dto.PaymentModeConfigDTO;
import com.sparrow.Finance.dto.ProfileDiscountDTO;
import com.sparrow.Finance.dto.ProfileDiscountDataDTO;
import com.sparrow.Finance.dto.ProfileDiscountGetDTO;
import com.sparrow.Finance.dto.ReminderConfigDTO;
import com.sparrow.Finance.dto.Response;
import com.sparrow.Finance.dto.RolePrivilegeDTO;
import com.sparrow.Finance.dto.StudentCategoryDTO;
import com.sparrow.Finance.dto.StudentCourseDTO;
import com.sparrow.Finance.dto.StudentInfoDTO;
import com.sparrow.Finance.dto.StudentResponse.Student;
import com.sparrow.Finance.dto.UserRolePrivilegeDTO;
import com.sparrow.Finance.dto.cashierMappingDTO;
import com.sparrow.Finance.feignclient.StudentProfileDiscountClient;
import com.sparrow.Finance.feignclient.StudentServiceFeignClient;
import com.sparrow.Finance.feignclient.WebClientService;
import com.sparrow.finance.entity.AddOnDiscount;
import com.sparrow.finance.entity.DefaultFeeSettingsConfig;
import com.sparrow.finance.entity.FeeConfig;
import com.sparrow.finance.entity.FeeHeadMaster;
import com.sparrow.finance.entity.FeeMasterDetails;
import com.sparrow.finance.entity.FeeMasterSetup;
import com.sparrow.finance.entity.FeeTypeMaster;
import com.sparrow.finance.entity.FinanceAccountSetup;
import com.sparrow.finance.entity.FinanceAccountsConfig;
import com.sparrow.finance.entity.PaymentModeConfig;
import com.sparrow.finance.entity.ProfileDiscount;
import com.sparrow.finance.entity.ReminderConfig;
import com.sparrow.finance.repository.IAddOnDiscountRepo;
import com.sparrow.finance.repository.IDefaultFeeSettingsConfigRepo;
import com.sparrow.finance.repository.IFeeConfigRepo;
import com.sparrow.finance.repository.IFeeHeadMasterRepo;
import com.sparrow.finance.repository.IFeeMasterDetailsRepo;
import com.sparrow.finance.repository.IFeeMasterSetupRepo;
import com.sparrow.finance.repository.IFeeTypeMasterRepo;
import com.sparrow.finance.repository.IFinanceAccountSetupRepo;
import com.sparrow.finance.repository.IFinanceAccountsConfigRepo;
import com.sparrow.finance.repository.IProfileDiscountRepo;
import com.sparrow.finance.repository.PaymentModeConfigRepo;
import com.sparrow.finance.repository.ReminderConfigRepo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class FinanceServiceImpl implements IFinanceSevice {

    @Autowired
    private IFeeMasterDetailsRepo iFeeMasterDetailsRepo;

    @Autowired
    private IFeeMasterSetupRepo iFeeMasterSetupRepo;
    @Autowired
    private IFeeTypeMasterRepo  iFeeTypeMasterRepo;
    @Autowired
	private IFinanceAccountSetupRepo financeAccountSetupRepo;
	@Autowired
	private IFinanceAccountsConfigRepo iFinanceAccountsConfigRepo;
	@Autowired
	private IFeeConfigRepo iFeeConfigRepo;
	@Autowired
	 private StudentServiceFeignClient studentServiceFeignClient;
	@Autowired
	private IFeeHeadMasterRepo iFeeHeadMasterRepo;
	@Autowired
	private IAddOnDiscountRepo iAddOnDiscountRepo;
	@Autowired
	private IProfileDiscountRepo iProfileDiscountRepo;
    @Autowired
    private IFinanceAccountSetupRepo iFinanceAccountSetupRepo;
    @Autowired
	private WebClientService webClientService;
    @Autowired
    private PaymentModeConfigRepo paymentModeConfigRepo;
    @Autowired
    private IDefaultFeeSettingsConfigRepo defaultFeeSettingsConfigRepo;
    @Autowired
    private ReminderConfigRepo reminderConfigRepo;
    @Autowired
    private StudentProfileDiscountClient studentProfileDiscountClient;
    
    

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Response<?> addOrUpdateFeeSetup(FeeSetupInputsDTO feeSetupInputsDTO) {

    	if(feeSetupInputsDTO.getFeeMasterSetupInputsDTO().getFeeMasterSetupId()==null) {
    	if(iFeeMasterDetailsRepo.existsByFeeHeadIdAndInstituteIdAndIsActiveTrue(
    			feeSetupInputsDTO.getFeeMasterSetupInputsDTO().getFeeMasterDetailsInputsDTO()
    			.get(0).getFeeHeadId(), feeSetupInputsDTO.getInstituteId())) {
    		return new Response<>(AppConstants.DUPLICATE_CODE.getCode(),AppConstants.DUPLICATE_FEE_HEAD.getMessage()); 
    	}
    	}

        Long feeMasterSetupId = feeSetupInputsDTO.getFeeMasterSetupInputsDTO().getFeeMasterSetupId();
        
        List<FeeMasterDetailsInputsDTO> dtoList = feeSetupInputsDTO.getFeeMasterSetupInputsDTO().getFeeMasterDetailsInputsDTO();
        
        try {
            FeeMasterSetup feeMasterSetup = convertFeeMasterSetupInputsDTOToEntity(
                    feeSetupInputsDTO.getFeeMasterSetupInputsDTO(), 
                    feeSetupInputsDTO.getInstituteId());

            feeMasterSetup = iFeeMasterSetupRepo.save(feeMasterSetup);
            log.info("FeeMasterSetup saved for Institute ID: {}", feeSetupInputsDTO.getInstituteId());

            Long feeMasterSetupIdFinal = feeMasterSetup.getFeeMasterSetupId();

            if (dtoList != null && feeMasterSetupId != null) {
                List<FeeMasterDetails> existingEntities = iFeeMasterDetailsRepo
                        .findByFkFeeMasterSetupIdAndInstituteIdAndIsActiveTrue(feeMasterSetupId, feeSetupInputsDTO.getInstituteId());
                
                Set<Long> existingIds = existingEntities.stream()
                        .map(FeeMasterDetails::getFeeMasterDetailsId)
                        .collect(Collectors.toSet());

                Set<Long> updatedIds = dtoList.stream()
                        .map(FeeMasterDetailsInputsDTO::getFeeMasterDetailsId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet());

                existingEntities.stream()
                        .filter(entity -> !updatedIds.contains(entity.getFeeMasterDetailsId()))
                        .forEach(iFeeMasterDetailsRepo::delete);
                log.info(" FeeMasterDetails deleted for Institute ID: {}", feeSetupInputsDTO.getInstituteId());
            }

            
            List<FeeMasterDetails> feeMasterDetailsList = dtoList.stream()
                    .map(dto -> {
                        FeeMasterDetails details = convertFeeMasterDetailsInputsDTOtoEntity(dto, feeSetupInputsDTO.getInstituteId());
                        details.setFkFeeMasterSetupId(feeMasterSetupIdFinal); 
                        return details;
                    })
                    .collect(Collectors.toList());

            iFeeMasterDetailsRepo.saveAllAndFlush(feeMasterDetailsList);
            log.info("FeeMasterDetails saved successfully for Institute ID: {}", feeSetupInputsDTO.getInstituteId());

            return new Response<>(AppConstants.SUCCESS_CODE.getCode(), AppConstants.FEE_SETUP_SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while processing fee setup for Institute ID: {}", feeSetupInputsDTO.getInstituteId(), e);
            return new Response<>(AppConstants.SERVER_ERROR_CODE.getCode(), AppConstants.FEE_SETUP_FAILURE.getMessage(), e.getMessage());
        }
    }


    @Override
	public Response<?> getAllFeeSetup(Long instituteId, String feeType) {
    	FeeMasterSetup feeMasterSetup=new FeeMasterSetup();
    	feeMasterSetup.setCreatedBy(null);
    	feeMasterSetup.setUpdatedBy(null);
    	if(instituteId != null && instituteId > 0) {
    		feeMasterSetup.setInstituteId(instituteId);
    	}
    	if(!feeType.equalsIgnoreCase("none")) {
    		feeMasterSetup.setFeeType(feeType);
    		}
    	Example example=Example.of(feeMasterSetup);
    	
    	List<FeeMasterSetup> listData =iFeeMasterSetupRepo.findAll(example);
    	List<FeeSetUpTypeResponseDTO> listFeeSetUpTypeResponseDTO=listData.stream().map(feeMasterSetupData->{
    	return convertFeeSetUpTypeResponseDTOToEntity(feeMasterSetupData);
    	}).collect(Collectors.toList());
    	
    	if(listFeeSetUpTypeResponseDTO.isEmpty()) {
    		return new Response<>(AppConstants.FAILURE_CODE.getCode(),AppConstants.RECORD_NOT_FOUND.getMessage()+instituteId,null);
    		
    	}
		return new Response<>(AppConstants.SUCCESS_CODE.getCode(),AppConstants.RECORD_FOUND.getMessage(),listFeeSetUpTypeResponseDTO);
	}
    
    
    private FeeSetUpTypeResponseDTO convertFeeSetUpTypeResponseDTOToEntity(FeeMasterSetup feeMasterSetup){
    	
    	List<FeeMasterDetails> listFeeMasterDetailsData = iFeeMasterDetailsRepo.
    			findByFkFeeMasterSetupIdAndInstituteIdAndIsActiveTrue(feeMasterSetup.getFeeMasterSetupId(), feeMasterSetup.getInstituteId());
    	
    	List<FeeSetUpResponseDTO> listFeeSetUpResponseDTO=listFeeMasterDetailsData.stream().map(feeMasterDetailsData->{
    		
    		FeeSetUpResponseDTO eeSetUpResponseDTO=FeeSetUpResponseDTO.builder()
    				.dueDate(feeMasterDetailsData.getDuePaymentDay())
    				.feeAmount(feeMasterDetailsData.getFeeAmount())
    				.feeHeadName(feeMasterDetailsData.getFeeHeadName())
    				.lateFeeAmountOrRate(feeMasterDetailsData.getLateFeeAmountOrRate())
    				.lateFeeType(feeMasterDetailsData.getLateFeeType())
    				.occurance(feeMasterDetailsData.getOccurance())
    				.tax(feeMasterDetailsData.getTax())
    				.totalAmount(feeMasterDetailsData.getFeeAmount())
    				.feeDetailsId(feeMasterDetailsData.getFeeMasterDetailsId())
    				.build();
    				return eeSetUpResponseDTO;
    		
    	}).collect(Collectors.toList());
    	List<String> list=new ArrayList<>();
    	if(feeMasterSetup.getIsNew()) {
    		list.add("new");
    	}
    	if(feeMasterSetup.getIsExisting()) {
    		list.add("Existing");
    	}
    	List<StudentCategoryDTO> studentCategory = convertJsonToListOfString(feeMasterSetup.getStudentCategory());
    	
    	return FeeSetUpTypeResponseDTO.builder()
    			.appliedStudents(0l)
    			.enrolledStatus(list)
    			.feeGroupTittle(feeMasterSetup.getFeeGroupTitle())
    			.studentCatyegory(studentCategory)
    			.feeSetupResponseDTO(listFeeSetUpResponseDTO)
    			.instituteId(feeMasterSetup.getInstituteId())
    			.feeSetupId(feeMasterSetup.getFeeMasterSetupId())
    			.build();
    	
    	
    }
    
    
    @Override
    public Response<?> getFeeSetupById(Long id, Long instituteId) throws Exception{
        log.info("Fetching FeeMasterSetup with ID: {} and Institute ID: {}", id, instituteId);
        FeeMasterSetup feeMasterSetup;
        
            Optional<FeeMasterSetup> optional = iFeeMasterSetupRepo.findByFeeMasterSetupIdAndInstituteIdAndIsActiveTrue(id, instituteId);
            if(optional.isPresent()) {
            	feeMasterSetup=optional.get();

            log.debug("FeeMasterSetup fetched successfully: {}", feeMasterSetup);
            FeeMasterSetupInputsDTO feeMasterSetupInputsDTO=convertFeeMasterSetupToDTO(feeMasterSetup);

            log.debug("FeeMasterSetupInputsDTO built successfully: {}", feeMasterSetupInputsDTO);

            List<FeeMasterDetails> listData = iFeeMasterDetailsRepo.findByFkFeeMasterSetupIdAndInstituteIdAndIsActiveTrue(id, instituteId);
            log.info("FeeMasterDetails records fetched: {}", listData.size());
            List<FeeMasterDetailsInputsDTO> listFeeMasterDetailsInputsDTO=null;
        if(!listData.isEmpty()) {
        	listFeeMasterDetailsInputsDTO= convertFeeMasterDetailsToDTO(listData);

            log.debug("FeeMasterDetailsInputsDTO list built successfully.");
        }

            FeeSetupInputsDTO feeSetupInputsDTO = new FeeSetupInputsDTO();
            feeMasterSetupInputsDTO.setFeeMasterDetailsInputsDTO(listFeeMasterDetailsInputsDTO);
            feeSetupInputsDTO.setFeeMasterSetupInputsDTO(feeMasterSetupInputsDTO);
            feeSetupInputsDTO.setInstituteId(instituteId);

            log.info("Final FeeSetupInputsDTO prepared: {}", feeSetupInputsDTO);
            return new Response<>(AppConstants.SUCCESS_CODE.getCode(), AppConstants.DATA_RERIEVED_SUCCESSFULLY.getMessage(),feeSetupInputsDTO);
            }else {

            return new Response<>(AppConstants.FAILURE_CODE.getCode(),AppConstants.DATA_NOT_FOUND_WITH_ID.getMessage()+id);
           }
    }
    @Override
    public Response<?> deactivateFeeSetup(Long id, Long instituteId) {
        try {
            
            Optional<FeeMasterSetup> optionalFeeMasterSetup = iFeeMasterSetupRepo.findByFeeMasterSetupIdAndInstituteIdAndIsActiveTrue(id, instituteId);
            if (optionalFeeMasterSetup.isEmpty()) {
                log.warn("FeeMasterSetup record not found for ID: {}", id);
                return new Response<>(HttpStatus.NOT_FOUND.value(), AppConstants.FEE_MASTER_SETUP_NOT_FOUND.getMessage() + id);
            }

            FeeMasterSetup feeMasterSetup = optionalFeeMasterSetup.get();
            feeMasterSetup.setIsActive(false);
            iFeeMasterSetupRepo.save(feeMasterSetup);
            log.info("FeeMasterSetup with ID: {} deactivated successfully", id);

            List<FeeMasterDetails> listData = iFeeMasterDetailsRepo.findByFkFeeMasterSetupIdAndInstituteIdAndIsActiveTrue(id, instituteId);
            log.info("FeeMasterDetails records fetched: {}", listData.size());

            if (!listData.isEmpty()) {
                List<FeeMasterDetails> listFeeMasterDetails = listData.stream()
                        .peek(feeMasterDetails -> {
                            feeMasterDetails.setIsActive(false);
                            log.debug("Deactivating FeeMasterDetails with ID: {}", feeMasterDetails.getFeeMasterDetailsId());
                        })
                        .collect(Collectors.toList());
                iFeeMasterDetailsRepo.saveAll(listFeeMasterDetails);
                log.info("FeeMasterDetails records updated successfully");
            }

            return new Response<>(HttpStatus.OK.value(), "Fee Setup deactivated successfully");

        } catch (Exception e) {
            log.error("Unexpected error occurred while deactivating Fee Setup: {}", e.getMessage(), e);
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PROCESSING_ERROR.getMessage());
        }
    }
    
    





    private FeeMasterSetup convertFeeMasterSetupInputsDTOToEntity(FeeMasterSetupInputsDTO dto, Long instituteId) {
        FeeMasterSetup feeMasterSetup = dto.getFeeMasterSetupId() != null
                ? iFeeMasterSetupRepo.findById(dto.getFeeMasterSetupId())
                        .orElseThrow(() -> new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + dto.getFeeMasterSetupId()))
                : new FeeMasterSetup();

       
        try {
            feeMasterSetup.setCourseGroups(objectMapper.writeValueAsString(dto.getCourseGroups()));
        // List<String> result = objectMapper.readValue(dto.getStudentCategory(), new TypeReference<List<Student>>() {});
            String data=objectMapper.writeValueAsString(dto.getStudentCategory());
            feeMasterSetup.setStudentCategory(data);
            log.info("-----------------------{}",feeMasterSetup.getStudentCategory());
            log.info("Converted course groups and student categories to JSON.");
        } catch (JsonProcessingException e) {
            log.error("Error converting course groups or student categories to JSON", e);
        }

        feeMasterSetup.setFeeGroupTitle(dto.getFeeGroupTitle());
        feeMasterSetup.setFeeType(dto.getFeeType());
        feeMasterSetup.setInstituteId(instituteId);
        feeMasterSetup.setIsApplicableToAllYears(dto.getIsApplicableToAllYears());
        feeMasterSetup.setIsExisting(dto.getIsExisting());
        feeMasterSetup.setIsNew(dto.getIsNew());
        if(dto.getBatchYear().isPresent()&&dto.getBatchYear().get()!=null) {
        	feeMasterSetup.setBatchYear(dto.getBatchYear().get());
        	
        }else {
        	feeMasterSetup.setBatchYear(Year.now().toString());
        	
        }

        return feeMasterSetup;
    }

    private FeeMasterDetails convertFeeMasterDetailsInputsDTOtoEntity(FeeMasterDetailsInputsDTO dto, Long instituteId) {
        // Null check for the dto parameter
        if (dto == null) {
            log.error("FeeMasterDetailsInputsDTO is null. Cannot convert to entity.");
            throw new IllegalArgumentException(AppConstants.NULL_FEE_DETAILS_DTO.getMessage());
        }

        // Proceed with the existing logic
        FeeMasterDetails feeMasterDetails = dto.getFeeMasterDetailsId() != null
                ? iFeeMasterDetailsRepo.findById(dto.getFeeMasterDetailsId())
                        .orElseThrow(() -> new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + dto.getFeeMasterDetailsId()))
                : new FeeMasterDetails();

        feeMasterDetails.setDuePaymentDay(dto.getDuePaymentDay());
        feeMasterDetails.setFeeAmount(dto.getFeeAmount());
        feeMasterDetails.setFeeHeadId(dto.getFeeHeadId());
        feeMasterDetails.setFeeHeadName(dto.getFeeHeadName());
        feeMasterDetails.setFromKm(dto.getFromKm());
        feeMasterDetails.setToKm(dto.getToKm());
        feeMasterDetails.setInstituteId(instituteId);
        if(dto.getFeeMasterDetailsId() == null) {
        feeMasterDetails.setIsLateFeeApplicable(dto.getIsLateFeeApplicable());
        feeMasterDetails.setIsTerminateFeeApplicable(dto.getIsTerminateFeeApplicable());
        }
        
        feeMasterDetails.setIsTerminateFeeApplicable(dto.getIsTerminateFeeApplicable());
        feeMasterDetails.setLateFeeType(dto.getLateFeeType());
        feeMasterDetails.setTax(dto.getTax());
        
        
        feeMasterDetails.setLateFeeAmountOrRate(dto.getLateFeeAmountOrRate());
        feeMasterDetails.setOccurance(dto.getOccurance());

        return feeMasterDetails;
    }
   

   
    
    public  List<StudentCategoryDTO> convertJsonToListOfString(String jsonArray) {
        try {
            List<StudentCategoryDTO> result = objectMapper.readValue(jsonArray, new TypeReference<List<StudentCategoryDTO>>() {});
            log.info("Converted JSON to List<String>: {}", result);
            return result;
        } catch (IOException e) {
            log.error("Error converting JSON to list: {}", e.getMessage());
            return Collections.emptyList(); 
        }
    }
    
    public static List<Long> convertJsonToListOfLong(String jsonArray) {
        try {
            List<Long> result = objectMapper.readValue(jsonArray, new TypeReference<List<Long>>() {});
            log.info("Converted JSON to List<Long>: {}", result);
            return result;
        } catch (IOException e) {
            log.error("Error converting JSON to list: {}", e.getMessage());
            return Collections.emptyList(); 
        }
    }
    
    private FeeMasterSetupInputsDTO convertFeeMasterSetupToDTO(FeeMasterSetup feeMasterSetup) {
    	List<Long> courseGroups = convertJsonToListOfLong(feeMasterSetup.getCourseGroups());
    	List<StudentCategoryDTO> studentCategoryId = convertJsonToListStudentCategoryDTO(feeMasterSetup.getStudentCategory());

       return FeeMasterSetupInputsDTO.builder()
                .courseGroups(courseGroups)
                .feeGroupTitle(feeMasterSetup.getFeeGroupTitle())
                .feeMasterSetupId(feeMasterSetup.getFeeMasterSetupId())
                .feeType(feeMasterSetup.getFeeType())
                .isApplicableToAllYears(feeMasterSetup.getIsApplicableToAllYears())
                .isExisting(feeMasterSetup.getIsExisting())
                .isNew(feeMasterSetup.getIsNew())
                .studentCategory(studentCategoryId)
                .build();
    	
    }
    
    private List<StudentCategoryDTO> convertJsonToListStudentCategoryDTO(String jsonData) {
	    try {
	        List<StudentCategoryDTO> result = objectMapper.readValue(jsonData, new TypeReference<List<StudentCategoryDTO>>() {});
	        log.info("Converted JSON to List<AddOnDiscountDTO>: {}", result);
	        return result;
	    } catch (IOException e) {
	        log.error("Error converting JSON to List<AddOnDiscountDTO>: {}", e.getMessage(), e);
	        return Collections.emptyList(); 
	    }
	}
    
    private List<FeeMasterDetailsInputsDTO> convertFeeMasterDetailsToDTO(List<FeeMasterDetails> listData) {
    	return listData.stream().map(feeMasterDetails -> {


            return FeeMasterDetailsInputsDTO.builder()
            		.duePaymentDay(feeMasterDetails.getDuePaymentDay())
                    .feeAmount(feeMasterDetails.getFeeAmount())
                    .feeHeadId(feeMasterDetails.getFeeHeadId())
                    .feeHeadName(feeMasterDetails.getFeeHeadName())
                    .feeMasterDetailsId(feeMasterDetails.getFeeMasterDetailsId())
                    .fromKm(feeMasterDetails.getFromKm())
                    .isLateFeeApplicable(feeMasterDetails.getIsLateFeeApplicable())
                    .isTerminateFeeApplicable(feeMasterDetails.getIsTerminateFeeApplicable())
                    .lateFeeAmountOrRate(feeMasterDetails.getLateFeeAmountOrRate())
                    .lateFeeType(feeMasterDetails.getLateFeeType())
                    .occurance(feeMasterDetails.getOccurance())
                    .toKm(feeMasterDetails.getToKm())
                    .build();
        }).collect(Collectors.toList());
    }
    public Response<?> addorUpdateFeeTypeMaster(List<FeeTypeMasterDTO> feeTypeMasterDTOList) {
        List<FeeTypeMaster> feeTypeMastersToSave = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        feeTypeMasterDTOList.forEach(feeTypeMasterDTO -> {
            try {
            	
            	
                if (feeTypeMasterDTO.getId() != null) {
                    FeeTypeMaster existingFeeTypeMaster = iFeeTypeMasterRepo.findByIdAndInstituteIdAndIsActiveTrue(feeTypeMasterDTO.getId(), feeTypeMasterDTO.getInstituteId())
                            .orElseThrow(() -> new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + feeTypeMasterDTO.getId()));
                    if(!(feeTypeMasterDTO.getFeeType().equalsIgnoreCase(existingFeeTypeMaster.getFeeType()))){
                    	if(iFeeTypeMasterRepo.existsByInstituteIdAndFeeTypeAndIsActiveTrue(feeTypeMasterDTO.getInstituteId(),feeTypeMasterDTO.getFeeType())) {
                    		errorMessages.add(AppConstants.DUPLICATE_FEETYPE_NAME.getMessage()+feeTypeMasterDTO.getFeeType()+" Please try with another name");
                    		return;
                    	}
                    }
                    if (Boolean.TRUE.equals(existingFeeTypeMaster.getIsDefault())) {
                    	String feeType=feeTypeMasterDTO.getFeeType();
                    
                    	if(!existingFeeTypeMaster.getFeeType().equalsIgnoreCase(feeType)){
                        errorMessages.add(AppConstants.DEFAULT_RECORD_UPDATE_NOT_ALLOWED.getMessage() + feeTypeMasterDTO.getId());
                        return; 
                    }
                    	if(existingFeeTypeMaster.getIsDefault()) {
                            feeTypeMasterDTO.setIsDefault(true);
                            log.info("feeTypeMasterDTO obj value {}",feeTypeMasterDTO);
                            }
                    }
                    
                    BeanUtils.copyProperties(feeTypeMasterDTO, existingFeeTypeMaster);
                    feeTypeMastersToSave.add(existingFeeTypeMaster);
                    log.info("Prepared FeeTypeMaster update for Institute ID: {}", feeTypeMasterDTO.getInstituteId());
                } else {
                	if(iFeeTypeMasterRepo.existsByInstituteIdAndFeeTypeAndIsActiveTrue(feeTypeMasterDTO.getInstituteId(),feeTypeMasterDTO.getFeeType())) {
                		errorMessages.add(AppConstants.DUPLICATE_FEETYPE_NAME.getMessage()+feeTypeMasterDTO.getFeeType()+" Please try with another name");
                		return;
                	}
                    FeeTypeMaster newFeeTypeMaster = new FeeTypeMaster();
                    BeanUtils.copyProperties(feeTypeMasterDTO, newFeeTypeMaster);
                    feeTypeMastersToSave.add(newFeeTypeMaster);
                    log.info("Prepared FeeTypeMaster addition for Institute ID: {}", feeTypeMasterDTO.getInstituteId());
                }
            } catch (RecordNotFoundException e) {
                errorMessages.add("Record not found for ID: " + feeTypeMasterDTO.getId());
                log.error("Record not found for FeeTypeMaster ID: {}", feeTypeMasterDTO.getId(), e);
            } catch (Exception e) {
                errorMessages.add("Error processing FeeTypeMaster for ID: " + feeTypeMasterDTO.getId());
                log.error("Error occurred while processing FeeTypeMaster with ID: {}", feeTypeMasterDTO.getId(), e);
            }
        });

        if (!feeTypeMastersToSave.isEmpty()) {
            iFeeTypeMasterRepo.saveAll(feeTypeMastersToSave);
            log.info("FeeTypeMasters saved successfully.");
        }

        if (errorMessages.isEmpty()) {
            return new Response<>(HttpStatus.OK.value(), AppConstants.FEE_TYPE_SETUP_SUCCESS.getMessage());
        } else if (errorMessages.size() == feeTypeMasterDTOList.size()) {
            return new Response<>(HttpStatus.BAD_REQUEST.value(), AppConstants.FEE_TYPE_SETUP_FAILURE.getMessage(), String.join(", ", errorMessages));
        } else {
            return new Response<>(HttpStatus.PARTIAL_CONTENT.value(), AppConstants.FEE_TYPE_SETUP_PARTIAL_SUCCESS.getMessage(), String.join(", ", errorMessages));
        }
    }
    
    public Response<?> addorUpdateFeeTypeMaster(FeeTypeMasterDTO feeTypeMasterDTO) {
        try {
            FeeTypeMaster feeTypeMaster;
            
            if (feeTypeMasterDTO.getId() != null) {
                feeTypeMaster = iFeeTypeMasterRepo.findByIdAndInstituteIdAndIsActiveTrue(feeTypeMasterDTO.getId(), feeTypeMasterDTO.getInstituteId())
                        .orElseThrow(() -> new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + feeTypeMasterDTO.getId()));
                if (Boolean.TRUE.equals(feeTypeMaster.getIsDefault()) && 
                    !feeTypeMaster.getFeeType().equalsIgnoreCase(feeTypeMasterDTO.getFeeType())) {
                    return new Response<>(HttpStatus.BAD_REQUEST.value(), AppConstants.DEFAULT_RECORD_UPDATE_NOT_ALLOWED.getMessage() + feeTypeMasterDTO.getId());
                }
                if(feeTypeMaster.getIsDefault()) {
                feeTypeMasterDTO.setIsDefault(true);
                log.info("feeTypeMasterDTO obj value {}",feeTypeMasterDTO);
                }
                BeanUtils.copyProperties(feeTypeMasterDTO, feeTypeMaster);
                log.info("Prepared FeeTypeMaster update for Institute ID: {}", feeTypeMasterDTO.getInstituteId());
            } else {
                feeTypeMaster = new FeeTypeMaster();
                
                BeanUtils.copyProperties(feeTypeMasterDTO, feeTypeMaster);
                log.info("Prepared FeeTypeMaster addition for Institute ID: {}", feeTypeMasterDTO.getInstituteId());
            }

            iFeeTypeMasterRepo.save(feeTypeMaster);
            log.info("FeeTypeMaster saved successfully for Institute ID: {}", feeTypeMasterDTO.getInstituteId());
            return new Response<>(HttpStatus.OK.value(), AppConstants.FEE_TYPE_SETUP_SUCCESS.getMessage());

        } catch (RecordNotFoundException e) {
            log.error("Record not found for FeeTypeMaster ID: {}", feeTypeMasterDTO.getId(), e);
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Record not found for ID: " + feeTypeMasterDTO.getId());
        } catch (Exception e) {
            log.error("Error occurred while processing FeeTypeMaster with ID: {}", feeTypeMasterDTO.getId(), e);
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.FEE_TYPE_SETUP_FAILURE.getMessage(), e.getMessage());
        }
    }

	@Override
	public List<FeeTypeMasterDTO> getAllFeeTypeMaster(Long instituteId) {
		List<FeeTypeMaster> listFeeTypeMaster=iFeeTypeMasterRepo.findByInstituteIdAndIsActiveTrue(instituteId);
		if(!listFeeTypeMaster.isEmpty()) {
			return listFeeTypeMaster.stream().map(feeTypeMaster->{
				FeeTypeMasterDTO feeTypeMasterDTO=new FeeTypeMasterDTO();
				BeanUtils.copyProperties(feeTypeMaster, feeTypeMasterDTO);
				return feeTypeMasterDTO;
				
			}).collect(Collectors.toList());
		}
		return null;
	}

	
	@Override
	public FeeTypeMasterDTO getFeeTypeMasterById(Long id, Long instituteId) {
	    log.info("Fetching FeeTypeMaster with ID: {} and Institute ID: {}", id, instituteId);
	    try {
	        FeeTypeMaster feeTypeMaster = iFeeTypeMasterRepo.findByIdAndInstituteIdAndIsActiveTrue(id, instituteId)
	                .orElseThrow(() -> new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + id));


	        log.debug("FeeTypeMaster fetched successfully: {}", feeTypeMaster);
	        FeeTypeMasterDTO feeTypeMasterDTO = new FeeTypeMasterDTO();
	        BeanUtils.copyProperties(feeTypeMaster, feeTypeMasterDTO);
	        return feeTypeMasterDTO;
	    } catch (RecordNotFoundException ex) {
	        log.error("Record not found: {}", ex.getMessage());
	        throw ex; 
	    } catch (Exception ex) {
	        log.error("An unexpected error occurred while fetching FeeTypeMaster with ID: {} and Institute ID: {}", id, instituteId, ex);
	        throw new InternalServerException(AppConstants.PROCESSING_ERROR.getMessage());
	    }
	}
	
	@Override
	public Response<?> deactivateFeeTypeMaster(Long id, Long instituteId) {
	    try {
	        FeeTypeMaster feeTypeMaster = iFeeTypeMasterRepo.findByIdAndInstituteIdAndIsActiveTrue(id, instituteId)
	                .orElseThrow(() -> {
	                    log.warn("FeeTypeMaster record not found for ID: {}", id);
	                    return new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + id);
	                });

	        if (iFeeHeadMasterRepo.existsByFeeTypeIdAndIsActiveTrue(id)) {
	            log.warn("Found mappings in Fee Head for FeeTypeMaster ID: {}", id);
	            return new Response<>(HttpStatus.BAD_REQUEST.value(), AppConstants.FEE_HEAD_DEACTIVATION_NOT_ALLOWED.getMessage());
	        }

	        if (Boolean.TRUE.equals(feeTypeMaster.getIsDefault())) {
	            log.warn("Cannot delete default FeeTypeMaster ID: {}", id);
	            return new Response<>(HttpStatus.BAD_REQUEST.value(), AppConstants.DEFAULT_RECORD_DELETION_NOT_ALLOWED.getMessage());
	        }

	        feeTypeMaster.setIsActive(false);
	        iFeeTypeMasterRepo.save(feeTypeMaster);
	        log.info("FeeTypeMaster with ID: {} deactivated successfully", id);
	        return new Response<>(HttpStatus.OK.value(), "Fee Setup deactivated successfully.");
	    } catch (RecordNotFoundException e) {
	        return new Response<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
	    } catch (Exception e) {
	        log.error("Unexpected error occurred while deactivating FeeTypeMaster ID: {}: {}", id, e.getMessage(), e);
	        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PROCESSING_ERROR.getMessage());
	    }
	}




    //**********************************************************
    @Override
    public Optional<BankDTO> getBankDetailsById(Long accSetupId, Long instituteId) {
        Optional<FinanceAccountSetup> accountSetupOpt = financeAccountSetupRepo.findByAccSetupIdAndInstituteIdAndIsActiveTrue(accSetupId, instituteId);

        return accountSetupOpt.map(this::convertToDTO);
    }

    private BankDTO convertToDTO(FinanceAccountSetup entity) {
        return BankDTO.builder()
                .accSetupId(entity.getAccSetupId())
                .empId(entity.getEmpId())
                .openingBalance(entity.getOpeningBalance())
                .status(entity.getStatus())
                .bankName(entity.getBankName())
                .bankAccountNo(entity.getBankAccountNo())
                .bankLogo(entity.getBankLogo())
                .instituteId(entity.getInstituteId())
                .build();
    }
//******************************************************************************
    private FinanceAccountsConfig saveOrUpdateCashier(CashierDTO cashierDTO, FinanceAccountSetup financeAccountSetup) {
		FinanceAccountsConfig financeAccountsConfig;

		// Check if an existing FinanceAccountsConfig entity needs to be updated
		if (cashierDTO.getAccConfigId() != null) {
			Optional<FinanceAccountsConfig> financeAccountsConfigOptional = iFinanceAccountsConfigRepo.findById(cashierDTO.getAccConfigId());
			if (financeAccountsConfigOptional.isPresent()) {
				financeAccountsConfig = financeAccountsConfigOptional.get();
			} else {
				financeAccountsConfig = new FinanceAccountsConfig(); // Create a new instance if not found
			}
		} else {
			financeAccountsConfig = new FinanceAccountsConfig(); // Create a new instance if accConfigId is null
		}

		// Set or update the properties from CashierDTO
		financeAccountsConfig.setFkFinanceAccSetupId(financeAccountSetup.getAccSetupId());
		financeAccountsConfig.setAccessType(cashierDTO.getAccessType());
		financeAccountsConfig.setStatus(financeAccountSetup.getStatus());
		financeAccountsConfig.setInstituteId(financeAccountSetup.getInstituteId());

		// Handle multiple cashierMappingDTO entries
		if (cashierDTO.getCashierMappingDTO() != null && !cashierDTO.getCashierMappingDTO().isEmpty()) {
			List<FinanceAccountsConfig> cashierMappings = new ArrayList<>();
			for (cashierMappingDTO mappingDTO : cashierDTO.getCashierMappingDTO()) {
				FinanceAccountsConfig mapping = new FinanceAccountsConfig();
				mapping.setTitle(mappingDTO.getTitle());
				mapping.setStatus(mappingDTO.getStatus());
//				mapping.setFinanceAccountsConfig(financeAccountsConfig); // Set the relationship
				cashierMappings.add(mapping);
			}
//			financeAccountsConfig.setFinanceAccountsConfig(cashierMappings);
		}

		// Save the FinanceAccountsConfig entity and return it
		return iFinanceAccountsConfigRepo.save(financeAccountsConfig);
	}
	@Override
	public List<FeeConfigOutputDTO> getAllFeeConfig() {
		
		return iFeeConfigRepo.findByIsActiveTrue().stream().map(feeConfig->{
			FeeConfigOutputDTO feeConfigOutputDTO=new FeeConfigOutputDTO(); 
			BeanUtils.copyProperties(feeConfig, feeConfigOutputDTO);
			return feeConfigOutputDTO;
		}).collect(Collectors.toList());
		
	}
	@Override
	@Transactional
	
	public Response<?> addorUpdateFeeHeadMaster(List<FeeHeadMasterDTO> feeHeadMasterDTOList) {
	    List<FeeHeadMaster> feeHeadMastersToSave = new ArrayList<>();
	    List<String> errorMessages = new ArrayList<>();

	    feeHeadMasterDTOList.forEach(feeHeadMasterDTO -> {
	        try {
	        	
            	
	            if (feeHeadMasterDTO.getFeeHeadId() != null) {
	            	
	                FeeHeadMaster existingFeeHeadMaster = iFeeHeadMasterRepo.findByFeeHeadIdAndInstituteIdAndIsActiveTrue(feeHeadMasterDTO.getFeeHeadId(), feeHeadMasterDTO.getInstituteId())
	                        .orElseThrow(() -> new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + feeHeadMasterDTO.getFeeHeadId()));
	                if(!(feeHeadMasterDTO.getFeeHeadName().equalsIgnoreCase(existingFeeHeadMaster.getFeeHeadName()))) {
	                	if(iFeeHeadMasterRepo.existsByInstituteIdAndFeeHeadNameAndIsActiveTrue(feeHeadMasterDTO.getInstituteId(), feeHeadMasterDTO.getFeeHeadName())) {
		            		errorMessages.add(AppConstants.DUPLICATE_FEETHEAD_NAME.getMessage()+feeHeadMasterDTO.getFeeHeadName()+" Please try with another name");
		            		return;
		            	}
	                }
	                if (Boolean.TRUE.equals(existingFeeHeadMaster.getIsDefault())) {
	                    String feeHeadName = feeHeadMasterDTO.getFeeHeadName();
	                    
	                    if (!existingFeeHeadMaster.getFeeHeadName().equalsIgnoreCase(feeHeadName)) {
	                        errorMessages.add(AppConstants.DEFAULT_RECORD_UPDATE_NOT_ALLOWED.getMessage() + feeHeadMasterDTO.getFeeHeadId());
	                        return;
	                    }
	                    if(existingFeeHeadMaster.getIsDefault()) {
	                    	feeHeadMasterDTO.setIsDefault(true);
                            log.info("feeHeadMasterDTO obj value {}",feeHeadMasterDTO);
                            }
	                }

	                BeanUtils.copyProperties(feeHeadMasterDTO, existingFeeHeadMaster);
	                feeHeadMastersToSave.add(existingFeeHeadMaster);
	                log.info("Prepared FeeHeadMaster update for Institute ID: {}", feeHeadMasterDTO.getInstituteId());
	            } else {
	            	if(iFeeHeadMasterRepo.existsByInstituteIdAndFeeHeadNameAndIsActiveTrue(feeHeadMasterDTO.getInstituteId(), feeHeadMasterDTO.getFeeHeadName())) {
	            		errorMessages.add(AppConstants.DUPLICATE_FEETHEAD_NAME.getMessage()+feeHeadMasterDTO.getFeeHeadName()+" Please try with another name");
	            		return;
	            	}
	                FeeHeadMaster newFeeHeadMaster = new FeeHeadMaster();
	                BeanUtils.copyProperties(feeHeadMasterDTO, newFeeHeadMaster);
	                feeHeadMastersToSave.add(newFeeHeadMaster);
	                log.info("Prepared FeeHeadMaster addition for Institute ID: {}", feeHeadMasterDTO.getInstituteId());
	            }
	        } catch (RecordNotFoundException e) {
	            errorMessages.add("Record not found for ID: " + feeHeadMasterDTO.getFeeHeadId());
	            log.error("Record not found for FeeHeadMaster ID: {}", feeHeadMasterDTO.getFeeHeadId(), e);
	        } catch (Exception e) {
	            errorMessages.add("Error processing FeeHeadMaster for ID: " + feeHeadMasterDTO.getFeeHeadId());
	            log.error("Error occurred while processing FeeHeadMaster with ID: {}", feeHeadMasterDTO.getFeeHeadId(), e);
	        }
	    });

	    if (!feeHeadMastersToSave.isEmpty()) {
	        iFeeHeadMasterRepo.saveAll(feeHeadMastersToSave);
	        log.info("FeeHeadMasters saved successfully.");
	    }

	    if (errorMessages.isEmpty()) {
	        return new Response<>(HttpStatus.OK.value(), AppConstants.FEE_HEAD_SETUP_SUCCESS.getMessage());
	    } else if (errorMessages.size() == feeHeadMasterDTOList.size()) {
	        return new Response<>(HttpStatus.BAD_REQUEST.value(), AppConstants.FEE_HEAD_SETUP_FAILURE.getMessage(), String.join(", ", errorMessages));
	    } else {
	        return new Response<>(HttpStatus.PARTIAL_CONTENT.value(), AppConstants.FEE_HEAD_SETUP_PARTIAL_SUCCESS.getMessage(), String.join(", ", errorMessages));
	    }
	}

	@Override
	public Response<?> geetypeMasterByFeeTypeName(Long instituteId, String feeType) throws RecordNotFoundException {
	   
	    log.info("Fetching FeeTypeMaster for InstituteId: {} and FeeType: {}", instituteId, feeType);

	    Optional<FeeTypeMaster> Optional = iFeeTypeMasterRepo.findByInstituteIdAndFeeTypeAndIsDefaultTrue(instituteId, feeType);
	    if(Optional.isEmpty()) {
	    	 return new Response<>(AppConstants.FAILURE_CODE.getCode(),AppConstants.RECORD_NOT_FOUND.getMessage(),null);
	    }else {
	    FeeTypeMasterDTO feeTypeMasterDTO = new FeeTypeMasterDTO();
	    BeanUtils.copyProperties(Optional.get(), feeTypeMasterDTO);

	    log.info("Successfully retrieved FeeTypeMaster for InstituteId: {} and FeeType: {}", instituteId, feeType);
	    return new Response<>(AppConstants.SUCCESS_CODE.getCode(),AppConstants.RECORD_FOUND.getMessage(),feeTypeMasterDTO);
	}
	}
	@Override
	public FeeHeadMasterOutputDTO getFeeHeadMasterById(Long id, Long instituteId) {
		 log.info("Fetching FeeHeadMaster with ID: {} and Institute ID: {}", id, instituteId);
		    try {
		    	FeeHeadMaster feeHeadMaster = iFeeHeadMasterRepo.findByFeeHeadIdAndInstituteIdAndIsActiveTrue(id, instituteId)
		                .orElseThrow(() -> new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + " ID: " + id));


		        log.debug("FeeHeadMaster fetched successfully: {}", feeHeadMaster);
		        FeeHeadMasterOutputDTO feeHeadMasterOutputDTO = new FeeHeadMasterOutputDTO();
		        FeeTypeMaster feeTypeMaster=iFeeTypeMasterRepo.findByIdAndInstituteIdAndIsActiveTrue(feeHeadMaster.getFeeTypeId(), instituteId)
		                .orElseThrow(() -> new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + " ID: " + id));
		        BeanUtils.copyProperties(feeHeadMaster, feeHeadMasterOutputDTO);
		        feeHeadMasterOutputDTO.setFeeTypeName(feeTypeMaster.getFeeType());
		        return feeHeadMasterOutputDTO;
		    } catch (RecordNotFoundException ex) {
		        log.error("Record not found: {}", ex.getMessage());
		        throw ex; 
		    } catch (Exception ex) {
		        log.error("An unexpected error occurred while fetching FeeHeadMaster with ID: {} and Institute ID: {}", id, instituteId, ex);
		        throw new InternalServerException(AppConstants.PROCESSING_ERROR.getMessage());
		    }
	}

	@Override
	public Response<?> getAllFeeHeadMaster(Long instituteId) {
	    log.info("Fetching FeeHeadMaster data for instituteId: {}", instituteId);
	    List<FeeHeadMaster> listFeeHeadMaster = iFeeHeadMasterRepo.findByInstituteIdAndIsActiveTrue(instituteId);
	    log.info("-------------------------------- {}",listFeeHeadMaster);
	    if (!listFeeHeadMaster.isEmpty()) {
	        log.info("Found {} FeeHeadMaster records for instituteId: {}", listFeeHeadMaster.size(), instituteId);

	        List<FeeHeadMasterOutputDTO> listData = listFeeHeadMaster.stream().map(feeHeadMaster -> {
	            FeeHeadMasterOutputDTO feeHeadMasterOutputDTO = new FeeHeadMasterOutputDTO();
	            Optional<FeeTypeMaster> feeTypeMasterOpt = iFeeTypeMasterRepo.findByIdAndInstituteIdAndIsActiveTrue(feeHeadMaster.getFeeTypeId(), instituteId);

	            if (feeTypeMasterOpt.isPresent()) {
	                FeeTypeMaster feeTypeMaster = feeTypeMasterOpt.get();
	                BeanUtils.copyProperties(feeHeadMaster, feeHeadMasterOutputDTO);
	                log.info("---------------------=--0o=0------------ {}",feeHeadMasterOutputDTO);
	                feeHeadMasterOutputDTO.setFeeTypeName(feeTypeMaster.getFeeType());
	                feeHeadMasterOutputDTO.setTax(feeTypeMaster.getTax());
	                log.debug("Mapped FeeHeadMaster with FeeTypeMaster: FeeTypeName={}, Tax={}", feeTypeMaster.getFeeType(), feeTypeMaster.getTax());
	            } else {
	            	FeeTypeMaster feeTypeMaster = feeTypeMasterOpt.get();
	                BeanUtils.copyProperties(feeHeadMaster, feeHeadMasterOutputDTO);
	                log.info("---------------------=--0o=0------------ {}",feeHeadMasterOutputDTO);
	            	
	                log.warn("FeeTypeMaster not found for FeeHeadMaster with ID: {}", feeHeadMaster.getFeeHeadId());
	            }

	            return feeHeadMasterOutputDTO;
	        }).collect(Collectors.toList());

	        log.info("Successfully retrieved FeeHeadMaster data for instituteId: {}", instituteId);
	        return new Response<>(AppConstants.SUCCESS_CODE.getCode(), AppConstants.DATA_RERIEVED_SUCCESSFULLY.getMessage(), listData);
	    } else {
	        log.warn("No FeeHeadMaster data found for instituteId: {}", instituteId);
	        return new Response<>(AppConstants.FAILURE_CODE.getCode(), AppConstants.DATA_NOT_FOUND.getMessage() + instituteId, null);
	    }
	}

	@Override
	public Response<?> deactivateFeeHeadMaster(Long id, Long instituteId) {
		
		 try {
		        FeeHeadMaster feeHeadMaster = iFeeHeadMasterRepo.findByFeeHeadIdAndInstituteIdAndIsActiveTrue(id, instituteId)
		                .orElseThrow(() -> {
		                    log.warn("FeeHeadMaster record not found for ID: {}", id);
		                    return new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + id);
		                });
		        if(iFeeMasterDetailsRepo.existsByFeeHeadIdAndIsActiveTrue(id)) {
		        	log.warn("Found mappings in FeeSetup for FeeHeadMaster ID: {}", id);
		            return new Response<>(HttpStatus.BAD_REQUEST.value(), AppConstants.FEE_HEAD_DEACTIVATION_NOT_ALLOWED.getMessage());
		        }
		        
		        feeHeadMaster.setIsActive(false);
		        iFeeHeadMasterRepo.save(feeHeadMaster);
		        log.info("FeeHeadMaster with ID: {} deactivated successfully", id);
		        return new Response<>(HttpStatus.OK.value(), "Fee Head deactivated successfully.");
		    } catch (RecordNotFoundException e) {
		        return new Response<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
		    } catch (Exception e) {
		        log.error("Unexpected error occurred while deactivating Fee Head ID: {}: {}", id, e.getMessage(), e);
		        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PROCESSING_ERROR.getMessage());
		    }
	}
	@Override
	public Response<?> addOrUpdateAddOnDiscount(AddOnDiscountDataDTO addOnDiscountDTO) {
	    try {
	        AddOnDiscount addOnDiscount = addOnDiscountDTO.getId() != null
	            ? iAddOnDiscountRepo.findByIdAndFkInstituteIdAndIsActiveTrue(
	                    addOnDiscountDTO.getId(), addOnDiscountDTO.getFkInstituteId())
	                .orElseThrow(() -> new RecordNotFoundException(
	                    AppConstants.RECORD_NOT_FOUND.getMessage() + addOnDiscountDTO.getId()))
	            : new AddOnDiscount();

	        addOnDiscount.setFkInstituteId(addOnDiscountDTO.getFkInstituteId());
	        addOnDiscount.setFkStudentId(addOnDiscountDTO.getFkStudentId());
	        addOnDiscount.setUserId(addOnDiscountDTO.getUserId());
	        
	        
	     
	        
	        log.info("Successfully added or updated AddOnDiscount with ID: {}", addOnDiscountDTO.getAddOnDiscountList());

	        try {
	            String discountJson = objectMapper.writeValueAsString(addOnDiscountDTO.getAddOnDiscountList());
	            addOnDiscount.setAddOnDiscount(discountJson);
	        } catch (JsonProcessingException e) {
	            log.error("Error processing JSON for AddOnDiscount: {}", e.getMessage(), e);
	            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                "Failed to process discount details.");
	        }

	        iAddOnDiscountRepo.save(addOnDiscount);
	        log.info("Successfully added or updated AddOnDiscount with ID: {}", addOnDiscount.getId());

	        return new Response<>(HttpStatus.OK.value(), AppConstants.ADD_ON_DISCOUNT_SUCCESS.getMessage());

	    } catch (RecordNotFoundException e) {
	        log.error("Record not found for AddOnDiscount ID: {}", addOnDiscountDTO.getId(), e);
	        return new Response<>(HttpStatus.BAD_REQUEST.value(),
	        		AppConstants.RECORD_NOT_FOUND.getMessage() + addOnDiscountDTO.getId());
	    } catch (Exception e) {
	        log.error("Error occurred while processing AddOnDiscount with ID: {}", addOnDiscountDTO.getId(), e);
	        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
	            AppConstants.ADD_ON_DISCOUNT_FAILURE.getMessage(), e.getMessage());
	    }
	}
	@Override
	public AddOnDiscountDataResponseDTO getAddOnDiscountById(Long id, Long instituteId) {
	    try {
	        AddOnDiscount addOnDiscount = iAddOnDiscountRepo.findByIdAndFkInstituteIdAndIsActiveTrue(id, instituteId)
	            .orElseThrow(() -> new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + id));

	        AddOnDiscountDataResponseDTO addOnDiscountDataDTO = convertToAddOnDiscountForStudentDataDTO(addOnDiscount);
	        log.info("Successfully retrieved AddOnDiscount with ID: {} and Institute ID: {}", id, instituteId);

	        return addOnDiscountDataDTO;
	    } catch (RecordNotFoundException e) {
	        log.error("Record not found for AddOnDiscount with ID: {} and Institute ID: {}", id, instituteId, e);
	        throw e; 
	    } catch (Exception e) {
	        log.error("Unexpected error while retrieving AddOnDiscount with ID: {} and Institute ID: {}", id, instituteId, e);
	        throw new InternalServerException(AppConstants.UNEXPECTED_ERROR_RETRIEVING_ADD_ON_DISCOUNT.getMessage(), e);
	    }
	    
	  
	}
	 private  List<DiscountResponseDTO> getAllDiscounts(Long studentId,Long instituteId){
		   return getAllDiscountsByStudentId(studentId,instituteId);
	   }
	 
	 private AddOnDiscountDataResponseDTO convertToAddOnDiscountForStudentDataDTO(AddOnDiscount addOnDiscount) {
		    try {
		        List<AddOnDiscountDTO> listData = convertJsonToListAddOnDiscountDTO(addOnDiscount.getAddOnDiscount());
		        List<DiscountResponseDTO> listOfDiscounts=getAllDiscounts(addOnDiscount.getFkStudentId(),addOnDiscount.getFkInstituteId());
		      
		        AddOnDiscountDataResponseDTO addOnDiscountDataDTO = AddOnDiscountDataResponseDTO.builder()
		            .fkInstituteId(addOnDiscount.getFkInstituteId())
		            .id(addOnDiscount.getId())
		            .fkStudentId(addOnDiscount.getFkStudentId())
		            .addOnDiscountList(listData)
		            .allDiscounts(listOfDiscounts)
		            .build();

		        log.info("Successfully converted AddOnDiscount to AddOnDiscountDataDTO with ID: {}", addOnDiscount.getId());
		        return addOnDiscountDataDTO;
		    } catch (Exception e) {
		        log.error("Error converting AddOnDiscount with ID: {} to AddOnDiscountDataDTO", addOnDiscount.getId(), e);
		        throw new InternalServerException(AppConstants.ERROR_CONVERTING_ADD_ON_DISCOUNT.getMessage(), e);
		    }
		}
	 
	 

	private AddOnDiscountDataDTO convertToAddOnDiscountDataDTO(AddOnDiscount addOnDiscount) {
	    try {
	        List<AddOnDiscountDTO> listData = convertJsonToListAddOnDiscountDTO(addOnDiscount.getAddOnDiscount());
	        
	        

	        AddOnDiscountDataDTO addOnDiscountDataDTO = AddOnDiscountDataDTO.builder()
	            .fkInstituteId(addOnDiscount.getFkInstituteId())
	            .id(addOnDiscount.getId())
	            .fkStudentId(addOnDiscount.getFkStudentId())
	            .addOnDiscountList(listData)
	            .build();

	        log.info("Successfully converted AddOnDiscount to AddOnDiscountDataDTO with ID: {}", addOnDiscount.getId());
	        return addOnDiscountDataDTO;
	    } catch (Exception e) {
	        log.error("Error converting AddOnDiscount with ID: {} to AddOnDiscountDataDTO", addOnDiscount.getId(), e);
	        throw new InternalServerException(AppConstants.ERROR_CONVERTING_ADD_ON_DISCOUNT.getMessage(), e);
	    }
	}

	private List<AddOnDiscountDTO> convertJsonToListAddOnDiscountDTO(String jsonAddOnDiscountDTO) {
	    try {
	        List<AddOnDiscountDTO> result = objectMapper.readValue(jsonAddOnDiscountDTO, new TypeReference<List<AddOnDiscountDTO>>() {});
	        log.info("Converted JSON to List<AddOnDiscountDTO>: {}", result);
	        return result;
	    } catch (IOException e) {
	        log.error("Error converting JSON to List<AddOnDiscountDTO>: {}", e.getMessage(), e);
	        return Collections.emptyList(); 
	    }
	}
	private  List<AddOnDiscountResponseDTO> converttoDTO(List<AddOnDiscount> listData){
		return listData.stream()
                .map(this::convertEntityToAddOnDiscountResponseDTO)
                .collect(Collectors.toList());
		
	}
	@Override
	public Response<?> getAllAddOnDiscount(AddOnDiscountFilterDTO filterDTO) {
	    List<AddOnDiscount> listData = new ArrayList<>();
	    List<AddOnDiscountResponseDTO> listDTO = new ArrayList<>();

	    Long instituteId = filterDTO.getInstituteId();
	    Long courseId = filterDTO.getCourseId();
	    String startDate = filterDTO.getStartDate();
	    String endDate = filterDTO.getEndDate();
	    Long feeHeadId = filterDTO.getFeeHeadId();
	    Long feeTypeId = filterDTO.getFeeTypeId();

	    log.info("Fetching AddOnDiscount data with filter: {}", filterDTO);

	    if (Objects.nonNull(instituteId)) {
	        if (Objects.isNull(courseId) && Objects.isNull(endDate) && Objects.isNull(feeHeadId) && Objects.isNull(feeTypeId) && Objects.isNull(startDate)) {
	            log.info("Fetching AddOnDiscounts with only InstituteId: {}", instituteId);
	            listData = iAddOnDiscountRepo.findByFkInstituteIdAndIsActiveTrue(instituteId);
	            return new Response<>(AppConstants.SUCCESS_CODE.getCode(), 
	                    AppConstants.DATA_RERIEVED_SUCCESSFULLY.getMessage(), converttoDTO(listData));
	        }

	        if (Objects.nonNull(startDate) && Objects.nonNull(endDate)) {
	            log.info("Fetching AddOnDiscounts between dates {} and {} for InstituteId: {}", startDate, endDate, instituteId);
	            listData = iAddOnDiscountRepo.getAllAddOnDiscountBetweenDates(instituteId, startDate, endDate);

	            if (Objects.nonNull(courseId)) {
	                List<Long> studentIds = studentServiceFeignClient.getStudentsByInstituteAndCourse(instituteId, courseId)
	                        .getData().stream().map(StudentCourseDTO::getStudentId).collect(Collectors.toList());

	                log.info("Filtering AddOnDiscounts by studentIds: {}", studentIds);
	                listData = listData.stream()
	                        .filter(data -> studentIds.contains(data.getFkStudentId()))
	                        .collect(Collectors.toList());
	            }

	            if (Objects.nonNull(feeTypeId)) {
		            log.info("Filtering AddOnDiscounts by FeeTypeId: {}", feeTypeId);

		            List<FeeHeadMaster> listFeeHead = iFeeHeadMasterRepo.findByInstituteIdAndFeeTypeIdAndIsActiveTrue(
		                    instituteId, feeTypeId);
		            List<String> feeHeadNames = listFeeHead.stream().map(FeeHeadMaster::getFeeHeadName).toList();

		            log.info("FeeHeadMaster records found: {}", listFeeHead.size());

		            listDTO = listData.stream()
		                    .filter(dto -> feeHeadNames.contains(dto.getAddOnDiscount()))
		                    .map(this::convertEntityToAddOnDiscountResponseDTO)
		                    .collect(Collectors.toList());

		            if (Objects.nonNull(feeHeadId)) {
		                log.info("Further filtering by FeeHeadId: {}", feeHeadId);

		                listDTO = listDTO.stream()
		                        .filter(dto -> dto.getFeeHeadName().equals(iFeeHeadMasterRepo.findById(feeHeadId).get().getFeeHeadName()))
		                        .collect(Collectors.toList());

		                log.info("Filtered AddOnDiscounts by FeeHeadId: {} records found", listDTO.size());
	                return new Response<>(AppConstants.SUCCESS_CODE.getCode(), 
		                    AppConstants.DATA_RERIEVED_SUCCESSFULLY.getMessage(),listDTO);
		            }
	            }
	            return new Response<>(AppConstants.SUCCESS_CODE.getCode(), 
	                    AppConstants.DATA_RERIEVED_SUCCESSFULLY.getMessage(), converttoDTO(listData));
	        }
	    }

	    log.warn("No AddOnDiscount data found for the given filters.");
	    return new Response<>(AppConstants.FAILURE_CODE.getCode(), 
	            AppConstants.DATA_NOT_FOUND.getMessage(), null);
	}
	

	@Override
	public Response<?> deActivateAddOnDiscount(Long id, Long instituteId) {
			
			 try {
				 AddOnDiscount addOnDiscount = iAddOnDiscountRepo.findByIdAndFkInstituteIdAndIsActiveTrue(id, instituteId)
				            .orElseThrow(() -> new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + id));
				 addOnDiscount.setIsActive(false);
				 iAddOnDiscountRepo.save(addOnDiscount);
			        log.info("Add On Discount with ID: {} deactivated successfully", id);
			        return new Response<>(HttpStatus.OK.value(), AppConstants.RECORD_DEACTIVATION_SUCESSFULL.getMessage());
			    } catch (RecordNotFoundException e) {
			        return new Response<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
			    } catch (Exception e) {
			        log.error("Unexpected error occurred while deactivating Fee Head ID: {}: {}", id, e.getMessage(), e);
			        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PROCESSING_ERROR.getMessage());
			    }
		}
	@Override
	public Response<?> addOrUpdateProfileDiscount(ProfileDiscountDataDTO profileDiscountDataDTO) {
		try {
		ProfileDiscount profileDiscount =profileDiscountDataDTO.getProfileDiscountId() != null
	            ? iProfileDiscountRepo.findByIdAndFkInstituteIdAndIsActiveTrue(
	            		profileDiscountDataDTO.getProfileDiscountId(), profileDiscountDataDTO.getFkInstituteId())
	                .orElseThrow(() -> new RecordNotFoundException(
	                    AppConstants.RECORD_NOT_FOUND.getMessage() + profileDiscountDataDTO.getProfileDiscountId()))
	            : new ProfileDiscount();

		profileDiscount.setFkInstituteId(profileDiscountDataDTO.getFkInstituteId());
		profileDiscount.setDescription(profileDiscountDataDTO.getDescription());
		profileDiscount.setFeeProfiletittle(profileDiscountDataDTO.getFeeProfileTittle());
		
		 try {
	            String discountJson = objectMapper.writeValueAsString(profileDiscountDataDTO.getProfileDiscounts());
	            profileDiscount.setDiscountProfile(discountJson);
	        } catch (JsonProcessingException e) {
	            log.error("Error processing JSON for profile Discount: {}", e.getMessage(), e);
	            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
	                "Failed to process  profile discount details.");
	        }
		 iProfileDiscountRepo.save(profileDiscount);
	        log.info("Successfully added or updated profile Discount with ID: {}", profileDiscountDataDTO.getProfileDiscountId());

	        return new Response<>(HttpStatus.OK.value(), AppConstants.PROFILE_DISCOUNT_SUCCESS.getMessage());

	    } catch (RecordNotFoundException e) {
	        log.error("Record not found for AddOnDiscount ID: {}", profileDiscountDataDTO.getProfileDiscountId(), e);
	        return new Response<>(HttpStatus.BAD_REQUEST.value(),
	        		AppConstants.RECORD_NOT_FOUND.getMessage() + profileDiscountDataDTO.getProfileDiscountId());
	    } catch (Exception e) {
	        log.error("Error occurred while processing profile Discount with ID: {}", profileDiscountDataDTO.getProfileDiscountId(), e);
	        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
	            AppConstants.PROFILE_DISCOUNT_FAILURE.getMessage(), e.getMessage());
	    }
	}
	@Override
	public ProfileDiscountDataDTO getProfileDiscountsById(Long id, Long instituteId) {
		 try {
			 ProfileDiscount profileDiscount = iProfileDiscountRepo.findByIdAndFkInstituteIdAndIsActiveTrue(id, instituteId)
		            .orElseThrow(() -> new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + id));
			 ProfileDiscountDataDTO profileDiscountDataDTO=convertProfileDiscountToDTO(profileDiscount);

		        
		        log.info("Successfully retrieved ProfileDiscount with ID: {} and Institute ID: {}", id, instituteId);

		        return profileDiscountDataDTO;
		    } catch (RecordNotFoundException e) {
		        log.error("Record not found for ProfileDiscount with ID: {} and Institute ID: {}", id, instituteId, e);
		        throw e; 
		    } catch (Exception e) {
		        log.error("Unexpected error while retrieving ProfileDiscount with ID: {} and Institute ID: {}", id, instituteId, e);
		        throw new InternalServerException(AppConstants.PROCESSING_ERROR.getMessage(), e);
		    }
		}
	
	@Override
	public List<ProfileDiscountGetDTO> getAllProfileDiscounts(Long instituteId) {
		List<ProfileDiscount> listData=iProfileDiscountRepo.findByFkInstituteIdAndIsActiveTrue(instituteId);
	if(!listData.isEmpty()) {
		return listData.stream().map(profileDiscountdata->
		convertEntityToProfileDiscountGetDTO(profileDiscountdata)
		).collect(Collectors.toList());
	}else {
		return null;
	}
	
	
}
	
	private ProfileDiscountGetDTO convertEntityToProfileDiscountGetDTO(ProfileDiscount profileDiscount) {
		Double wavierAmount=0.0;
		List<StudentCourseDTO> listStudents=studentProfileDiscountClient.getStudentsByInstituteAndProfileDiscount(profileDiscount.getFkInstituteId(), profileDiscount.getId()).getData();
		
		List<ProfileDiscountDTO> profileDiscountDTO=convertJsonToListProfileDiscountDTO(profileDiscount.getDiscountProfile());
		for (ProfileDiscountDTO dto : profileDiscountDTO) {
		   
		    Long feeTypeId = dto.getFeetypeId();
		    List<FeeHeadMaster> feeHeadMasters = iFeeHeadMasterRepo.findByInstituteIdAndFeeTypeIdAndIsActiveTrue( profileDiscount.getFkInstituteId(),feeTypeId);
		    
		    for (FeeHeadMaster feeHeadMaster : feeHeadMasters) {
		        List<FeeMasterDetails> feeMasterDetails = iFeeMasterDetailsRepo.findByFeeHeadIdAndInstituteIdAndIsActiveTrue(feeHeadMaster.getFeeHeadId(), profileDiscount.getFkInstituteId());
		        if(!feeMasterDetails.isEmpty()){
		        for (FeeMasterDetails masterDetails : feeMasterDetails) {
		            wavierAmount += masterDetails.getFeeAmount();
		        }
		        }
		    }
		}
		List<StudentCourseDTO>  listStudentData=new ArrayList<>();
		if(listStudents!=null) {
		  listStudentData=filterStudentsByDiscount(listStudents,profileDiscount.getId());
		int size=listStudentData.size();
		wavierAmount=wavierAmount*size;
		
		}
		
		
	
		
		
		ProfileDiscountGetDTO profileDiscountGetDTO= ProfileDiscountGetDTO.builder()
				.description(profileDiscount.getDescription())
				.feeProfileTittle(profileDiscount.getFeeProfiletittle())
				.instituteId(profileDiscount.getFkInstituteId())
				.profileDiscountId(profileDiscount.getId())
				
				.wavierAmount(wavierAmount)
				.build();
		if(!listStudentData.isEmpty()) {
			profileDiscountGetDTO.setStudentInfoDTO(listStudentData);
		}
		return profileDiscountGetDTO;
	}
	@Override
	public Response<?> deActivateProfileDiscount(Long id, Long instituteId) {
		try {
			ProfileDiscount profileDiscount = iProfileDiscountRepo.findByIdAndFkInstituteIdAndIsActiveTrue(id, instituteId)
			            .orElseThrow(() -> new RecordNotFoundException(AppConstants.RECORD_NOT_FOUND.getMessage() + id));
			profileDiscount.setIsActive(false);
			iProfileDiscountRepo.save(profileDiscount);
		        log.info("ProfileDiscount with ID: {} deactivated successfully", id);
		        return new Response<>(HttpStatus.OK.value(), AppConstants.RECORD_DEACTIVATION_SUCESSFULL.getMessage());
		    } catch (RecordNotFoundException e) {
		        return new Response<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
		    } catch (Exception e) {
		        log.error("Unexpected error occurred while deactivating ProfileDiscount ID: {}: {}", id, e.getMessage(), e);
		        return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.PROCESSING_ERROR.getMessage());
		    }
	}
	 public List<StudentCourseDTO> filterStudentsByDiscount(List<StudentCourseDTO> students, Long discountValue) {
	        return students.stream()
	                .filter(student -> student.getProfileDiscount() != null 
	                        && student.getProfileDiscount().contains(discountValue.toString()))
	                .collect(Collectors.toList());
	    }

	private ProfileDiscountDataDTO convertProfileDiscountToDTO(ProfileDiscount profileDiscount) {
		try {
		List<ProfileDiscountDTO> profileDiscountDTO=convertJsonToListProfileDiscountDTO(profileDiscount.getDiscountProfile());
		ProfileDiscountDataDTO profileDiscountDataDTO=ProfileDiscountDataDTO.builder()
				.description(profileDiscount.getDescription())
				.feeProfileTittle(profileDiscount.getFeeProfiletittle())
				.fkInstituteId(profileDiscount.getFkInstituteId())
				.profileDiscounts(profileDiscountDTO)
				.profileDiscountId(profileDiscount.getId())
				.build();
		 log.info("Successfully converted ProfileDiscount to ProfileDiscountDataDTO with ID: {}", profileDiscountDataDTO.getProfileDiscountId() );
	        return profileDiscountDataDTO;
	    } catch (Exception e) {
	        log.error("Error converting ProfileDiscount with ID: {} to ProfileDiscountDataDTO",  e);
	        throw new InternalServerException(AppConstants.ERROR_CONVERTING_ADD_ON_DISCOUNT.getMessage(), e);
	    }
	}
	
	private List<ProfileDiscountDTO> convertJsonToListProfileDiscountDTO(String jsonData){
		try {
	        List<ProfileDiscountDTO> result = objectMapper.readValue(jsonData, new TypeReference<List<ProfileDiscountDTO>>() {});
	        log.info("Converted JSON to  List<ProfileDiscountDTO>: {}", result);
	        return result;
	    } catch (IOException e) {
	        log.error("Error converting JSON to   List<ProfileDiscountDTO>: {}", e.getMessage(), e);
	        return Collections.emptyList(); 
	    }
		
	}

    @Override
    public Response<?> addOrUpdateCashier(CashierDataDTO cashierDataDTO) {
        try {

            FinanceAccountSetup financeAccountSetup = cashierDataDTO.getAccSetupId() != null
                    ? iFinanceAccountSetupRepo.findByAccSetupIdAndInstituteIdAndIsActiveTrue(cashierDataDTO.getAccSetupId(), cashierDataDTO.getInstituteId())
                    .orElseThrow(() -> new RecordNotFoundException(
                            AppConstants.RECORD_NOT_FOUND.getMessage() + cashierDataDTO.getAccSetupId()))
                    : new FinanceAccountSetup();


            financeAccountSetup.setEmpId(cashierDataDTO.getEmpId());
            financeAccountSetup.setInstituteId(cashierDataDTO.getInstituteId());
            financeAccountSetup.setOpeningBalance(cashierDataDTO.getOpeningBalance());
            financeAccountSetup.setStatus(cashierDataDTO.getStatus());


            FinanceAccountSetup savedFinanceAccountSetup = iFinanceAccountSetupRepo.save(financeAccountSetup);


            List<FinanceAccountsConfig> configList = new ArrayList<>();


            cashierDataDTO.getAceessData().forEach(accessData -> {
                accessData.getAccessDTO().forEach(accessTypeDTO -> {
                    FinanceAccountsConfig financeAccountsConfig;


                    if (accessTypeDTO.getId() != null) {
                        financeAccountsConfig = iFinanceAccountsConfigRepo.findById(accessTypeDTO.getId())
                                .orElse(new FinanceAccountsConfig());
                    } else {
                        financeAccountsConfig = new FinanceAccountsConfig();
                    }


                    financeAccountsConfig.setAccessType(accessData.getAccessType());
                    financeAccountsConfig.setFkFinanceAccSetupId(savedFinanceAccountSetup.getAccSetupId());
                    financeAccountsConfig.setInstituteId(savedFinanceAccountSetup.getInstituteId());
                    financeAccountsConfig.setIsActive(true);
                    financeAccountsConfig.setStatus(savedFinanceAccountSetup.getStatus());
                    financeAccountsConfig.setAccessStatus(accessData.getAccessStatus());
                    financeAccountsConfig.setTitle(accessTypeDTO.getTitle());

                    configList.add(financeAccountsConfig);
                });
            });


            iFinanceAccountsConfigRepo.saveAllAndFlush(configList);


            return new Response<>(0, AppConstants.DATA_SAVED_SUCCESSFULLY.getMessage(), null);

        } catch (RecordNotFoundException e) {
            log.error("Record not found for AccSetup ID: {}", cashierDataDTO.getAccSetupId(), e);
            return new Response<>(1, AppConstants.RECORD_NOT_FOUND.getMessage() + cashierDataDTO.getAccSetupId(), null);
        } catch (Exception e) {
            log.error("Error occurred while processing Cashier with ID: {}", cashierDataDTO.getAccSetupId(), e);
            return new Response<>(1, AppConstants.PROCESSING_ERROR.getMessage(), e.getMessage());
        }
    }
	 public List<AddOnDiscountResponseDTO> getAllAddOnDiscountData(Long instituteId) {
	        log.info("Fetching all add-on discount data for institute ID: {}", instituteId);
	        
	        List<AddOnDiscount> listData = iAddOnDiscountRepo.findByFkInstituteIdAndIsActiveTrue(instituteId);
	        log.info("Found {} active add-on discounts for institute ID: {}", listData.size(), instituteId);

	        List<AddOnDiscountResponseDTO> response = listData.stream()
	                .map(this::convertEntityToAddOnDiscountResponseDTO)
	                .collect(Collectors.toList());
	        
	        log.info("Converted {} add-on discounts to response DTOs", response.size());
	        return response;
	    }

	    private AddOnDiscountResponseDTO convertEntityToAddOnDiscountResponseDTO(AddOnDiscount addOnDiscount) {
	        log.debug("Converting add-on discount entity to DTO: {}", addOnDiscount);

	        ApiResponseDTO apiResponseDTO = fetchUserRolesWithPrivilege(addOnDiscount.getFkInstituteId());
	        Optional<UserRolePrivilegeDTO> userRolePrivilegeDTOOpt = filterByUserId(apiResponseDTO.getData(), addOnDiscount.getUserId());

	        log.debug("UserRolePrivilegeDTO fetched for user ID {}: {}", addOnDiscount.getUserId(), userRolePrivilegeDTOOpt);

	        StudentInfoDTO studentInfoDTO = fetchStudentInfo(addOnDiscount.getFkInstituteId(), addOnDiscount.getFkStudentId());
	        List<AddOnDiscountDTO> listAddOnDiscountDTO = convertJsonToListAddOnDiscountDTO(addOnDiscount.getAddOnDiscount());

	        List<String> feeheadNames = extractFeeHeadNames(listAddOnDiscountDTO);
	        List<String> feeTypeNames = extractFeeTypeNames(listAddOnDiscountDTO);
	        List<String> roles = extractRoles(userRolePrivilegeDTOOpt.orElse(null), listAddOnDiscountDTO);
	        List<Double> discounts = extractDiscounts(listAddOnDiscountDTO);

	        AddOnDiscountResponseDTO responseDTO = buildAddOnDiscountResponseDTO(
	                studentInfoDTO,
	                addOnDiscount,
	                userRolePrivilegeDTOOpt.orElse(null),
	                feeheadNames,
	                feeTypeNames,
	                roles,
	                discounts
	        );

	        log.debug("Created AddOnDiscountResponseDTO: {}", responseDTO);
	        return responseDTO;
	    }

	    private ApiResponseDTO fetchUserRolesWithPrivilege(Long instituteId) {
	        log.info("Fetching user roles with privilege for institute ID: {}", instituteId);
	        try {
	            ApiResponseDTO response = webClientService.getUserRolesWithPrivilege(instituteId);
	            log.debug("User roles with privilege fetched successfully for institute ID: {}", instituteId);
	            return response;
	        } catch (Exception e) {
	            log.error("Error fetching user roles with privilege for institute ID: {}", instituteId, e);
	            throw new InternalServerException(AppConstants.USER_ROLES_PRIVILEGE_FETCH_FAILED.getMessage(), e);
	        }
	    }

	    private StudentInfoDTO fetchStudentInfo(Long instituteId, Long studentId) {
	        log.info("Fetching student info for student ID: {} and institute ID: {}", studentId, instituteId);
	        try {
	            StudentInfoDTO studentInfo = webClientService.getStudentInfo(instituteId, studentId);
	            log.debug("Student info fetched successfully: {}", studentInfo);
	            return studentInfo;
	        } catch (Exception e) {
	            log.error("Error fetching student info for student ID: {} and institute ID: {}", studentId, instituteId, e);
	            throw new InternalServerException(AppConstants.STUDENT_INFO_FETCH_FAILED.getMessage(), e);
	        }
	    }

	    private List<String> extractFeeHeadNames(List<AddOnDiscountDTO> listAddOnDiscountDTO) {
	        log.debug("Extracting fee head names from list of AddOnDiscountDTO");
	        return listAddOnDiscountDTO.stream()
	                .map(AddOnDiscountDTO::getFeeHeadName)
	                .collect(Collectors.toList());
	    }

	    private List<String> extractFeeTypeNames(List<AddOnDiscountDTO> listAddOnDiscountDTO) {
	        log.debug("Extracting fee type names from list of AddOnDiscountDTO");
	        return listAddOnDiscountDTO.stream()
	                .map(feeTypeDTO -> {
	                    try {
	                        return iFeeHeadMasterRepo.findById(feeTypeDTO.getFeeHeadId())
	                                .flatMap(feeHeadMaster -> iFeeTypeMasterRepo.findById(feeHeadMaster.getFeeTypeId()))
	                                .map(FeeTypeMaster::getFeeType)
	                                .orElse(null);
	                    } catch (Exception e) {
	                        log.error("Error fetching fee type for FeeHeadId: {}", feeTypeDTO.getFeeHeadId(), e);
	                        return null;
	                    }
	                })
	                .filter(Objects::nonNull)
	                .collect(Collectors.toList());
	    }

	    private List<String> extractRoles(UserRolePrivilegeDTO userRolePrivilegeDTO, List<AddOnDiscountDTO> listAddOnDiscountDTO) {
	        log.debug("Extracting roles from userRolePrivilegeDTO: {}", userRolePrivilegeDTO);
	        if (userRolePrivilegeDTO == null) {
	            return List.of();
	        }
	        return listAddOnDiscountDTO.stream()
	                .flatMap(dto -> userRolePrivilegeDTO.getListOfRolesAndPrivilege().stream()
	                        .map(RolePrivilegeDTO::getRoleName))
	                .collect(Collectors.toList());
	    }

	    private List<Double> extractDiscounts(List<AddOnDiscountDTO> listAddOnDiscountDTO) {
	        log.debug("Extracting discounts from list of AddOnDiscountDTO");
	        return listAddOnDiscountDTO.stream()
	                .map(AddOnDiscountDTO::getDiscount)
	                .collect(Collectors.toList());
	    }

	    private AddOnDiscountResponseDTO buildAddOnDiscountResponseDTO(
	            StudentInfoDTO studentInfoDTO,
	            AddOnDiscount addOnDiscount,
	            UserRolePrivilegeDTO userRolePrivilegeDTO,
	            List<String> feeheadNames,
	            List<String> feeTypeNames,
	            List<String> roles,
	            List<Double> discounts) {

	        log.debug("Building AddOnDiscountResponseDTO with studentInfoDTO: {}, addOnDiscount: {}, userRolePrivilegeDTO: {}",
	                studentInfoDTO, addOnDiscount, userRolePrivilegeDTO);

	        return AddOnDiscountResponseDTO.builder()
	                .className(studentInfoDTO.getClassName())
	                .dateTime(addOnDiscount.getCreatedDate())
	                .discount(discounts)
	                .empId(userRolePrivilegeDTO != null ? userRolePrivilegeDTO.getProfileId() : null)
	                .empImg(userRolePrivilegeDTO != null ? userRolePrivilegeDTO.getUserPhoneNo() : null)
	                .employeeName(userRolePrivilegeDTO != null ? userRolePrivilegeDTO.getUserName() : null)
	                .feeHeadName(feeheadNames)
	                .feeTypeName(feeTypeNames)
	                .roles(roles)
	                .section(studentInfoDTO.getSection())
	                .studentId(studentInfoDTO.getStudentId())
	                .studentName(studentInfoDTO.getStudentName())
	                .studentImg(studentInfoDTO.getStudentProfilePic())
	                .id(addOnDiscount.getId())
	                .studentRollNo(studentInfoDTO.getStudentRollNo())
	                .build();
	    }

	    public Optional<UserRolePrivilegeDTO> filterByUserId(List<UserRolePrivilegeDTO> listData, Long userId) {
	        log.debug("Filtering user roles by user ID: {}", userId);
	        return listData.stream()
	                .filter(dto -> dto.getAppUserId().equals(userId))
	                .findFirst();
	    }
		@Override
		 public List<DiscountResponseDTO> getAllDiscountsByStudentId(Long studentId, Long instituteId) {
	        log.info("Fetching discounts for studentId: {} and instituteId: {}", studentId, instituteId);

	        List<AddOnDiscount> discountEntities;
	        List<DiscountResponseDTO> discountResponseList = new ArrayList<>();
	        StudentInfoDTO studentInfoDTO;
	        try {
	        	studentInfoDTO=webClientService.getStudentInfo(instituteId, studentId);
	        	
	        	String jsonProfileDiscount=studentInfoDTO.getProfieDiscountId();
	        	if(jsonProfileDiscount!=null) {
	        	List<Long> profileDiscountIds=convertJsonStringToList(jsonProfileDiscount) ;
	        	
	        	profileDiscountIds.forEach(ids->{
	        		
	        		ProfileDiscount profileDiscount=iProfileDiscountRepo.findById(ids).get();
	        		
	        		List<ProfileDiscountDTO> profileDiscountDTO=convertJsonToListProfileDiscountDTO(profileDiscount.getDiscountProfile());
	        		profileDiscountDTO.forEach(dto->{
	        		 DiscountResponseDTO discountResponse = DiscountResponseDTO.builder()
		                        .disCountHeadname(profileDiscount.getFeeProfiletittle())
		                        .discounId(ids)
		                        .discountRate(dto.getRate())
		                        .discountType(AppConstants.PROFILE_DISCOUNT.getMessage())
		                        .feeHeadName(dto.getFeeTypeName())
		                        .discountAmountType(dto.getDiscountType())
		                        .build();
		                discountResponseList.add(discountResponse);
		                
	        		});
	        		
	        	});
	        	
	        	
	        	}
	            discountEntities = iAddOnDiscountRepo.findByFkStudentIdAndFkInstituteIdAndIsActiveTrue(studentId, instituteId);
	            log.debug("Retrieved {} discount entities for studentId: {} and instituteId: {}", discountEntities.size(), studentId, instituteId);
	        } catch (Exception e) {
	            log.error("Error retrieving discount entities for studentId: {} and instituteId: {}", studentId, instituteId, e);
	            throw new DiscountRetrievalException("An error occurred while fetching discounts.", e); 
	        }

	       
	        
	        discountEntities.forEach(discountEntity -> {
	            AddOnDiscountDataDTO discountDataDTO = convertToAddOnDiscountDataDTO(discountEntity);
	            
	            discountDataDTO.getAddOnDiscountList().forEach(discountData -> {
	                DiscountResponseDTO discountResponse = DiscountResponseDTO.builder()
	                        .disCountHeadname(discountData.getDiscountHeadName())
	                        .discounId(discountEntity.getId())
	                        .discountRate(discountData.getDiscount())
	                        .discountType(AppConstants.DISCOUNT_TYPE.getMessage())
	                        .feeHeadName(discountData.getFeeHeadName())
	                        .discountAmountType("Flat Amount")
	                        .build();
	                discountResponseList.add(discountResponse);
	            });
	        });

	        log.info("Successfully processed {} discount responses for studentId: {} and instituteId: {}", discountResponseList.size(), studentId, instituteId);
	        return discountResponseList;
	    }

    @Override
    public List<BankDTO> getBankDetails(Long instituteId) {
        List<FinanceAccountSetup> listFinanceAccountSetup=financeAccountSetupRepo.findByInstituteIdAndIsActiveTrue(instituteId);
        if(!listFinanceAccountSetup.isEmpty()) {
            return listFinanceAccountSetup.stream().map(feeTypeMaster->{
                BankDTO bankDTO=new BankDTO();
                BeanUtils.copyProperties(feeTypeMaster, bankDTO);
                return bankDTO;

            }).collect(Collectors.toList());
        }
        return null;
    }

@Override
    public BankDTO saveOrUpdateBankDetails(BankDTO bankDTO) {
        if (bankDTO.getAccSetupId() != null) {
            log.info("Attempting to update bank details with accSetupId: {}", bankDTO.getAccSetupId());

            Optional<FinanceAccountSetup> existingRecord = financeAccountSetupRepo.findById(bankDTO.getAccSetupId());
            if (existingRecord.isPresent()) {
                FinanceAccountSetup updatedRecord = financeAccountSetupRepo.save(convertToEntity(bankDTO));
                return convertToDTO(updatedRecord);
            } else {
                log.warn("No existing record found for accSetupId: {}, cannot update.", bankDTO.getAccSetupId());
                return null;
            }
        } else {
            log.info("Creating new bank details for instituteId: {}", bankDTO.getInstituteId());
            FinanceAccountSetup newRecord = financeAccountSetupRepo.save(convertToEntity(bankDTO));
            return convertToDTO(newRecord);
        }
    }

    private FinanceAccountSetup convertToEntity(BankDTO dto) {
        return FinanceAccountSetup.builder()
                .accSetupId(dto.getAccSetupId())
                .empId(dto.getEmpId())
                .openingBalance(dto.getOpeningBalance())
                .status(dto.getStatus())
                .bankName(dto.getBankName())
                .bankAccountNo(dto.getBankAccountNo())
                .bankLogo(dto.getBankLogo())
                .instituteId(dto.getInstituteId())
                .isActive(true)
                .build();
    }
@Override
public Response<?> deactivateBank(Long id, Long instituteId) {
    Optional<FinanceAccountSetup> financeAccountOpt = financeAccountSetupRepo.findByAccSetupIdAndInstituteIdAndIsActiveTrue(id, instituteId);

    if (financeAccountOpt.isPresent()) {
        FinanceAccountSetup financeAccount = financeAccountOpt.get();
        financeAccount.setIsActive(false);
        financeAccountSetupRepo.save(financeAccount);

        log.info("Deactivated bank account with ID: {} for institute ID: {}", id, instituteId);

        return new Response<>(200, "Bank account deactivated successfully");
    } else {
        log.warn("Bank account with ID: {} and institute ID: {} not found or already inactive", id, instituteId);
        return new Response<>(404, "Bank account not found or already inactive");
    }
}
@Override
public Response<?> addOrUpdatePaymentModeConfig(List<PaymentModeConfigDTO> listPaymentModeConfigDTO) throws Exception{
	 log.info("Starting the process to add or update Payment Mode Config. Total records: {}", listPaymentModeConfigDTO.size());
	 List<PaymentModeConfig> listdata= listPaymentModeConfigDTO.stream().map(paymentModeconfigDTO->{
		PaymentModeConfig paymentModeConfig;

        if (paymentModeconfigDTO.getId() != null) {
            paymentModeConfig = paymentModeConfigRepo
                .findByIdAndInstituteIdAndIsActiveTrue(paymentModeconfigDTO.getId(), paymentModeconfigDTO.getInstituteId())
                .orElse(new PaymentModeConfig());
        } else {
        	log.info("ID is null in DTO. Creating a new PaymentModeConfig entity.");
            paymentModeConfig = new PaymentModeConfig();
        }
        
        BeanUtils.copyProperties(paymentModeconfigDTO, paymentModeConfig);
        
        return paymentModeConfig;
        
	}).collect(Collectors.toList());
	 log.info("Successfully saved {} Payment Mode Config entities.", listdata.size());

	 paymentModeConfigRepo.saveAll(listdata);
	
	return new Response<>(AppConstants.SUCCESS_CODE.getCode(),"Payment Mode Config Saved Successfully");
}

@Override
public Response<?> getPaymentModeConfigById(Long id, Long instituteId) {
    log.info("Attempting to retrieve Payment Mode Config with ID: {} and Institute ID: {}", id, instituteId);

    Optional<PaymentModeConfig> optional = paymentModeConfigRepo.findByIdAndInstituteIdAndIsActiveTrue(id, instituteId);
    
    if (optional.isPresent()) {
        PaymentModeConfigDTO paymentModeConfigDTO = PaymentModeConfigDTO.builder().build();
        BeanUtils.copyProperties(optional.get(), paymentModeConfigDTO);

        log.info("Payment Mode Config retrieved successfully for ID: {} and Institute ID: {}", id, instituteId);
        return new Response<>(AppConstants.SUCCESS_CODE.getCode(), "Data retrieved successfully", paymentModeConfigDTO);
    } else {
        log.warn("No Payment Mode Config found with ID: {} and Institute ID: {}", id, instituteId);
        return new Response<>(AppConstants.FAILURE_CODE.getCode(), "Data not found with the provided ID");
    }
}
@Override
public Response<?> getAllPaymentModeConfig(Long instituteId) {
	List<PaymentModeConfig> listData=paymentModeConfigRepo.findByInstituteIdAndIsActiveTrue(instituteId);
	if(!listData.isEmpty()) {
		List<PaymentModeConfigDTO> listDataDTO=listData.stream().map(entity->{
			PaymentModeConfigDTO paymentModeConfigDTO = PaymentModeConfigDTO.builder().build();
			BeanUtils.copyProperties(entity, paymentModeConfigDTO);
			return paymentModeConfigDTO;
		}).collect(Collectors.toList());
		return new Response<>(AppConstants.SUCCESS_CODE.getCode(),"Data Retrieved SuccessFully",listDataDTO);
	}else {
		
		
	}
	
	return new Response<>(AppConstants.FAILURE_CODE.getCode(),AppConstants.DATA_NOT_FOUND.getMessage()+instituteId);
}
@Override
public Response<?> deactivatePaymentModeconfig(Long id, Long instituteId) {
Optional<PaymentModeConfig> optional = paymentModeConfigRepo.findByIdAndInstituteIdAndIsActiveTrue(id, instituteId);
    
    if (optional.isPresent()) {
    	PaymentModeConfig paymentModeConfig=optional.get();
    	if(paymentModeConfig.getIsDefault()) {
    		return new Response<>(AppConstants.FAILURE_CODE.getCode(),"You cant delete system generated fields ");
    	}
    	paymentModeConfig.setIsActive(false);
    	paymentModeConfigRepo.save(paymentModeConfig);
        log.info("Payment Mode Config retrieved successfully for ID: {} and Institute ID: {}", id, instituteId);
        return new Response<>(AppConstants.SUCCESS_CODE.getCode(), "Record Deactivated SuccessFully" );
    } else {
        log.warn("No Payment Mode Config found with ID: {} and Institute ID: {}", id, instituteId);
        return new Response<>(AppConstants.FAILURE_CODE.getCode(), "Data not found with the provided ID");
    }
	
	
}
@Override
public Response<?> getAllDefaultFeeSettingsConfig(Long instituteId) {
	log.info("Fetching Default Fee Settings Config for Institute ID: {}", instituteId);
	Optional<DefaultFeeSettingsConfig> optional=defaultFeeSettingsConfigRepo.findByInstituteIdAndIsActiveTrue(instituteId);
	
	if(optional.isEmpty()) {
		return new Response<>(AppConstants.SUCCESS_CODE.getCode(),"Data Fetcherd SuccessFully",new DefaultFeeSettingsConfigDTO());
		
	}else {
		 log.info("Default Fee Settings Config retrieved successfully for Institute ID: {}", instituteId);
		DefaultFeeSettingsConfigDTO defaultFeeSettingsConfigDTO=DefaultFeeSettingsConfigDTO.builder().build();
		BeanUtils.copyProperties(optional.get(), defaultFeeSettingsConfigDTO);
	
	return new Response<>(AppConstants.SUCCESS_CODE.getCode(),"Data Fetcherd SuccessFully",defaultFeeSettingsConfigDTO);
	}
}
@Override
public Response<?> addOrUpdateDefaultFeeSettingsConfig(DefaultFeeSettingsConfigDTO defaultFeeSettingsConfigDTO) {
	
	DefaultFeeSettingsConfig defaultFeeSettingsConfig;

    if (defaultFeeSettingsConfigDTO.getId() != null) {
    	defaultFeeSettingsConfig = defaultFeeSettingsConfigRepo.findByInstituteIdAndIsActiveTrue(defaultFeeSettingsConfigDTO.getInstituteId())
    			.orElse(new DefaultFeeSettingsConfig());
    } else {
    	log.info("ID is null in DTO. Creating a new DefaultFeeSettingsConfig entity.");
    	defaultFeeSettingsConfig = new DefaultFeeSettingsConfig();
    }
    BeanUtils.copyProperties(defaultFeeSettingsConfigDTO, defaultFeeSettingsConfig);
    
    log.info("--------------------- {}",defaultFeeSettingsConfig);
    defaultFeeSettingsConfigRepo.save(defaultFeeSettingsConfig);
	
	return new Response<>(AppConstants.SUCCESS_CODE.getCode(),"Data Saved SuccessFully");
}
//reminder config operation started

@Override
public Response<?> addOrUpdateReminderConfig(List<ReminderConfigDTO> listDTO) {
    log.info("Starting addOrUpdateReminderConfig with {} DTOs", listDTO.size());

    List<ReminderConfig> listEntity = listDTO.stream().map(reminderConfigDTO -> {
        ReminderConfig reminderConfig;
        if (reminderConfigDTO.getId() != null) {
            log.debug("Updating ReminderConfig with ID: {}", reminderConfigDTO.getId());
            reminderConfig = reminderConfigRepo.findByIdAndInstituteIdAndIsActiveTrue(
                    reminderConfigDTO.getId(), 
                    reminderConfigDTO.getInstituteId()
            ).orElse(new ReminderConfig());
        } else {
            log.debug("Creating new ReminderConfig");
            reminderConfig = new ReminderConfig();
        }
      //  if(!reminderConfig.getActivityType().equalsIgnoreCase(reminderConfigDTO.getActivityType())) {
        	
        	
     //   }
        BeanUtils.copyProperties(reminderConfigDTO, reminderConfig);
        return reminderConfig;
    }).collect(Collectors.toList());

    try {
        reminderConfigRepo.saveAll(listEntity);
        log.info("Successfully saved {} ReminderConfig entities", listEntity.size());
        return new Response<>(AppConstants.SUCCESS_CODE.getCode(), AppConstants.DATA_SAVED_SUCCESSFULLY.getMessage());
    } catch (Exception e) {
        log.error("Failed to save ReminderConfig entities", e);
        return new Response<>(AppConstants.FAILURE_CODE.getCode(), AppConstants.FAILED_TO_SAVE_DATA.getMessage(), e.getMessage());
    }
}
@Override
public Response<?> getReminderConfigById(Long id, Long instituteId) {
    log.info("Fetching ReminderConfig by ID: {} and Institute ID: {}", id, instituteId);
    
    Optional<ReminderConfig> optional = reminderConfigRepo.findByIdAndInstituteIdAndIsActiveTrue(id, instituteId);
    
    if (optional.isPresent()) {
        ReminderConfig reminderConfig = optional.get();
        ReminderConfigDTO dto = ReminderConfigDTO.builder().build();
        BeanUtils.copyProperties(reminderConfig, dto);
        
        log.info("ReminderConfig found for ID: {} and Institute ID: {}", id, instituteId);
        return new Response<>(AppConstants.SUCCESS_CODE.getCode(), AppConstants.DATA_RERIEVED_SUCCESSFULLY.getMessage(), dto);
    } else {
    	log.warn("ReminderConfig not found for ID: {} and Institute ID: {}", id, instituteId);
        return new Response<>(AppConstants.FAILURE_CODE.getCode(), AppConstants.DATA_NOT_FOUND_WITH_ID.getMessage()+id);
    }
}

@Override
public Response<?> getAllReminderConfig(Long instituteId) {
	log.info("Fetching all ReminderConfigs for Institute ID: {}", instituteId);
    
    List<ReminderConfig> listData = reminderConfigRepo.findByInstituteIdAndIsActiveTrue(instituteId);
    
    if (!listData.isEmpty()) {
        List<ReminderConfigDTO> listDTOData = listData.stream().map(reminderConfig -> {
            ReminderConfigDTO dto = ReminderConfigDTO.builder().build();
            BeanUtils.copyProperties(reminderConfig, dto);
            return dto;
        }).collect(Collectors.toList());
        
        log.info("Successfully retrieved {} ReminderConfigs for Institute ID: {}", listDTOData.size(), instituteId);
        return new Response<>(AppConstants.SUCCESS_CODE.getCode(), AppConstants.DATA_RERIEVED_SUCCESSFULLY.getMessage(), listDTOData);
    } else {
    	log.warn("No active ReminderConfigs found for Institute ID: {}", instituteId);
        return new Response<>(AppConstants.FAILURE_CODE.getCode(), AppConstants.DATA_NOT_FOUND.getMessage()+instituteId);
    }
}

@Override
public Response<?> deactivateReminderConfig(Long id, Long instituteId) {
	log.info("Deactivating ReminderConfig with ID: {} for Institute ID: {}", id, instituteId);
    
    Optional<ReminderConfig> optional = reminderConfigRepo.findByIdAndInstituteIdAndIsActiveTrue(id, instituteId);
    
    if (optional.isPresent()) {
        ReminderConfig reminderConfig = optional.get();
        
        if (reminderConfig.getIsDefault()) {
        	log.warn("Attempted to deactivate a system-generated ReminderConfig with ID: {}", id);
            return new Response<>(AppConstants.FAILURE_CODE.getCode(), "You can't delete system-generated fields.");
        }
        
        reminderConfig.setIsActive(false);
        reminderConfigRepo.save(reminderConfig);
        
        log.info("Successfully deactivated ReminderConfig with ID: {}", id);
        return new Response<>(AppConstants.SUCCESS_CODE.getCode(), AppConstants.RECORD_DEACTIVATION_SUCESSFULL.getMessage());
    } else {
    	log.warn("ReminderConfig not found for deactivation with ID: {} and Institute ID: {}", id, instituteId);
        return new Response<>(AppConstants.FAILURE_CODE.getCode(), AppConstants.RECORD_NOT_FOUND.getMessage()+id);
    }
}


     @Override
public void deactivateFinanceAccountSetupById(Long accSetupId, Long instituteId) {

    // Retrieve FinanceAccountSetup by ID
    FinanceAccountSetup financeAccountSetup = financeAccountSetupRepo.findById(accSetupId)
            .orElseThrow(() -> new EntityNotFoundException("FinanceAccountSetup not found for id: " + accSetupId));

    // Check if the FinanceAccountSetup belongs to the provided instituteId
    if (!financeAccountSetup.getInstituteId().equals(instituteId)) {
        throw new EntityNotFoundException("No FinanceAccountSetup found for this instituteId: " + instituteId);
    }

    // Deactivate the FinanceAccountSetup
    financeAccountSetup.setIsActive(false);

    // Find associated FinanceAccountsConfig and deactivate them
    List<FinanceAccountsConfig> configs = iFinanceAccountsConfigRepo.findByFkFinanceAccSetupIdAndIsActiveTrue(accSetupId);

    configs.forEach(config -> {
        if (config.getInstituteId().equals(instituteId)) {
            config.setIsActive(false);
            iFinanceAccountsConfigRepo.save(config); // Deactivate individual config
        }
    });

    // Save deactivated FinanceAccountSetup
    financeAccountSetupRepo.save(financeAccountSetup);
}


      public List<FinanceAccountSetupDTO> getFinanceAccountSetupsWithConfigs(Long instituteId) {
               List<FinanceAccountSetupDTO> setupList = new ArrayList<>();
    
    
             List<FinanceAccountSetup> setupEntities = financeAccountSetupRepo.findByInstituteId(instituteId);
    
        for (FinanceAccountSetup setupEntity : setupEntities) {
      
         FinanceAccountSetupDTO setupDTO = FinanceAccountSetupDTO.builder()
            .accSetupId(setupEntity.getAccSetupId())
            .empId(setupEntity.getEmpId())
            .openingBalance(setupEntity.getOpeningBalance())
            .status(setupEntity.getStatus())
            .bankName(setupEntity.getBankName())
            .bankAccountNo(setupEntity.getBankAccountNo())
            .bankLogo(setupEntity.getBankLogo())
            .instituteId(setupEntity.getInstituteId())
            .isActive(setupEntity.getIsActive())
            .accountConfigs(new ArrayList<>()) // Initialize with an empty list
            .build();

        
        List<FinanceAccountsConfig> configEntities = iFinanceAccountsConfigRepo.findByFkFinanceAccSetupIdAndIsActiveTrue(setupEntity.getAccSetupId());
        
        
        List<FinanceAccountsConfigDTO> configDTOs = configEntities.stream().map(configEntity -> FinanceAccountsConfigDTO.builder()
            .accConfigId(configEntity.getAccConfigId())
            .fkFinanceAccSetupId(configEntity.getFkFinanceAccSetupId())
            .accessType(configEntity.getAccessType())
            .title(configEntity.getTitle())
            .status(configEntity.getStatus())
            .accessStatus(configEntity.getAccessStatus())
            .instituteId(configEntity.getInstituteId())
            .isActive(configEntity.getIsActive())
            .build()
        ).collect(Collectors.toList());

        setupDTO.setAccountConfigs(configDTOs); 
        
        setupList.add(setupDTO);
    }
    
    return setupList;
}

    @Override
    public List<FeeConfig> getAllFeeConfig(String type) {
        if (type != null && !type.isEmpty()) {
            return iFeeConfigRepo.findByTypeAndIsActiveTrue(type);  // Filter by type
        } else {
            return iFeeConfigRepo.findAll();  // Return all records if type is not provided
        }
    }
//@Override
//    public List<CashierDataDTO> getCashierDataByInstituteId(Long instituteId) {
//        log.info("Fetching Cashier Data for Institute ID: {}", instituteId);
//
//        List<FinanceAccountSetup> accountSetups = financeAccountSetupRepo.findByInstituteIdAndIsActiveTrue(instituteId);
//
//        if (accountSetups.isEmpty()) {
//            log.warn("No active Cashier Data found for Institute ID: {}", instituteId);
//        } else {
//            log.info("Successfully fetched {} Cashier Data records for Institute ID: {}", accountSetups.size(), instituteId);
//        }
//
//        return accountSetups.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    private CashierDataDTO convertToDTO(FinanceAccountSetup entity) {
//        return CashierDataDTO.builder()
//                .accSetupId(entity.getAccSetupId())
//                .instituteId(entity.getInstituteId())
//                .openingBalance(entity.getOpeningBalance())
//                .status(entity.getStatus())
//                .empId(entity.getEmpId())
//                .aceessData(entity.getAccountsConfigList().stream()
//                        .map(config -> AccessDataDTO.builder()
//                                .id(config.getAccConfigId())
//                                .accessType(config.getAccessType())
//                                .accessStatus(config.getAccessStatus())
//                                .accessDTO(config.getTitle().stream()
//                                        .map(title -> AccessDTO.builder()
//                                                .id(config.getAccConfigId())
//                                                .title(config.getTitle())
//                                                .status(config.getStatus())
//                                                .build())
//                                        .collect(Collectors.toList()))
//                                .build())
//                        .collect(Collectors.toList()))
//                .build();
//    }
    
    public CashierDataDTO getCashierByAccSetupId(Long instituteId, Long accSetupId) {
        log.info("Fetching FinanceAccountSetup for instituteId: {} and accSetupId: {}", instituteId, accSetupId);

        FinanceAccountSetup financeAccountSetup = financeAccountSetupRepo
            .findByAccSetupIdAndInstituteIdAndIsActiveTrue(instituteId, accSetupId)
            .orElseThrow(() -> {
                log.error("FinanceAccountSetup not found for accSetupId: {}", accSetupId);
                return new RecordNotFoundException("FinanceAccountSetup not found for ID: " + accSetupId);
            });

        log.info("FinanceAccountSetup found: {}", financeAccountSetup);

        List<FinanceAccountsConfig> financeAccountsConfigs = iFinanceAccountsConfigRepo
            .findByFkFinanceAccSetupIdAndIsActiveTrue(accSetupId);

        log.info("Found {} FinanceAccountsConfig records for accSetupId: {}", financeAccountsConfigs.size(), accSetupId);

        CashierDataDTO cashierDataDTO = new CashierDataDTO();
        cashierDataDTO.setAccSetupId(financeAccountSetup.getAccSetupId());
        cashierDataDTO.setEmpId(financeAccountSetup.getEmpId());
        cashierDataDTO.setInstituteId(financeAccountSetup.getInstituteId());
        cashierDataDTO.setOpeningBalance(financeAccountSetup.getOpeningBalance());
        cashierDataDTO.setStatus(financeAccountSetup.getStatus());

        cashierDataDTO.setAceessData(mapToAccessDataDTO(financeAccountsConfigs));

        log.info("Returning CashierDataDTO: {}", cashierDataDTO);
        return cashierDataDTO;
    }

    private List<AccessDataDTO> mapToAccessDataDTO(List<FinanceAccountsConfig> financeAccountsConfigs) {
        log.info("Mapping {} FinanceAccountsConfig to AccessDataDTO", financeAccountsConfigs.size());

        return financeAccountsConfigs.stream().map(config -> {
            AccessDataDTO accessDataDTO = new AccessDataDTO();
            accessDataDTO.setId(config.getAccConfigId());
            accessDataDTO.setAccessType(config.getAccessType());
            accessDataDTO.setAccessStatus(config.getAccessStatus());

            // Check for null values before mapping
            String title = config.getTitle();
            Boolean status = config.getStatus();

            log.debug("Mapping AccessDTO with id: {}, title: {}, status: {}", config.getAccConfigId(), title, status);

            List<AccessDTO> accessDTOList = List.of(
                new AccessDTO(config.getAccConfigId(), title, status)
            );

            accessDataDTO.setAccessDTO(accessDTOList);
            return accessDataDTO;
        }).collect(Collectors.toList());
    }
    
    public static List<Long> convertJsonStringToList(String jsonString) {
       
        List<Long> longList = null;
        
        try {
            // Convert the JSON string to a List<Long> using TypeReference
            longList = objectMapper.readValue(jsonString, new TypeReference<List<Long>>() {});
        } catch (Exception e) {
            e.printStackTrace(); // Handle parsing errors appropriately
        }
        return longList;
    }
	@Override
	public Double getFeeAmountByFeeHead(Long instituteId, Long FeeHeadId) {
		Double amount=0.0;
		List<FeeMasterDetails> listData=iFeeMasterDetailsRepo.findByFeeHeadIdAndInstituteIdAndIsActiveTrue(FeeHeadId, instituteId);
		for (FeeMasterDetails data : listData) {
			amount += data.getFeeAmount();
		}
		return amount;
	}

	@Override
	public List<FeeHeadDetailsDTO> getAllDistinctFeeHeadsInFeeSetup(Long instituteId) {
		List<Long> dicstinctIds=iFeeMasterDetailsRepo.findDistinctFeeHeadIdsByInstituteId(instituteId);
		
		return dicstinctIds.stream().map(id->{
			Double amount=0.0;
			FeeHeadMaster master=iFeeHeadMasterRepo.findByFeeHeadIdAndInstituteIdAndIsActiveTrue(id, instituteId).get();
			
			List<FeeMasterDetails> listData=iFeeMasterDetailsRepo.findByFeeHeadIdAndInstituteIdAndIsActiveTrue(id, instituteId);
			for (FeeMasterDetails data : listData) {
			    amount = amount + data.getFeeAmount();
			}
			
			return FeeHeadDetailsDTO.builder()
					.feeHeadId(id)
					.feeHeadName(master.getFeeHeadName())
					.totalAmount(amount)
					.build();
			
		}).collect(Collectors.toList());
		
	}
    @Override
    public List<CashierDataDTO> getCashierByAccSetupAndInstitute(Long accSetupId, Long instituteId) {
        // Fetch the FinanceAccountSetup entity
        Optional<FinanceAccountSetup> accountSetupOptional = financeAccountSetupRepo.findByAccSetupIdAndInstituteIdAndIsActiveTrue(instituteId, accSetupId);
        if (accountSetupOptional.isPresent()) {
            FinanceAccountSetup accountSetup = accountSetupOptional.get();


            // Fetch FinanceAccountsConfig entries related to the FinanceAccountSetup
            List<FinanceAccountsConfig> configList = iFinanceAccountsConfigRepo.findByFkFinanceAccSetupIdAndIsActiveTrue(accSetupId);

            // Map FinanceAccountsConfig to AccessDataDTO and AccessDTO
            List<AccessDataDTO> accessDataDTOList = configList.stream()
                    .map(config -> AccessDataDTO.builder()
                            .id(config.getAccConfigId())
                            .accessType(config.getAccessType())
                            .accessStatus(config.getAccessStatus())
                            .accessDTO(List.of(AccessDTO.builder()
                                    .id(config.getAccConfigId())
                                    .title(config.getTitle())
                                    .status(config.getStatus())
                                    .build()))
                            .build())
                    .collect(Collectors.toList());
            List<AccessDataDTO> accessDTO=accessDataDTOList.stream().collect(Collectors.groupingBy(AccessDataDTO::getAccessType))
                    .entrySet()
                    .stream().map(entry -> new AccessDataDTO(
                            entry.getValue().get(0).getId(),
                            entry.getKey(),
                            entry.getValue().get(0).getAccessStatus(),
                            entry.getValue().stream()
                                    .flatMap(aceessData -> aceessData.getAccessDTO().stream())
                                    .collect(Collectors.toList())
                    ))
                    .collect(Collectors.toList());

            CashierDataDTO cashierData = CashierDataDTO.builder()
                    .accSetupId(accountSetup.getAccSetupId())
                    .instituteId(accountSetup.getInstituteId())
                    .openingBalance(accountSetup.getOpeningBalance())
                    .status(accountSetup.getStatus())
                    .empId(accountSetup.getEmpId())
                    .aceessData(accessDTO)
                    .build();

            return List.of(cashierData);
        }

        throw new EntityNotFoundException("Finance Account Setup not found for accSetupId: " + accSetupId + " and instituteId: " + instituteId);
    }

}

