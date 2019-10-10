[Go学习教程](http://c.biancheng.net/view/1.html)

静态链接

编译

运行速度

原生并发支持

标准库维护

###常见库
```
bufio  带缓冲IO
bytes  字节操作
container  ?
crypto	加密算法
database 数据库驱动
debug	？
encoding	json  xml  base64
flag	？
fmt		格式化
go		go语言的词法，词法树。类型
html	html转义以及模板
image 	常见图形格式的访问和生成
io		
math	数学库
net		网络，socket ， http ， 邮件 ， rpc ， smtp
os		
path	
pugin
reflect	反射接口
regexp	正则接口
runtime	运行时接口
sort	排序接口
strings	字符串工具
time	时间
text	文本模板，token，词法器
```
###基本语法

基本类型

```
bool
string
int、int8、int16、int32、int64
uint、uint8、uint16、uint32、uint64、uintptr
byte // uint8 的别名
rune // int32 的别名 代表一个 Unicode 码
float32、float64
complex64、complex128
```

import

```
导入库文件，依赖

不能导入没被使用的依赖
不能定义没被使用的变量
```

const申明常量，只能时字符串，布尔，数值

```
iota 用在常量中，iota每出现一次自动加一，而前面的操作书如果不指定则默认使用上一个的
const{
	name = "helun"
	age = 10 
	isAlive = true`
}
```

数组，切片（动态数组）

```
定义并初始化一个数组
var array = []int{0,1,2}
简短形式申明并初始化一个数组
array2 := []int{0,1} 

一个特殊的切片，长度和容量都是3
var slice = []int{0,1,2}
使用make申明一个切片，长度3，容量5，未初始化，int类型默认填充0
slice2 := make([]int,3,5)

len（slice）： 切片长度  ， cap（slice） 切片容量，append() 和 copy() 

```

range

```
//遍历数组，切片，通道，集合的元素，返回元素索引和元素值
nums := []int{0,1,2}
for index,value := range nums{
	fmt.Println(index,"-->",value)
}

maps := map[int]string{1:"1",2:"2"}
for key,value := range maps{
	fmt.Println(key," to " , value)
}

//range也可以用来枚举Unicode字符串。第一个参数是字符的索引，第二个是字符（Unicode的值）本身。
for index,code range := "go language"{
	fmt.Prinln(index , " is " , code)
}

```
结构体

```
和c中的结构体类似：
type structName struct{
	name string 
	age int8
} 

 helun1 := structName{"helun",17}
 helun2 := structName{name:"helun",age:18}
```

函数 

```
func functionName([parameters...]) [returnTypes] {

}
```

方法

```
一个指明了接收者的函数就是方法，相比于java  c++等面向对象，默认为类的方法加上this指针，
go语言需要开发者显示声明方法的所属者
func (type Type) functionName([parameters...]) [returnTypes]{

}
```

接口

```
type IPhone interface{
	call(num string);
}

type Phone struct{
	name string
}


func (phone Phone) call(nums string){
	fmt.Pritln("I am ", phone.name,", I can call " , nums);
}

func main(){
	var phone IPhone
	phone = new(Phone){"Nokia"}
	phone.call("110")
	phone = new(Phone){"iPhone"}
	phone.call("911")
}
```

error接口

```
type SelfDivide struct{
	a int 
	b int
}

func (divide *SelfDivide) Error() string{
	strFormat := "a is %d , b is 0"
	return fmt.Sprintf(strFormat, divide.a)
}

func Divide(a int , b int) (result int,errorMsg string){
	if(b == 0){
		dDate := Divide{a:a,b:b}
		errorMsg = dDate.Error();
		return ;
	}else{
		return a/b,"";
	}
}

func main(){
	if result , msg := SelfDivide(100,0) ; msg==""{
		fmt.Println(result)
	}else{
		fmt.Println(msg)
	}
}
```
指针（引用）

```
go默认都是值传递，即copy一份一样的值到函数中处理。对于需要传递引用的需要使用指针

```
channel

```
这个段代码会死锁：主线程等待channel的缓冲区，channel输出等待消费者（主线程）消费缓冲区
package main
import(
	"fmt"
)

func main(){
	channel := make(chan int, 2)
	channel <- 2;
	channel <- 1;
	channel <- 3;
	
	fmt.Println(<-channel)
	fmt.Println(<-channel)
}


package main
import (
	"fmt"
)

func main(){

	c := make(chan int , 10)
	go fibo(cap(c),c)
        // range 函数遍历每个从通道接收到的数据，因为 c 在发送完 10 个
        // 数据之后就关闭了通道，所以这里我们 range 函数在接收到 10 个数据
        // 之后就结束了。如果上面的 c 通道不关闭，那么 range 函数就不
        // 会结束，从而在接收第 11 个数据的时候就阻塞了。
	for i := range c{
		fmt.Println(i)
	}
}

func fibo(n int , c chan int){
	x,y := 0,1 
	for i := 0 ; i < n ; i++{
		c <-x 
		x,y = y ,x+y
	}
	close(c)
}
```

并发

```
使用go关键字，执行一个函数，该函数会在一个新的线程中执行。go启动的是一个轻量级线程

