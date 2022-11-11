package gaea.vehicle.basic.service.controller;

import java.util.List;

import gaea.vehicle.basic.service.models.domain.BaseApi;
import gaea.vehicle.basic.service.models.domain.Dictionary;
import gaea.vehicle.basic.service.models.query.DictionaryQuery;
import gaea.vehicle.basic.service.models.request.DictionaryReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import gaea.vehicle.basic.service.models.page.PageResult;
import gaea.vehicle.basic.service.models.page.Resp;
import gaea.vehicle.basic.service.models.validate.Update;
import gaea.vehicle.basic.service.service.DictionaryService;

import javax.validation.Valid;


/**
 * 
 *
 * @author xiajiankang
 * @email xiajiankang1@bngrp.com
 * @date 2020-04-02 18:06:32
 */
@RestController
@Api(value="字典管理接口",tags={"字典管理接口"})
@RequestMapping("/dictionary")
public class DictionaryController extends BaseApi {
    /**
     * 服务接口
     */
    @Autowired
    private DictionaryService dictionaryService;

    @ApiOperation("列表")
    @GetMapping("dictionarys")
    public PageResult<List<Dictionary>> queryPage(DictionaryQuery query) {
        return new PageResult<>(query, dictionaryService.queryPage(query));
    }

    @ApiOperation("获取")
    @GetMapping("dictionarys/{dictionaryId}")
    public Resp<Dictionary> queryById(@PathVariable Long dictionaryId) {
        return new Resp<>(dictionaryService.queryById(dictionaryId));
    }

    @ApiOperation("新增")
    @PostMapping("dictionarys")
    public Resp<Integer> saveDictionary(@RequestBody @Valid DictionaryReq dictionaryReq) {
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dictionaryReq, dictionary);
        return new Resp<>(dictionaryService.insertDictionary(dictionary));
    }

    @ApiOperation("更新")
    @PutMapping("dictionarys")
    public Resp<Integer> updateDictionary(@RequestBody @Validated(value = Update.class) DictionaryReq dictionaryReq) {
        Dictionary dictionary = new Dictionary();
        BeanUtils.copyProperties(dictionaryReq, dictionary);
        return new Resp<>(dictionaryService.updateDictionary(dictionary));
    }

    @ApiOperation("删除")
    @DeleteMapping("dictionarys/{dictionaryId}")
    public Resp<Integer> deleteById(@PathVariable Long dictionaryId) {
        return new Resp<>(dictionaryService.deleteById(dictionaryId));
    }
}