package com.vti.company.dto;


import com.vti.company.domain.Images;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class ImagesDto extends BaseObjectDto {

    private String title;

    private String type;

    private byte[] data;

    public ImagesDto(Images entity){
        super(entity);
        this.title = entity.getName();
        this.type = entity.getType();
        this.data = entity.getData();
    }
}
