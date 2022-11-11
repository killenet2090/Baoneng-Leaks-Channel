package gaea.vehicle.basic.service.models.validate;

import javax.validation.groups.Default;

/**
 * <pre>
 *  更新校验组,后续可以根据不同项目做一些分组校验，使用：
 *
 *  ｛@code
 *   @NotNull(message="{Messaeg.Notnull.message}")
 *   private Integer id;
 *
 *   public void test(@Validated(value = Update.class) id) {
 *
 *   }
 *  ｝
 * </pre>
 *
 * @author jiankank.xia
 * @version 1.0.0
 */
public interface Update extends Default {
}
