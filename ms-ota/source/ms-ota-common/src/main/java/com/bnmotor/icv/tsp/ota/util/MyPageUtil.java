package com.bnmotor.icv.tsp.ota.util;

/**
 * @ClassName: MyPageUtil
 * @Description:
 * @author: xuxiaochang1
 * @date: 2020/12/2 17:18
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved. 注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目
 */
public final class MyPageUtil {
    private MyPageUtil(){}

    /**
     * iPage转换到Page
     * @param iPage
     * @return
     */
    /*public static <T, R> Page<R> map(IPage<T> iPage, Function<T, R> fun) {
        IPage<R> page = new IPage();
        page.setCurrent(Long.valueOf(iPage.getCurrent()).intValue());
        page.setPageSize(Long.valueOf(iPage.getSize()).intValue());
        page.setTotal(iPage.getTotal());
        if (page.getTotal() == 0L) {
            page.setPages(0L);
        } else {
            Long pages = page.getTotal() / (long)page.getPageSize();
            page.setPages(page.getTotal() % (long)page.getPageSize() == 0L ? (long)pages.intValue() : (long)(pages.intValue() + 1));
        }

        if(MyCollectionUtil.isNotEmpty(iPage.getRecords())) {
            List<R> list = iPage.getRecords().stream().map(fun).collect(Collectors.toList());
            page.setList(list);
        }
        return page;
    }*/
}
