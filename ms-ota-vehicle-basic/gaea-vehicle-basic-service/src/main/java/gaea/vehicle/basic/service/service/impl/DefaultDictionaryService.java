/*
* Copyright 2020 The JA-SIG Collaborative. All rights reserved.
* distributed with thi file and available online at
*/
package gaea.vehicle.basic.service.service.impl;

import gaea.vehicle.basic.service.dao.DictionaryMapper;
import gaea.vehicle.basic.service.models.domain.Dictionary;
import gaea.vehicle.basic.service.models.query.DictionaryQuery;
import gaea.vehicle.basic.service.service.DictionaryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *  默认业务实现类,建议流程控制/业务流程在API实现.
 *	如果需要项目内复用的业务代码,可以考虑在该层实现.
 *  不在统一实现事务配置,需要业务请在下面接口注解,需要注意,事务是整个类代理,所以请在public 入口方法注解.
 * </pre>
 *
 * @author JianKang.Xia
 * @version 1.0.0
 */
@AllArgsConstructor
@Service("dictionaryService")
public class DefaultDictionaryService implements DictionaryService {
    /**
     * 持久操作对象
     */
    private final DictionaryMapper dictionaryMapper;

    @Override
	public List<Dictionary> queryPage(DictionaryQuery query) {
		int count = dictionaryMapper.countPage(query);
		query.setTotalItem(count);
		if (count == 0) {
			return Collections.emptyList();
		} else {
			return dictionaryMapper.queryPage(query);
		}
	}

	@Override
	public Dictionary queryById(Long dictionaryId) {
		return dictionaryMapper.queryById(dictionaryId);
	}

	@Override
	public int insertDictionary(Dictionary dictionary) {
		dictionaryMapper.insertDictionary(dictionary);
		return dictionary.getId().intValue();
	}

	@Override
	public int updateDictionary(Dictionary dictionary) {
		return dictionaryMapper.updateDictionary(dictionary);
	}

	@Override
	public int deleteById(Long dictionaryId) {
		return dictionaryMapper.deleteById(dictionaryId);
	}

}