package main
import(
	"fmt"
)

func main(){
	channel := make(chan int)
	array := []int{0,1,2,3,4,5};
	go sum(array[:len(array)/2],channel)
	go sum(array[len(array)/2],channel)
	x,y := <- channel,<- channel ;
	fmt.Println("x + y =",x+y)
	
}

func sum(a []int , b chan int){
	sum := 0 
	for _,value := range a{
		sum += value
	}
	b <- sum
}

```

###高级语法

使用new 创建指针

```
str := new(string)
*str = "hahah"
str = "jasj" // 这行会报错，因为str是一个指针变量
fmt.Println(str)
```

变量逃逸分析

```
编译器觉得变量应该分配在堆和栈上的原则是：
变量是否被取地址。
变量是否发生逃逸。
```

type

```
type TypeAlias = Type ： 为Type类型定义一个别名，不能为非本地类型取别名

type TypeAlias Type ： 将Type作为一种新的自定义类型
```

struct

```
还有接口，总的来说。这一套机制和java中的接口，方法覆盖很像

可以嵌套


嵌套中的可以存在同名属性，访问同名属性需要使用全路径名。否则可以直接使用最外层访问最为里层的属性

可以为嵌套的结构体指定同名的函数，如果不适用全路径访问，会优先访问最外的方法。也就是外层的结构体实现的方法会覆盖嵌套的结构体的方法

package main

import(

)


type X struct{
	a int
}

type Y struct{
	X
}

type T struct{
	*X
}

func (x X) Get() int{
	return x.a
]

func (x X) Set( i int) {
	x.a = i
}

func main(){
	x := X{a:1}
	y := Y{X:x}
	t := T{X:&x}
	println(y.Get())
	y.Set(2)
	println(y.Get())
	
	
}



```

map

```
var varName = map[keyType]valueType

map的存储是无序的，顺序输出需要依赖切片，导入后进行排序

map读线程安全，同时读写不完全，delete删除元素

sync.Map：一个线程安全的map,无需创建，定义则使用：var myMap sync.Map。Store存储元素，Load读取元素，Delete删除元素，Range遍历元素

package main

import(
	"fmt"
	"sync"
)

func main() {
	var sense sync.Map
	sense.Store(1,1)
	sense.Store(2,"ss")
	sense.Store("aa","aa")
	sense.Store(true,"true")
	fmt.println(sense.Load("aa"))
	sense.Range(func (k,v interface{}) bool{	// 定义一个匿名函数作为回调，注意这个匿名函数的写法：func 参数列表  返回列表
		fmt.println(k,"-->",v)
		return true // 返回true标识需要继续遍历，false终止遍历
	})
}

```

<b>疑问</b>

此处的map接口可以接受所有类型，功能上类似于泛型，查看参数类型未interface{}，这是啥，如何实现的


list

```
链表， var link = list.New() 

package main

import(
	"container/list"
	"fmt"
)

func main() {
	var link = list.New();	 //初始化一个list
	link.PushFront(1)		// 向list队首加入元素
	elem := link.PushBack("2") // 向list队尾加入元素
	link.InsertBefore("2-1",elem) // 向指定元素的前面插入元素
	link.InsertAfter("2+1",elem)  // 向指定元素的后面插入元素
	link.Remove(elem) // 移除指定元素
	
	
	// 便利list
	for temp := link.Front ; temp != nil ; temp = temp.Next(){
		fmt.Println(temp.Value)
	}
	
}


```

nil

```
是map list slice pointer channel  func interface 的零值

nil 不同于 null  ， nil无法互相比较，nil没有默认的数据类型，不同类型的nil占用的空间也不一样。但不同类型的nil指针值是一样的，都是0x0。

```

make 和 new

```
通常make用于初始化一个内部的数据结构，如slice，channel，等。new则可以用来创建指向某个类型的指针
slice := make([]int,0,100) //初始化一个分配0个元素，预分配空间为100的切片
hash := make(map[int]bool,100) //初始化一个初始容量为100的map
ch := make(chan int , 5) // 初始化一个缓冲区为5的channle

var p = new(int) // 定义一个int类型的指针 等价于  var a int ; var p = &a ; new会直接在堆内存上申请一块地址空间

```

if

```
不需要括号
特殊写法：
在判表达式前，加入一句执行语句，用于获取表达式中相关的变量值。执行语句的变量的作用会被限制在这个判断语句中，这样设计的目的是为了减小变量的作用范围，降低程序错误的可能性
eg：
if err := Connect(); err != nil { // err的作用域只在if中有效
    fmt.Println(err)
    return
}

```

for

```
不需要括号
针对无线循环情况，提供了直接使用for的方式：
for{}

整合了if的for

for i<10 { // go中不支持while，这里的写法像极了while
i++
}

在go中挑出循环的语句有： break、goto、return、panic 
```

for …… range

```
for key,val := range map/channel/slice {} //值得注意的是val始终是后面对象的值拷贝，因此在range通常只进行只读操作，写操作是没有意义的


```







































