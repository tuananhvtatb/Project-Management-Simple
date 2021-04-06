package com.vti.company.dto;

import com.vti.company.domain.BaseObject;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BaseObjectDto implements Serializable {

    private UUID id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer flagDelete;

    public BaseObjectDto(){

    }

    public BaseObjectDto(BaseObject entity){
        this.id = entity.getId();
        this.createdDate = entity.getCreatedDate();
        this.updatedDate = entity.getUpdatedDate();
        this.flagDelete = entity.getFlagDelete();
    }
}
