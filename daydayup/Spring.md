###spring-web 启动流程
[启动流程](https://my.oschina.net/klausprince/blog/1791357)

```
<load-on-start>
配置servle的启动权限，默认情况下，servlet只有在第一次访问是实例化，配置后可以在容器启动时实例化
```
	
###spring aop 配置
####基于注解
aop支持和注解支持依赖：

```
   <groupId>org.springframework</groupId>
      <artifactId>spring-aop</artifactId>
      <version>4.3.1.RELEASE</version>
    </dependency>
    <!-- aop aspect注解导包-->
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjrt</artifactId>
      <version>1.8.6</version>
    </dependency>
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.8.9</version>
    </dependency
```
注解说明（注解支持是否需要额外的配置作为该功能开关？）：

```
@Aspect 作用于类，将一个类申明一个切面
@Order	作用于类，参数int，存在多个切面时，用于指定切面的优先级，数值越低优先级越高
@Pointcut 作用于方法，参数execution表达式，用于申明切入点  被作用的方法一般不需要存在实现

以下注解都必须携带参数且值为"declearJoinPointExpression()"吗？？？

@Before 前置通知 目标方法之前执行。被修饰方法携带JoinPoint类型参数
@After 后置通知，目标方法执行后返回前执行。被修饰方法携带JoinPoint类型参数
@AfterReturning 返回通知，目标方法正常返回时执行。被修饰方法携带JoinPoint类型参数和Object类型参数存放目标方法返回值。
@AfterThrowing 目标方法异常时执行。被修饰方法携带JoinPoint类型参数和Exception类型参数存放目标方法抛出的异常。
@Around 环绕通知，包含前置  后置 返回 异常 ，被修饰的方法必须携带参数ProceedingJoinPoint类型的参数，必须有返回值（返回目标方法的返回值）
```
例子：

```
package com.example.aop;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect    //该标签把LoggerAspect类声明为一个切面
@Order(1)  //设置切面的优先级：如果有多个切面，可通过设置优先级控制切面的执行顺序（数值越小，优先级越高）
@Component //该标签把LoggerAspect类放到IOC容器中
public class LoggerAspect {

    /**
     * 定义一个方法，用于声明切入点表达式，方法中一般不需要添加其他代码
     * 使用@Pointcut声明切入点表达式
     * 后面的通知直接使用方法名来引用当前的切点表达式；如果是其他类使用，加上包名即可
     */
    @Pointcut("execution(public * com.example.controller.*Controller.*(..))")
    public void declearJoinPointExpression(){}

    /**
     * 前置通知
     * @param joinPoint
     */
    @Before("declearJoinPointExpression()") //该标签声明次方法是一个前置通知：在目标方法开始之前执行
    public void beforMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        System.out.println("this method "+methodName+" begin. param<"+ args+">");
    }
    /**
     * 后置通知（无论方法是否发生异常都会执行,所以访问不到方法的返回值）
     * @param joinPoint
     */
    @After("declearJoinPointExpression()")
    public void afterMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("this method "+methodName+" end.");
    }
    /**
     * 返回通知（在方法正常结束执行的代码）
     * 返回通知可以访问到方法的返回值！
     * @param joinPoint
     */
    @AfterReturning(value="declearJoinPointExpression()",returning="result")
    public void afterReturnMethod(JoinPoint joinPoint,Object result){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("this method "+methodName+" end.result<"+result+">");
    }
    /**
     * 异常通知（方法发生异常执行的代码）
     * 可以访问到异常对象；且可以指定在出现特定异常时执行的代码
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(value="declearJoinPointExpression()",throwing="ex")
    public void afterThrowingMethod(JoinPoint joinPoint,Exception ex){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("this method "+methodName+" end.ex message<"+ex+">");
    }
    /**
     * 环绕通知(需要携带类型为ProceedingJoinPoint类型的参数)
     * 环绕通知包含前置、后置、返回、异常通知；ProceedingJoinPoin 类型的参数可以决定是否执行目标方法
     * 且环绕通知必须有返回值，返回值即目标方法的返回值
     * @param point
     */
    @Around(value="declearJoinPointExpression()")
    public Object aroundMethod(ProceedingJoinPoint point){

        Object result = null;
        String methodName = point.getSignature().getName();
        try {
            //前置通知
            System.out.println("The method "+ methodName+" start. param<"+ Arrays.asList(point.getArgs())+">");
            //执行目标方法
            result = point.proceed();
            //返回通知
            System.out.println("The method "+ methodName+" end. result<"+ result+">");
        } catch (Throwable e) {
            //异常通知
            System.out.println("this method "+methodName+" end.ex message<"+e+">");
            throw new RuntimeException(e);
        }
        //后置通知
        System.out.println("The method "+ methodName+" end.");
        return result;
    }
}
```
####基于XML
标签说明：

```
<aop:config> : 配置切面的根标签，属性proxy-target-class="true"  可以强制要求使用CGLIB进行代理 false为jdk代理
<aop:pointcut>: 配置切入点，属性id：切入点标识，execution表达式：申明该切入点关联的连接点
<aop:aspect>:完成切面的关联配置，属性order：切面优先级，ref：切面实际的实现类bean
<aop:before>:配置具体的前置通知，属性method：切面类中对应的方法名，pointcut-ref:需要关联的切入点
<aop:after>：同上
<aop:after-returning>：同上
<aop:after-throwing>：同上
<aop:around>：同上
```

[<aop:aspect>与<aop:advisor>的区别](https://www.jianshu.com/p/40f79da0cdef)


示例：

```
<!-- 配置切面的Bean -->
    <bean id="sysAspect" class="com.example.aop.SysAspect"/>
    <!-- 配置AOP -->
    <aop:config>
        <!-- 配置切点表达式  -->
        <aop:pointcut id="pointcut" expression="execution(public * com.example.controller.*Controller.*(..))"/>
        <!-- 配置切面及配置 -->
        <aop:aspect order="3" ref="sysAspect">
            <!-- 前置通知 -->
            <aop:before method="beforMethod"  pointcut-ref="pointcut" />
            <!-- 后置通知 -->
            <aop:after method="afterMethod"  pointcut-ref="pointcut"/>
            <!-- 返回通知 -->
            <aop:after-returning method="afterReturnMethod" pointcut-ref="pointcut" returning="result"/>
            <!-- 异常通知 -->
            <aop:after-throwing method="afterThrowingMethod" pointcut-ref="pointcut" throwing="ex"/>
            <aop:around method="aroundMethod" pointcut-ref="pointcut"/>
        </aop:aspect>
    </aop:config>
```

####tiles
springmvc提供的一个前端布局管理插件，动态的组合jsp页面。

依赖

```
<dependency>
  <groupId>org.apache.tiles</groupId>
  <artifactId>tiles-extras</artifactId>
  <version>3.0.5</version>
</dependency>
```

布局文件layout.xml,定义模板，页面布局

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE tiles-definitions PUBLIC
     "-//Apache Software Foundation//DTD Tiles Configuration 3.0//EN"
     "http://tiles.apache.org/dtds/tiles-config_3_0.dtd">
<tiles-definitions>
  <!-- 主布局 -->
  <definition name="layout" template="/mainLayout.jsp">
  </definition>
  <!-- 主布局 -->
  <!-- 项目 -->
  <definition name="myView" extends="layout">
    <put-attribute name="a" value="/a.jsp" />
    <put-attribute name="item" expression="/${item}.jsp" />
  </definition>
  <!--项目-->
</tiles-definitions>
```


springMVC-servlet.xml中配置试图解析器

```
<bean id="tilesViewResolver" class="org.springframework.web.servlet.view.tiles3.TilesViewResolver" p:order="1"/>
    <bean id="tilesConfigurer" class="org.springframework.web.servlet.view.tiles3.TilesConfigurer">
        <property name="definitions">
            <list>
                <value>classpath:layout.xml</value>
            </list>
        </property>
</bean>
```

配合控制器使用

```
public String introductionView(Model model) { 
    model.addAttribute("item","introduction");//这个就是给返回值添加个属性，在页面可以直接得到item，
//${item}   在jsp页面就可以得到后台返回的值。
return “myView”; //这里的myView为layout.xml中配置的视图名称}
```

[tiles参考资料](https://www.cnblogs.com/handsome1013/p/6140720.html)


