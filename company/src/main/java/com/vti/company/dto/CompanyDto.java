package com.vti.company.dto;

import com.vti.company.domain.Company;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyDto extends BaseObjectDto{

    @NotBlank(message = "Mã trống!")
    @Size(max = 6, message = "Lớn hơn 6 kí tự!")
    @Pattern(message = "Mã chỉ gồm kí tự và số",regexp = "^[a-zA-Z0-9_]*$")
    private String code;

    @NotBlank(message = "Tên trống!")
    @Size(max = 256)
    private String name;

    @NotBlank(message = "Địa chỉ trống!")
    private String address;

    @NotBlank(message = "Email trống!")
    @Size(max = 256)
    @Pattern(message = "Email không hợp lệ!", regexp = "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$")
    private String email;

    @NotBlank(message = "Số điện thoại trống!")
    @Pattern(message = "Số điện thoại không hợp lệ!", regexp = "(\\+84|0)\\d{9,10}")
    private String phoneNumber;

    private String status;

    @Size(max = 256)
    private String website;


    public CompanyDto(){

    }

    public CompanyDto(Company entity){
        super(entity);
        this.code = entity.getCode();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.address = entity.getAddress();
        this.phoneNumber = entity.getPhoneNumber();
        this.status = entity.getStatus();
        this.website = entity.getWebsite();
    }

}
