package com.sparrow.Finance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sparrow.Finance.Exception.RecordNotFoundException;
import com.sparrow.Finance.dto.FeeDiscountDTO;
import com.sparrow.Finance.dto.FeeMasterGeneralSettingDTO;
import com.sparrow.Finance.dto.Response;
import com.sparrow.app.model.jpa.InstituteEntity;
import com.sparrow.finance.entity.FeeDiscountEntity;
import com.sparrow.finance.entity.FeeMasterGeneralSettingEntity;
import com.sparrow.finance.repository.IFeeDiscountRepo;
import com.sparrow.finance.repository.IFeeMasterGeneralSettingRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FeeDiscountService {

	@Autowired
	private IFeeDiscountRepo iFeeDiscountRepo;
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private IFeeMasterGeneralSettingRepo feeMasterGeneralSettingRepo;
	
	public FeeDiscountDTO addFeeDiscount(FeeDiscountDTO feeDiscountDTO) {
	    FeeDiscountEntity feeDiscountEntity;

	    if (feeDiscountDTO.getFeeDiscountHeadId() != null) {
	        FeeDiscountEntity existingEntity = iFeeDiscountRepo.findById(feeDiscountDTO.getFeeDiscountHeadId())
	                .orElse(null);  
	        if (existingEntity == null) {
	            throw new IllegalArgumentException("FeeDiscountEntity not found for ID: " + feeDiscountDTO.getFeeDiscountHeadId());
	        }

	        if (existingEntity.getIsDefault() != null && existingEntity.getIsDefault()) {
	            throw new IllegalArgumentException("Unable to update. The fee discount head is marked as default.");
	        }

	        feeDiscountEntity = existingEntity;
	    } else {
	        feeDiscountEntity = new FeeDiscountEntity();
	    }

	    feeDiscountEntity.setFkInstituteId(feeDiscountDTO.getFkInstituteId());
	    feeDiscountEntity.setDiscountHead(feeDiscountDTO.getDiscountHead());
	    feeDiscountEntity.setIsDefault(feeDiscountDTO.getIsDefault());
	    feeDiscountEntity.setIsActive(true);

	    FeeDiscountEntity savedEntity = iFeeDiscountRepo.save(feeDiscountEntity);

	    FeeDiscountDTO feeDiscount = new FeeDiscountDTO();
	    BeanUtils.copyProperties(savedEntity, feeDiscount);
	    return feeDiscount;
	}

	
	
	public List<FeeDiscountDTO> getAllFeeDiscount(Long instituteId) {
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<FeeDiscountDTO> cq = cb.createQuery(FeeDiscountDTO.class);
	    Root<FeeDiscountEntity> root = cq.from(FeeDiscountEntity.class);
	   
	    // Selecting fields for FeeDiscountDTO
	    cq.select(cb.construct(FeeDiscountDTO.class,
	           root.get("feeDiscountHeadId"),
	           root.get("fkInstituteId"),
	           root.get("discountHead"),
	           root.get("isDefault"),
	           root.get("isActive")
	    ));

	    // Create predicates
	    Predicate isActivePredicate = cb.isTrue(root.get("isActive"));
	    Predicate instituteIdPredicate = cb.equal(root.get("fkInstituteId"), instituteId);

	    // Applying predicates to the query
	    cq.where(cb.and(isActivePredicate, instituteIdPredicate));

	    // Executing query
	    return entityManager.createQuery(cq).getResultList();
	}

	public FeeDiscountDTO getFeeDiscountByIdAndInstituteId(Long feeDiscountHeadId, Long instituteId) {
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<FeeDiscountDTO> cq = cb.createQuery(FeeDiscountDTO.class);
	    Root<FeeDiscountEntity> root = cq.from(FeeDiscountEntity.class);

	    // Selecting fields for FeeDiscountDTO
	    cq.select(cb.construct(FeeDiscountDTO.class,
	           root.get("feeDiscountHeadId"),
	           root.get("fkInstituteId"),
	           root.get("discountHead"),
	           root.get("isDefault"),
	           root.get("isActive")
	    ));

	    // Create predicates for feeDiscountHeadId and fkInstituteId
	    Predicate feeDiscountIdPredicate = cb.equal(root.get("feeDiscountHeadId"), feeDiscountHeadId);
	    Predicate instituteIdPredicate = cb.equal(root.get("fkInstituteId"), instituteId);

	    // Applying predicates to the query
	    cq.where(cb.and(feeDiscountIdPredicate, instituteIdPredicate));

	    // Executing query and returning single result
	    
	        return entityManager.createQuery(cq).getSingleResult();
	   
	    }
	

	public Response<?> deactivateFeeDiscount(Long feeDiscountHeadId) {
	    Optional<FeeDiscountEntity> existingEntityOpt = iFeeDiscountRepo.findById(feeDiscountHeadId);
	    
	    if (existingEntityOpt.isPresent()) {
	        FeeDiscountEntity feeDiscountEntity = existingEntityOpt.get();
	        
	        if (feeDiscountEntity.getIsDefault() != null && feeDiscountEntity.getIsDefault()) {
	            return new Response<>(HttpStatus.FORBIDDEN.value(), "Unable to delete. The fee discount head is marked as default.");
	        }
	        
	        if (!feeDiscountEntity.getIsActive()) {
	            return new Response<>(HttpStatus.CONFLICT.value(), "FeeDiscount has already been deleted.");
	        }
	        
	        feeDiscountEntity.setIsActive(false);
	        iFeeDiscountRepo.save(feeDiscountEntity);
	        return new Response<>(HttpStatus.OK.value(), "FeeDiscount deleted successfully.");
	    } else {
	        return new Response<>(HttpStatus.NOT_FOUND.value(), "FeeDiscount ID not found.", null);
	    }
	}


	public FeeMasterGeneralSettingDTO addOrUpdateFeeMasterGeneralSetting(FeeMasterGeneralSettingDTO feeMasterGeneralSettingDTO) {
	    // Check if it's a new entity or an update
	    if (feeMasterGeneralSettingDTO.getFeeGeneralSeetingId() == null) {
	        // Check if an active record exists for the institute
	        if (feeMasterGeneralSettingRepo.existsByFkInstituteIdAndIsActiveTrue(feeMasterGeneralSettingDTO.getFkInstituteId())) {
	            throw new RecordNotFoundException("An active FeeMasterGeneralSetting already exists for this institute.");
	        }
	    }

	    // Validate fields when 'Automation' mode is selected
	    if (Boolean.TRUE.equals(feeMasterGeneralSettingDTO.getIsAutomation())) {
	        if (feeMasterGeneralSettingDTO.getSeqNumDigit() <= 0 || feeMasterGeneralSettingDTO.getSeqStartNumber() == null) {
	            throw new IllegalArgumentException("When 'Automatic' is selected, 'seqNumDigit' and 'seqStartNumber' must be provided.");
	        }
	    }

	    // Fetch existing entity or create a new one
	    FeeMasterGeneralSettingEntity feeMasterGeneralSettingEntity;
	    if (feeMasterGeneralSettingDTO.getFeeGeneralSeetingId() != null) {
	        // Update existing entity
	        feeMasterGeneralSettingEntity = feeMasterGeneralSettingRepo.findById(feeMasterGeneralSettingDTO.getFeeGeneralSeetingId())
	                .orElseThrow(() -> new RecordNotFoundException("FeeMasterGeneralSetting not found."));
	    } else {
	        // Create a new entity
	        feeMasterGeneralSettingEntity = new FeeMasterGeneralSettingEntity();
	    }

	    // Set common fields
	    feeMasterGeneralSettingEntity.setFkInstituteId(feeMasterGeneralSettingDTO.getFkInstituteId());
	    feeMasterGeneralSettingEntity.setIsManual(feeMasterGeneralSettingDTO.getIsManual());
	    feeMasterGeneralSettingEntity.setIsAutomation(feeMasterGeneralSettingDTO.getIsAutomation());
	    feeMasterGeneralSettingEntity.setTaxDisabled(feeMasterGeneralSettingDTO.getTaxDisabled());

	    if (Boolean.TRUE.equals(feeMasterGeneralSettingDTO.getIsAutomation())) {
	        // Automatic mode - set sequence fields
	        feeMasterGeneralSettingEntity.setIsManual(false);
	        feeMasterGeneralSettingEntity.setSeqPredixText(feeMasterGeneralSettingDTO.getSeqPredixText());
	        feeMasterGeneralSettingEntity.setSeqNumDigit(feeMasterGeneralSettingDTO.getSeqNumDigit());
	        feeMasterGeneralSettingEntity.setSeqStartNumber(feeMasterGeneralSettingDTO.getSeqStartNumber());
	        feeMasterGeneralSettingEntity.setSeqPreview(feeMasterGeneralSettingDTO.getSeqPreview());

	        // Allow cashDisabled to be true or false based on the provided value
	        feeMasterGeneralSettingEntity.setCashDisabled(feeMasterGeneralSettingDTO.getCashDisabled());

	        if (Boolean.TRUE.equals(feeMasterGeneralSettingDTO.getCashDisabled())) {
	            feeMasterGeneralSettingEntity.setCashPrefixText(feeMasterGeneralSettingDTO.getCashPrefixText());
	            feeMasterGeneralSettingEntity.setCashNumDigit(feeMasterGeneralSettingDTO.getCashNumDigit());
	            feeMasterGeneralSettingEntity.setCashStartNumber(feeMasterGeneralSettingDTO.getCashStartNumber());
	            feeMasterGeneralSettingEntity.setCashPreview(feeMasterGeneralSettingDTO.getCashPreview());
	        } else {
	            feeMasterGeneralSettingEntity.setCashPrefixText(null);
	            feeMasterGeneralSettingEntity.setCashNumDigit(0);
	            feeMasterGeneralSettingEntity.setCashStartNumber(null);
	            feeMasterGeneralSettingEntity.setCashPreview(null);
	        }
	    } else if (Boolean.TRUE.equals(feeMasterGeneralSettingDTO.getIsManual())) {
	        // Manual mode - force 'cashDisabled' to be false
	        feeMasterGeneralSettingEntity.setIsAutomation(false);
	        feeMasterGeneralSettingEntity.setCashDisabled(false); // Always false in manual mode

	        // Nullify automation-related fields
	        feeMasterGeneralSettingEntity.setSeqPredixText(null);
	        feeMasterGeneralSettingEntity.setSeqNumDigit(0);
	        feeMasterGeneralSettingEntity.setSeqStartNumber(null);
	        feeMasterGeneralSettingEntity.setSeqPreview(null);

	        // Nullify cash-related fields as well
	        feeMasterGeneralSettingEntity.setCashPrefixText(null);
	        feeMasterGeneralSettingEntity.setCashNumDigit(0);
	        feeMasterGeneralSettingEntity.setCashStartNumber(null);
	        feeMasterGeneralSettingEntity.setCashPreview(null);
	    }

	    // Save the entity
	    FeeMasterGeneralSettingEntity savedEntity = feeMasterGeneralSettingRepo.save(feeMasterGeneralSettingEntity);

	    // Convert saved entity back to DTO
	    FeeMasterGeneralSettingDTO resultDTO = new FeeMasterGeneralSettingDTO();
	    BeanUtils.copyProperties(savedEntity, resultDTO);

	    return resultDTO;
	}


	public FeeMasterGeneralSettingDTO getFeeMasterByInstituteId(Long instituteId) {
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<FeeMasterGeneralSettingDTO> cq = cb.createQuery(FeeMasterGeneralSettingDTO.class);
	    Root<FeeMasterGeneralSettingEntity> root = cq.from(FeeMasterGeneralSettingEntity.class);

	    // Constructing the DTO class instance in the select clause
	    cq.select(cb.construct(FeeMasterGeneralSettingDTO.class,
	            root.get("feeGeneralSeetingId"),
	            root.get("fkInstituteId"),
	            root.get("isManual"),
	            root.get("isAutomation"),
	            root.get("seqPredixText"),
	            root.get("seqNumDigit"),
	            root.get("seqStartNumber"),
	            root.get("seqPreview"),
	            root.get("cashDisabled"),
	            root.get("cashPrefixText"),
	            root.get("cashNumDigit"),
	            root.get("cashStartNumber"),
	            root.get("cashPreview"),
	            root.get("taxDisabled"),
	            root.get("isActive")
	    ));

	    // Adding predicates
	    Predicate isActivePredicate = cb.isTrue(root.get("isActive"));
	    Predicate instituteIdPredicate = cb.equal(root.get("fkInstituteId"), instituteId);

	    // Combine predicates using AND
	    cq.where(cb.and(isActivePredicate, instituteIdPredicate));

	    // Execute query and get single result
	    return entityManager.createQuery(cq).getSingleResult();
	}


}
