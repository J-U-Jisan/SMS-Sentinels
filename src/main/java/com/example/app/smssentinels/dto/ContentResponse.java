package com.example.app.smssentinels.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContentResponse extends BaseResponse{
    private Integer contentCount;
    private List<Content> contents;
}
