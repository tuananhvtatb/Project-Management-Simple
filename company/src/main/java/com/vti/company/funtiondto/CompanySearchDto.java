package com.vti.company.funtiondto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class CompanySearchDto extends SearchDto{

    private String textName;
    private String textCode;
    private String textEmail;
    private String textPhone;
    private String textStatus;
}
