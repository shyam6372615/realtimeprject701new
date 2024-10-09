package com.sparrow.Finance.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sparrow.Finance.dto.FeeReasonDTO;
import com.sparrow.finance.entity.FeeReasonEntity;
import com.sparrow.finance.repository.FeeReasonRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FeeReasonService {

	@Autowired
	private FeeReasonRepo feeReasonRepo;
	
	
	
	  public FeeReasonEntity addOrUpdateFeeReason(FeeReasonDTO feeReasonDTO) {
	        FeeReasonEntity feeReasonEntity;

	        if (feeReasonDTO.getReasonId() != null) {
	            
	            feeReasonEntity = feeReasonRepo.findById(feeReasonDTO.getReasonId())
	                    .orElseThrow(() -> new EntityNotFoundException("Fee Reason not found"));

	           
	            if (feeReasonEntity.getIsDefault() != null && feeReasonEntity.getIsDefault()) {
	                throw new IllegalArgumentException("Cannot update a Fee Reason marked as default");
	            }
	        } else {
	            
	            feeReasonEntity = new FeeReasonEntity();
	        }

	        
	        feeReasonEntity.setInstituteId(feeReasonDTO.getInstituteId());
	        feeReasonEntity.setType(feeReasonDTO.getType());
	        feeReasonEntity.setReasonType(feeReasonDTO.getReasonType());
	        feeReasonEntity.setIsDefault(feeReasonDTO.getIsDefault());

	        return feeReasonRepo.save(feeReasonEntity);
	    }
    

	  
	  
	  public boolean deleteFeeReasonByInstituteIdAndReasonId(Long instituteId, Long reasonId) {
		    Optional<FeeReasonEntity> optionalFeeReason = feeReasonRepo.findByInstituteIdAndReasonId(instituteId, reasonId);

		    if (optionalFeeReason.isPresent()) {
		        FeeReasonEntity feeReasonEntity = optionalFeeReason.get();
		        
		        
		        if (feeReasonEntity.getIsDefault()) {
		            
		            throw new IllegalStateException("Cannot delete a default reason.");
		        }

		       
		        feeReasonEntity.setIsActive(false); 
		        feeReasonRepo.save(feeReasonEntity);
		        return true;
		    }
		    return false;
		}

    
    
    public FeeReasonDTO getFeeReasonByInstituteIdAndReasonId(Long instituteId, Long reasonId) {
        FeeReasonEntity feeReasonEntity = feeReasonRepo.findByInstituteIdAndReasonIdAndIsActiveTrue(instituteId, reasonId)
                .orElseThrow(() -> new EntityNotFoundException("No active Fee Reason found with given instituteId and reasonId"));
        return mapToDTO(feeReasonEntity);
    }
    
    
    public List<FeeReasonDTO> getFeeReasonsByInstituteIdAndType(Long instituteId, String type) {
        List<FeeReasonEntity> feeReasonEntities = feeReasonRepo.findByInstituteIdAndTypeAndIsActiveTrue(instituteId, type);
        return feeReasonEntities.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private FeeReasonDTO mapToDTO(FeeReasonEntity feeReasonEntity) {
        FeeReasonDTO feeReasonDTO = new FeeReasonDTO();
        feeReasonDTO.setReasonId(feeReasonEntity.getReasonId());
        feeReasonDTO.setInstituteId(feeReasonEntity.getInstituteId());
        feeReasonDTO.setType(feeReasonEntity.getType());
        feeReasonDTO.setReasonType(feeReasonEntity.getReasonType());
        feeReasonDTO.setIsDefault(feeReasonEntity.getIsDefault());
        return feeReasonDTO;
    }

   
}
