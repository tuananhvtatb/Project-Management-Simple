package com.vti.company.funtiondto;

import lombok.Data;

@Data
public class SearchDto {

    private Long id;
    private String checkCode;
    private int pageIndex;
    private int pageSize;
}
