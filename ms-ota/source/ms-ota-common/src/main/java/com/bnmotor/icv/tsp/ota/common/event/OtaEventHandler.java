package com.bnmotor.icv.tsp.ota.common.event;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.bnmotor.icv.tsp.ota.common.OtaMessageAttribute;
import com.bnmotor.icv.tsp.ota.common.route.RouteSelector;
import com.bnmotor.icv.tsp.ota.common.route.RouteThreadFactory;
import com.bnmotor.icv.tsp.ota.util.MyCollectionUtil;
import com.google.common.collect.Maps;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: OtaEventHandler.java OtaEventHandler
 * @Description: 应用内的消息处理（暂时不占用kafka，ota有单独kafka时再考虑移出）
 * @author E.YanLonG
 * @since 2020-11-16 14:41:54
 * @Copyright: 2020 www.baoneng.com Inc. All rights reserved.
 *             注意：本内容仅限于宝能汽车公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
@Slf4j
public class OtaEventHandler implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	static final Map<String, TriplePair<OtaEventHandler.EventHandler>> OTA_EVENT_HANDLER_CLASS = Maps.newHashMap();

	@Autowired
	RouteSelector routeSelector;
	
	@Autowired
	RouteThreadFactory routeThreadFactory;
	
	@Qualifier(OtaEventConstant.TASK_COMMON_EXECUTOR)
	@Autowired
	Executor defexecutor;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@PostConstruct
	public void post() {
		Collection<OtaEventHandler.EventHandler> eventHandlerList = applicationContext.getBeansOfType(OtaEventHandler.EventHandler.class).values();
		eventHandlerList.forEach(eventHandler -> register(eventHandler));
		if (MyCollectionUtil.isEmpty(eventHandlerList)) {
			log.warn("系统未定义消息处理eventHandler,请检查是否正常");
		}
	}
	
	@PreDestroy
	public void destroy() {
		log.info("OtaEventHandler destroy...");
		routeThreadFactory.close();
	}
	
	public void handle(OtaEvent event) {
		OtaEventType eventType = event.getEventType();
		TriplePair triplePair = selectEventHandler(eventType);
		if (Objects.nonNull(triplePair)) {
			message(event, triplePair);
		} else {
			log.error("event message|{} lost...", event);
		}
	}
	
	public void message(OtaEvent event, TriplePair triplePair) {
		OtaEventHandler.EventHandler otaEventHandler = (OtaEventHandler.EventHandler) triplePair.getHandle();
		Class clazz = triplePair.getMsgtype();
		boolean isRoute = triplePair.isRoute();
		Object eventMessage = otaEventHandler.onReceive(event, clazz);
		Object routeKey = isRoute ? routeSelector.select(eventMessage, isRoute) : null;
		Executor executor = routeThreadFactory.with(routeKey);
		if (Objects.nonNull(executor)) {
			executor.execute(() -> {
				otaEventHandler.execute(eventMessage);
			});
		} else {
			executor = otaEventHandler.executor(); // handle是否指定处理
			executor = Objects.isNull(executor) ? this.defexecutor : executor; // 使用默认池处理
			executor.execute(() -> {
				otaEventHandler.execute(eventMessage);
			});
		}
	}
	
	public void register(OtaEventHandler.EventHandler eventHandler) {
		Class<?> targetClass = AopUtils.getTargetClass(eventHandler);
		OtaMessageAttribute otaMessageAttribute = targetClass.getAnnotation(OtaMessageAttribute.class);
		if (Objects.isNull(otaMessageAttribute)) {
			return;
		}
		String[] topics = otaMessageAttribute.topics();
		Class<?> clazz = otaMessageAttribute.msgtype();
		boolean route = otaMessageAttribute.route();
		for (String topic : topics) {
			TriplePair triplePair = TriplePair.of().setTopic(topic).setHandle(eventHandler).setMsgtype(clazz).setRoute(route);
			OTA_EVENT_HANDLER_CLASS.put(topic, triplePair);
			routeSelector.register(clazz);
		}
	}

	public interface EventHandler<T> {
		default T onReceive(OtaEvent event, Class<T> clazz) {
			return clazz.cast(event.getSource());
		}

		default public void beforeMessage(T message) {
//			log.info("耗时统计|{}", OtaEventMonitor.prettyPrint());
		}
		
		default public void afterMessage(T message) {}
		
		default public T execute(T message) {
			String threadname = Thread.currentThread().getName();
			try {
				String signname = OtaEventMonitor.init();
				Thread.currentThread().setName(threadname + "-" + signname);
				beforeMessage(message);
				onMessage(message);
				afterMessage(message);
			} catch (Exception e) {
				log.error("otaEvent error|{}", e.getMessage(), e);
				throw e;
			} finally {
				OtaEventMonitor.clean();
				Thread.currentThread().setName(threadname);
			}
			return message;
		}
		
		abstract public T onMessage(T message);
		
		default public Executor executor() {
			return null;
		}

	}

	public <T> TriplePair selectEventHandler(OtaEventType type) {
		return OTA_EVENT_HANDLER_CLASS.get(type.type);
	}

	@NoArgsConstructor(staticName = "of")
	@Accessors(chain = true)
	@Data
	public static class TriplePair<T> {
		String topic;
		T handle;
		Class<?> msgtype;
		boolean route;
	}

}