package main

import (
	"./log"
	"./taskpool"
	"fmt"
	"io"
	"net"
	"time"
)

var reTryTimes int = 3
var channel = make(chan string ,5)
func main() {
	task := taskpool.NewTask(func(args ...interface{}) error {
		time.Sleep(2*time.Second)
		fmt.Println(args,time.Now())
		return nil
	},nil)
	pool := taskpool.NewTaskPool(2,5,6)

	pool.Show()

	go func(){
		i:= 0
		for  {
			i++
			log.Info("投递",i%10,"个任务")
			for j := 0 ; j < i%10 ; j++  {
				pool.AddTask(task)
			}
			time.Sleep(time.Second*3)
		}
	}()

	pool.Start()

	time.Sleep(time.Hour);

	//StartServer()
}

func testpanic(){
	fmt.Println(" before recover")
	defer func() {
		if err := recover(); err!=nil{
			fmt.Println("recover")
		}
	}()
	fmt.Println(" before panic")
	f()
	fmt.Println(" after recover")
}


func f(){
	panic("panic")
}

func read(){
	for data := range channel{
		fmt.Println(data)
	}
}

func add() {
	for i := 0; i < 500 ; i++ {
		channel <- string(i)
		time.Sleep(10*time.Microsecond)
	}
}


func handleConnectionRead(conn net.Conn) {
	defer conn.Close()
	buffer := make([]byte, 10)
	tmpBuffer := make([]byte, 0)
	for   {
		n,err := conn.Read(buffer)
		if err != nil {
			fmt.Println(err)
			break
		}
		fmt.Println((tmpBuffer))
		tmpBuffer = append(tmpBuffer, buffer[:n]...)
		fmt.Println(string(tmpBuffer))
	}
}

func handleConnectionWrite(conn net.Conn){
	defer conn.Close()
	str1 := "{\"evt_type\":1,\"sieral_num\":\"aaa\",\"mac\":\"A89042005DFA\"," +
		"\"stroke_id\":19,\"image_id\":0,\"pos\":{\"seiral\":19,\"segment\":0,\"shelf\"" +
		":0,\"book\":0,\"page\":0,\"pressure\":0,\"abs_x\":0,\"abs_y\":0,\"image_id\":0,\"time\":0}}"

	//
	//str2 := "{\"evt_type\":2,\"sieral_num\":\"aaa\",\"mac\":\"A89042005DFA\"," +
	//	"\"stroke_id\":19,\"image_id\":0,\"pos\":{\"seiral\":19,\"segment\":0,\"shelf\"" +
	//	":0,\"book\":0,\"page\":0,\"pressure\":0,\"abs_x\":0,\"abs_y\":0,\"image_id\":0,\"time\":0}}"
	//
	//str5 := "{\"evt_type\":5,\"sieral_num\":\"aaa\",\"mac\":\"A89042005DFA\"," +
	//	"\"stroke_id\":19,\"image_id\":0,\"pos\":{\"seiral\":19,\"segment\":0,\"shelf\"" +
	//	":0,\"book\":0,\"page\":0,\"pressure\":0,\"abs_x\":1000,\"abs_y\":200,\"image_id\":0,\"time\":0}}"



	for  {

		time.Sleep(5000*time.Millisecond)
		fmt.Println("Begin to send msg to go-cli")
		io.WriteString(conn,str1)



		//time.Sleep(1000*time.Millisecond)
		//io.WriteString(conn,str5)
		//time.Sleep(1000*time.Millisecond)
		//io.WriteString(conn,str2)

	}
}


func StartServer() {
	serviceHost := ":6666"
	tcpAddr, err := net.ResolveTCPAddr("tcp4", serviceHost)
	if err != nil {
		fmt.Println("服务器地址解析异常",serviceHost,err)
	}
	listener, err := net.ListenTCP("tcp", tcpAddr)
	if err!=nil {
		listener.Close()
		fmt.Println("监听端口异常",serviceHost,err)

	}
	// 恢复计数器，以便后用
	reTryTimes=3

	for {
		conn, err := listener.Accept()
		if err != nil {
			continue
			fmt.Println("获取客户端连接异常",serviceHost,err)
		}
		go handleConnectionRead(conn)
		go handleConnectionWrite(conn)
	}
}
