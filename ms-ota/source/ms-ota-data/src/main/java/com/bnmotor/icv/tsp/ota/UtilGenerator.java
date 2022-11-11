package com.bnmotor.icv.tsp.ota;

/**
 * @ClassName:
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/6/10 13:26
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */

import com.bnmotor.icv.adam.data.mysql.entity.GeneratorDo;
import com.bnmotor.icv.adam.data.mysql.utils.CodeUtil;
import com.google.common.collect.Lists;

import java.util.List;


public class UtilGenerator {

    public static void main(String[] args) {
        GeneratorDo generatorDo = new GeneratorDo();
        // 指定数据库名称
        generatorDo.setDatabaseName("bnmotor_icv_vehicle_fota");
        // 指定模块名称，若只有一级模块sync-video,可以直接填写sync-video
        generatorDo.setModulePath("/source/ms-ota-service-data");
        // 指定数据库表名 表字段最好有注释，这样可以生成对应实体字段注释

        generatorDo.setMapperPackageName("mapper");
        generatorDo.setEntityPackageName("model.entity");
        //这里设置父包名称，所以该类建在和XXXApplication平级，无需提交git
        generatorDo.setParentPackge(UtilGenerator.class.getPackageName());

        //此处添加需要生成的表单名称
        String[] tableNameArys = {"tb_req_from_app"};

        List<String> tableNames = Lists.newArrayList(tableNameArys);
        tableNames.forEach(item -> {
            generatorDo.setTableName(item);
            CodeUtil.autoGenerator(generatorDo);
        });
    }
}
