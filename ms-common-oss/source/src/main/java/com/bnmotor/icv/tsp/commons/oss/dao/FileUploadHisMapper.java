package com.bnmotor.icv.tsp.commons.oss.dao;

import com.bnmotor.icv.tsp.commons.oss.entity.FileUploadHisDto;
import com.bnmotor.icv.tsp.commons.oss.entity.FileUploadHisPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @Author ：luoyang
* @Date ：2020/9/28 17:08
* @Description ：
*/
@Component
@Mapper
public interface FileUploadHisMapper {

    //批量保存
    Integer saveBatch(@Param("list") List<FileUploadHisPo> list);


    Integer save(FileUploadHisPo fileUploadHisPo);

    List<FileUploadHisPo> getFileUploadHisListBy(@Param("dto") FileUploadHisDto dto
            ,@Param("pageNum") Integer pageNum ,@Param("pageSize") Integer pageSize);


}
