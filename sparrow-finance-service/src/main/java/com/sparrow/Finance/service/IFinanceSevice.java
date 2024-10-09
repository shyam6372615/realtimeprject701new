package com.sparrow.Finance.service;


import java.util.List;
import java.util.Optional;

import com.sparrow.Finance.Exception.RecordNotFoundException;
import com.sparrow.Finance.dto.AddOnDiscountDataDTO;
import com.sparrow.Finance.dto.AddOnDiscountDataResponseDTO;
import com.sparrow.Finance.dto.AddOnDiscountFilterDTO;
import com.sparrow.Finance.dto.AddOnDiscountResponseDTO;
import com.sparrow.Finance.dto.BankDTO;
import com.sparrow.Finance.dto.CashierDataDTO;
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
import com.sparrow.finance.entity.FeeConfig;

public interface IFinanceSevice {
public Response<?> addOrUpdateFeeSetup(FeeSetupInputsDTO FeeSetupInputsDTO);//
public Response<?> getFeeSetupById(Long id,Long instituteId )throws Exception;//
public Response<?> getAllFeeSetup(Long instituteId,String feeType);//
public Response<?> deactivateFeeSetup(Long id,Long instituteId );//
public Response<?> addorUpdateFeeTypeMaster(List<FeeTypeMasterDTO> feeTypeMasterDTO);//
public List<FeeTypeMasterDTO> getAllFeeTypeMaster(Long instituteId);//
public FeeTypeMasterDTO getFeeTypeMasterById(Long id,Long instituteId);///
public Response<?> addorUpdateFeeTypeMaster(FeeTypeMasterDTO feeTypeMasterDTO);
public Response<?> deactivateFeeTypeMaster(Long id, Long instituteId);//

   Optional<BankDTO> getBankDetailsById(Long accSetupId, Long instituteId);//
public List<FeeConfigOutputDTO> getAllFeeConfig();//
public Response<?> addorUpdateFeeHeadMaster(List<FeeHeadMasterDTO> feeHeadMasterDTO);//
public FeeHeadMasterOutputDTO getFeeHeadMasterById(Long id,Long instituteId);//
public Response<?> getAllFeeHeadMaster(Long instituteId);//
public Response<?> geetypeMasterByFeeTypeName(Long instituteId, String feeType)throws RecordNotFoundException;//
public Response<?> deactivateFeeHeadMaster(Long id,Long instituteId);//
public Response<?> addOrUpdateAddOnDiscount(AddOnDiscountDataDTO addOnDiscountDataDTO);//
public AddOnDiscountDataResponseDTO getAddOnDiscountById(Long id,Long instituteId);//
public Response<?> getAllAddOnDiscount(AddOnDiscountFilterDTO addOnDiscountFilterDTO);//
public Response<?> deActivateAddOnDiscount(Long id,Long instituteId);//
public Response<?> addOrUpdateProfileDiscount(ProfileDiscountDataDTO profileDiscountDataDTO);//
public ProfileDiscountDataDTO getProfileDiscountsById(Long id,Long instituteId);//
public List<ProfileDiscountGetDTO> getAllProfileDiscounts(Long instituteId);//
public Response<?> deActivateProfileDiscount(Long id,Long instituteId);//
public Response<?> addOrUpdateCashier(CashierDataDTO cashierDataDTO);//
public List<DiscountResponseDTO> getAllDiscountsByStudentId(Long studentId,Long instituteId);//
public List<AddOnDiscountResponseDTO> getAllAddOnDiscountData(Long instituteId);//
public Response<?> addOrUpdatePaymentModeConfig(List<PaymentModeConfigDTO> paymentModeConfigDTO)throws Exception;//
public Response<?> getPaymentModeConfigById(Long id,Long instituteId) ;//
public Response<?> getAllPaymentModeConfig(Long instituteId) ;//
public Response<?> deactivatePaymentModeconfig(Long id,Long instituteId );//
public Response<?> getAllDefaultFeeSettingsConfig(Long instituteId);//
public Response<?> addOrUpdateDefaultFeeSettingsConfig(DefaultFeeSettingsConfigDTO defaultFeeSettingsConfigDTO);//

public Response<?> addOrUpdateReminderConfig(List<ReminderConfigDTO> listDTO);//
public Response<?> getReminderConfigById(Long id,Long instituteId);//
public Response<?> getAllReminderConfig(Long instityteId);//
public Response<?> deactivateReminderConfig(Long id,Long instituteId);//

public Double getFeeAmountByFeeHead(Long instituteId,Long FeeHeadId);


public List<FeeHeadDetailsDTO>  getAllDistinctFeeHeadsInFeeSetup(Long instituteId);

 public  List<BankDTO> getBankDetails(Long instituteId);//

 public  BankDTO saveOrUpdateBankDetails(BankDTO bankDTO);//

 public Response<?> deactivateBank(Long id, Long instituteId);//


 //   public void deactivateFinanceAccountSetupById(Long accSetupId, Long instituteId);


    void deactivateFinanceAccountSetupById(Long accSetupId, Long instituteId);

    public List<FinanceAccountSetupDTO> getFinanceAccountSetupsWithConfigs(Long instituteId);

    List<FeeConfig> getAllFeeConfig(String type);
    
	public CashierDataDTO getCashierByAccSetupId(Long instituteId, Long accSetupId);


    List<CashierDataDTO> getCashierByAccSetupAndInstitute(Long accSetupId, Long instituteId);
}
