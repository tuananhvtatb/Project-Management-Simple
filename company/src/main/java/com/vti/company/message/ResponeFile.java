package com.vti.company.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponeFile {

    private String name;

    private String url;

    private String type;

    private long size;
}
