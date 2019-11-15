package taskpool

import (
	"../log"
	"runtime"
	"runtime/debug"
	"strconv"
	"time"
)

const default_doWork = "doWork"
const default_closePool = "closePool"
const default_taskManager = "taskManager"
const default_show = "show"

func (taskPool *TaskPool) getWorkerNum() int {
	 num := len(taskPool.workerStatus)
	 for id,_ := range taskPool.workerStatus{
		 if isDefaultTask(id) {
			 num --
		 }
	 }
	 return num
}

func finally() {

	if err := recover(); err != nil {
		var buf [4096]byte
		n := runtime.Stack(buf[:], false)
		log.ErrInfo(string(buf[:n]))
		debug.PrintStack()
		log.ErrInfo("异常信息：", err)
	}

}


func (taskPool *TaskPool) worker(workerId string) {
	defer finally()
	defer func() {
		taskPool.workerStatus[workerId]=false
	}()
	taskPool.workerStatus[workerId]=true
	workerNum := taskPool.getWorkerNum()

	for taskPool.isRunning {
		if val:=taskPool.workerStatus[workerId];!val {
			break
		}
		if len(taskPool.jobsChannel) == 0 || len(taskPool.jobsChannel)*2 < workerNum {
			break
		}
		task, ok := <-taskPool.jobsChannel
		if ok {
			taskPool.currentTaskStop[workerId]=task.stop
			if err := task.Execute(workerId);err!=nil{
				log.ErrInfo(err)
			}
		} else {
			panic(workerId + "：执行通道已关闭")
		}
	}
}

func (taskPool *TaskPool) workerFactory(cap int) {
	defer finally()
	workerNum := taskPool.getWorkerNum()
	if workerNum < taskPool.workerNum {
		for i := 0; i < cap; i++ {
			num := workerNum + 1
			workerId := "worker" + strconv.Itoa(num)
			go taskPool.worker(workerId)
		}
	}
}

func isDefaultTask(taskId string) bool{
	return taskId==default_doWork||taskId==default_closePool||taskId==default_taskManager||default_show == taskId
}

func (taskPool *TaskPool) closePool() {
	defer finally()
	defer func() {
		taskPool.workerStatus[default_closePool]=false
	}()
	taskPool.workerStatus[default_closePool]=true

	taskPool.isRunning = false
	taskPool.workerStatus[default_doWork]=false
	taskPool.workerStatus[default_taskManager]=false

	for id,status := range taskPool.workerStatus {
		if !isDefaultTask(id) && status {
			taskStop,ok := taskPool.currentTaskStop[id]
			if ok&&taskStop!=nil{
				taskStop()
			}
			taskPool.workerStatus[id]= false
		}
	}

	close(taskPool.EntryChannel)
	close(taskPool.jobsChannel)
	taskPool.currentTaskStop= nil
	taskPool.workerStatus= nil

}

func (taskPool *TaskPool) dealTask() {
	defer finally()
	defer func() {
		taskPool.workerStatus[default_doWork]=false
	}()
	taskPool.workerStatus[default_doWork]=true
	for taskPool.isRunning {
		if val := taskPool.workerStatus[default_doWork] ; !val{ // TODO map中查询不到的bool 返回什么
			break
		}
		task, ok := <-taskPool.EntryChannel
		if ok {
			taskPool.jobsChannel <- task
		} else {
			panic("任务通道已关闭")
		}
	}
}

func (taskPool *TaskPool) goManager() {

	defer finally()
	defer func() {
		taskPool.workerStatus[default_taskManager]= false
	}()
	if taskPool.workerStatus == nil {
		taskPool.workerStatus = make(map[string]bool)
	}
	taskPool.workerStatus[default_taskManager]= true

	for taskPool.isRunning {

		for id,status := range taskPool.workerStatus {
			if !status {
				delete(taskPool.workerStatus, id)
				delete(taskPool.currentTaskStop, id)
			}

			needWorkerNum := taskPool.workerNum
			if needWorkerNum > len(taskPool.jobsChannel) {
				needWorkerNum = len(taskPool.jobsChannel)
			}

			if taskPool.getWorkerNum() < needWorkerNum {
				taskPool.workerFactory(needWorkerNum - taskPool.getWorkerNum())
			}
		}

		time.Sleep(time.Second * 1)
	}
}

func (taskPool *TaskPool) show(){
	defer finally()
	defer func() {
		taskPool.workerStatus[default_show]=false
	}()
	taskPool.workerStatus[default_show]=true
	for   {
		log.Info("================================")
		log.Info("状态：运行中：",taskPool.isRunning)
		log.Info("最大工作协程数：",taskPool.workerNum)
		log.Info("任务队列：",len(taskPool.EntryChannel))
		log.Info("执行队列：",len(taskPool.jobsChannel))
		log.Info("协程数：",len(taskPool.workerStatus))
		log.Info("工作协程数：",taskPool.getWorkerNum())
		log.Info("当前任务数：",len(taskPool.currentTaskStop))
		log.Info("当前协程状态：",taskPool.workerStatus)
		time.Sleep(time.Second*5)
	}
}
