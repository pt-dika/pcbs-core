package dk.apps.pcps.dbmaster.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(schema = "midware_master_db")
@Data
public class Merchant {
    @Id
    private int id;
    private String name;
    private String address1;
    private String address2;
    private int salesId;
    private String salesCoporateName;
    private String salesName;
    private String salesCode;
    private String salesPhone;
    private Boolean isBusinessEntity;
    private Boolean isBusinessEntityHasId;
    private Boolean isBusinessEntityHasTaxRegistration;
    private Boolean isBusinessEntityHasTradeBusinessLicense;
    private Boolean isBusinessEntityHasCompanyRegistrationCertificate;
    private Boolean isBusinessEntityHasCertificateOfBusinessDomicile;
    private Boolean isBusinessEntityHasDeedOfEstablishment;
    private Boolean isBusinessEntityHasPhotoOfPlaceOfBusiness;
    private Boolean isIndividual;
    private Boolean isIndividualHasId;
    private Boolean isIndividualHasTaxRegistration;
    private Boolean isIndividualHasCertificateOfBusinessDomicile;
    private Boolean isIndividualHasPhotoOfPlaceOfBusiness;
    private Boolean hasLetterOfAttorney;
    private String legalName;
    private String taxNum;
    private String familyCertificateNumber;
    private String businessType;
    private String postalCode;
    private String phoneOffice;
    private String businessDuration;
    private String picName;
    private String picPosition;
    private String picOfficePhone;
    private String picMobilePhone;
    private String picEmail;
    private String outletName;
    private String outletCashiers;
    private String outletAddress;
    private String outletPhone;
    private String outletManagerName;
    private String outletManagerMobilePhone;
    private String outletManagerEmail;
    private String outletOperationalHourStart;
    private String outletOperationalHourEnd;
    private String transactionBankBranch;
    private String transactionBankAccountNumber;
    private String transactionOnBehalf;
    private String transactionCurrency;
    private long maxAmount;
    private long minAmount;
    private String idImage;
    private String taxCardImage;
    private String tradeBusinessLicenseImage;
    private String companyRegistrationCertificateImage;
    private String letterOfAttorneyImage;
    private String idCardNum;
    private Integer districtId;
    private Integer villageId;
    private Boolean isCdcpEnable;
    private Boolean isQrpsEnable;
    @Column(name = "transaction_bank_account_number_2")
    private String transactionBankAccountNumber2;
    @Column(name = "transaction_on_behalf_2")
    private String transactionOnBehalf2;
    @Column(name = "transaction_currency_2")
    private String transactionCurrency2;
    @Column(name = "transaction_bank_branch_2")
    private String transactionBankBranch2;
    @Column(name = "transaction_bank_2")
    private Integer transactionBank2;
    @Column(name = "transaction_bank_clearing_code_2")
    private String transactionBankClearingCode2;
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "merchant_group_id", referencedColumnName = "id")
    private MerchantGroup merchantGroup;

}
