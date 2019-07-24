###spring-web 启动流程
	https://my.oschina.net/klausprince/blog/1791357
	
###spring aop 配置
####基于注解
```
@Aspect 作用于类，将一个类申明一个切面
@Order	作用于类，参数int，存在多个切面时，用于指定切面的优先级，数值越低优先级越高
@Pointcut 作用于方法，参数execution表达式，用于申明切入点  被作用的方法一般不需要存在实现

@Before 前置通知 目标方法之前执行。被修饰方法携带JoinPoint类型参数
@After 后置通知，目标方法执行后返回前执行。被修饰方法携带JoinPoint类型参数
@AfterReturning 返回通知，目标方法正常返回时执行。被修饰方法携带JoinPoint类型参数和Object类型参数存放目标方法返回值。
@AfterThrowing 目标方法异常时执行。被修饰方法携带JoinPoint类型参数和Exception类型参数存放目标方法抛出的异常。
@Around 环绕通知，包含前置  后置 返回 异常 ，被修饰的方法必须携带参数ProceedingJoinPoint类型的参数，必须有返回值（返回目标方法的返回值）
```
####基于XML