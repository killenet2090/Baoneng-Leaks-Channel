package com.bnmotor.icv.tsp.device.util;

import com.bnmotor.icv.adam.data.mysql.entity.GeneratorDo;
import com.bnmotor.icv.adam.data.mysql.utils.CodeUtil;

/**
 * @ClassName: Test
 * @Description: test
 * @author: zhangwei2
 * @date: 2020/6/23
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class MybatisPlusGenerator {

    public static void main(String[] args) {
        GeneratorDo generatorDo = new GeneratorDo();
        // 指定数据库名称
        generatorDo.setDatabaseName("db_device");
        // 指定模块名称，若只有一级模块sync-video,可以直接填写sync-video
        generatorDo.setModulePath("ms-device");
        // 指定数据库表名 表字段最好有注释，这样可以生成对应实体字段注释
        generatorDo.setTableName("tb_veh_data_asyn_task");
        //这里设置父包名称，所以该类建在和XXXApplication平级，无需提交git
        generatorDo.setParentPackge(MybatisPlusGenerator.class.getPackageName());
        CodeUtil.autoGenerator(generatorDo);
    }
}