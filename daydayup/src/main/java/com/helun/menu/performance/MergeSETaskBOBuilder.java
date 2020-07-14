package com.helun.menu.performance;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.lang.model.type.NullType;


public class MergeSETaskBOBuilder implements IMergeSETaskBOBuilder {
	String serviceName = null;
	Class<? extends IMergeService> serviceClass = null;
	Class<? extends MergeSETaskBO> voClass = null;
	String mergeKey = null;
	Integer proxyType = null;
	
	// 附加信息
	Map<String, Object> propertiesMap = null;
	// 接口参数
	Map<String, Object> paramsMap = null;

	MergeSETaskBO mergeSETaskBO = null ;
	
	public MergeSETaskBOBuilder() {
	}
	
	public MergeSETaskBOBuilder(MergeSETaskBO mergeSETaskBO) {
		this.mergeSETaskBO = mergeSETaskBO ;
	}

	@Override
	public String serviceName() {
		return serviceName;
	}

	public Class<? extends IMergeService> serviceClass() {
		return serviceClass;
	}

	@Override
	public String key() {
		return mergeKey;
	}

	@Override
	public Integer proxyType() {
		return proxyType == null ? IMergeServiceProxy.JUST_TRANSACTION : proxyType ;
	}

	@Override
	public MergeSETaskBO build() {
		if (mergeSETaskBO != null) {
			mergeSETaskBO.addPropertys(propertiesMap);
			return mergeSETaskBO;
		}
		try {
			mergeSETaskBO = (MergeSETaskBO) voClass.newInstance();
			mergeSETaskBO.addPropertys(propertiesMap);
			if (paramsMap != null) {
				for (Entry<String, Object> param : paramsMap.entrySet()) {
					Boolean hadExcute = false;
					String paramName = param.getKey(); // 参数名称
					Object paramValue = param.getValue(); // 参数值
					String fristLetter = paramName.substring(0, 1).toUpperCase();
					String methodName = "set" + fristLetter + paramName.substring(1);
					Method[] methods = voClass.getDeclaredMethods();
					if (methods != null) {
						for (Method method : methods) {
							// 根据方法名查找set方法
							if (method.getName().equals(methodName)) {
								// 通过参数类型和参数数量进一步判断set方法的合法性
								Class<?>[] parameterTypes = method.getParameterTypes();
								if (parameterTypes != null && parameterTypes.length == 1) {

									Class<?> paramClass = null;
									if (paramValue == null) {
										paramClass = NullType.class;
									} else {
										paramClass = paramValue.getClass();
									}

									if (paramValue == null || getClass(parameterTypes[0]).equals(paramClass)) {
										try {
											method.invoke(mergeSETaskBO, paramValue);
											hadExcute = true;
										} catch (IllegalArgumentException | InvocationTargetException e) {
											throw new RuntimeException("Excute " + methodName + "(" + parameterTypes[0]
													+ " " + parameterTypes[0].getName() + ") failure.[#paramValue : "
													+ paramValue + "]", e);
										}
									} else {
										throw new RuntimeException("Excute " + methodName + "(" + parameterTypes[0]
												+ " " + parameterTypes[0].getName() + ") failure.[#paramValue : "
												+ paramClass.getName() + " " + paramValue + "]");
									}
								}
							}
						}
					}
					if (!hadExcute) {
						throw new RuntimeException("Can't find method [#methodName:" + methodName + "]");
					}
				}
			}
			return mergeSETaskBO;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 设置当前请求将要调用的服务名称，名称为手动注册的服务名（一般地和spring容器的beanName一致）
	 * 
	 * @param serverName
	 */
	public void setServerName(String serverName) {
		this.serviceName = serverName;
	}

	/**
	 * 设置当前请求调用的服务类型，类型被注册在spring容器中
	 * 
	 * @param serverClass
	 */
	public void setServerClass(Class<? extends IMergeService> serverClass) {
		this.serviceClass = serverClass;
	}

	/**
	 * 设置请求参数将要转化的VO对现象类型
	 * @see com.ql.lxx.service.impl.MergeSETaskBOBuilder#setVOClass(Class)
	 * @param voClass
	 */
	public void setVOClass(Class<? extends MergeSETaskBO> voClass) {
		this.voClass = voClass;
	}

	/**
	 * 设置在合并服务中，按照怎样的规则收集一个批次的服务
	 * 
	 * @param mergeKey
	 */
	public void setMergeKey(String mergeKey) {
		this.mergeKey = mergeKey;
	}

	/**
	 * 设置以何种方式执行被代理的服务 {@link com.ql.lxx.service.IMergeSETaskBOBuilder}
	 */
	public void setProxyType(Integer proxyType) {
		this.proxyType = proxyType;
	}

	/**
	 * 设置请求参数,这些参数将会被构建为{@link com.ql.lxx.service.IMergeService}中指定的对象实例
	 * 
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	public MergeSETaskBOBuilder addParams(String paramName, Object paramValue) {
		if (paramsMap == null) {
			paramsMap = new HashMap<String, Object>();
		}
		paramsMap.put(paramName, paramValue);
		return this;
	}
	

	/**
	 * 设置附在参数，这些参数会作为附加信息传递到{@link com.ql.lxx.service.impl.MergeSETaskBOBuilder#setVOClass(Class)}实例中
	 * 
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	public MergeSETaskBOBuilder addProperties(String paramName, Object paramValue) {
		if (propertiesMap == null) {
			propertiesMap = new HashMap<String, Object>();
		}
		propertiesMap.put(paramName, paramValue);
		return this;
	}
	
	private Class<? extends Object> getClass(Class<?>  type){
		System.out.println(type.toString());
		switch(type.toString()) {
		case "int" : return Integer.class ;
		case "float" : return Float.class ;
		case "double" : return Double.class ;
		case "char" : return Character.class ;
		case "byte" : return Byte.class ;
		case "boolean" : return Boolean.class ;
		case "long" : return Long.class ;
		default : return type;
		}
		
	}
}

