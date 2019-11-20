package taskpool

import (
	"../log"
	"../syncmap"
	"runtime"
	"runtime/debug"
	"strconv"
	"strings"
	"sync"
	"sync/atomic"
	"time"
)

const default_doWork = "doWork"
const default_closePool = "closePool"
const default_taskManager = "taskManager"
const default_show = "show"

var lock sync.Mutex

func (taskPool *TaskPool) getWorkerNum() (int,int) {
	num := taskPool.workerStatus.Count()
	max := 0
	for _, id := range taskPool.workerStatus.Keys() {
		if isDefaultTask(id) {
			num--
		}else {
			index := strings.Index(id,"worker")

			if num,err := strconv.Atoi(id[index+6:]);err==nil{
				if num > max {
					max = num
				}
			}else {
				log.Debug(err)
			}
		}
	}
	return num,max
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
		taskPool.workerStatus.Set(workerId, false)
		atomic.AddInt32(taskPool.workerCount, -1)
	}()
	taskPool.workerStatus.Set(workerId, true)
	atomic.AddInt32(taskPool.workerCount, 1)
	workerNum,_ := taskPool.getWorkerNum()

	for taskPool.isRunning {
		if val, _ := taskPool.workerStatus.Get(workerId); val == nil || !val.(bool) {
			break
		}
		if len(taskPool.jobsChannel) == 0 || len(taskPool.jobsChannel)*2 < workerNum {
			if int(*taskPool.workerCount) > taskPool.defaultWorker {
				break
			}
		}
		task, ok := <-taskPool.jobsChannel
		if ok {
			taskPool.currentTaskStop.Set(workerId, task.stop)
			if err := task.Execute(workerId); err != nil {
				log.ErrInfo(err)
			}
		} else {
			panic(workerId + "：执行通道已关闭")
		}
	}
}

func (taskPool *TaskPool) workerFactory(cap int) {
	defer finally()

	workerNum,maxId := taskPool.getWorkerNum()
	needWorker := taskPool.workerNum - workerNum
	for i := 0; i < cap && needWorker > 0; i++ {
		workerNum++
		maxId ++
		workerId := "worker" + strconv.Itoa(maxId)
		log.Info("当前工作任务：", workerNum, "即将新建任务：", workerId, "cap", cap)
		go taskPool.worker(workerId)
		needWorker --
	}
}

func isDefaultTask(taskId string) bool {
	return taskId == default_doWork || taskId == default_closePool || taskId == default_taskManager || default_show == taskId
}

func (taskPool *TaskPool) closePool() {
	defer finally()
	defer func() {
		taskPool.workerStatus.Set(default_closePool, false)
	}()
	taskPool.workerStatus.Set(default_closePool, true)

	taskPool.isRunning = false
	taskPool.workerStatus.Set(default_doWork, false)
	taskPool.workerStatus.Set(default_taskManager, false)

	for _, id := range taskPool.workerStatus.Keys() {
		status, _ := taskPool.workerStatus.Get(id)
		if !isDefaultTask(id) && status.(bool) {
			taskStop, ok := taskPool.currentTaskStop.Get(id)
			if ok && taskStop != nil {
				taskStop.(func())()
			}
			taskPool.workerStatus.Set(id, false)
		}
	}

	close(taskPool.EntryChannel)
	close(taskPool.jobsChannel)
	taskPool.currentTaskStop = nil
	taskPool.workerStatus = nil

}

func (taskPool *TaskPool) dealTask() {
	defer finally()
	defer func() {
		taskPool.workerStatus.Set(default_doWork, false)
	}()
	taskPool.workerStatus.Set(default_doWork, true)
	for taskPool.isRunning {
		if val, _ := taskPool.workerStatus.Get(default_doWork); val == nil || !val.(bool) {
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
		taskPool.workerStatus.Set(default_taskManager, false)
	}()
	if taskPool.workerStatus == nil {
		taskPool.workerStatus = syncmap.New()
	}
	taskPool.workerStatus.Set(default_taskManager, true)

	for taskPool.isRunning {

		for _, id := range taskPool.workerStatus.Keys() {
			status, _ := taskPool.workerStatus.Get(id)
			if !status.(bool) {
				taskPool.workerStatus.Remove(id)
				taskPool.currentTaskStop.Remove(id)
			}
		}


		needWorkerNum := taskPool.workerNum
		if needWorkerNum > len(taskPool.jobsChannel) {
			needWorkerNum = len(taskPool.jobsChannel)
		}
		workerNum,_ :=taskPool.getWorkerNum()
		if needWorkerNum > workerNum {
			taskPool.workerFactory(needWorkerNum - workerNum)
		}

		time.Sleep(time.Microsecond*100)
	}
}

func (taskPool *TaskPool) show() {
	defer finally()
	defer func() {
		taskPool.workerStatus.Set(default_show, false)
	}()
	taskPool.workerStatus.Set(default_show, true)
	for {
		log.Info("================================")
		log.Info("状态：运行中：", taskPool.isRunning)
		log.Info("最大工作协程数：", taskPool.workerNum)
		log.Info("最大任务队列：", cap(taskPool.EntryChannel))
		log.Info("最大执行队列：", cap(taskPool.jobsChannel))
		log.Info("任务队列：", len(taskPool.EntryChannel))
		log.Info("执行队列：", len(taskPool.jobsChannel))
		log.Info("协程数：", taskPool.workerStatus.Count())
		workerNum,_ := taskPool.getWorkerNum()
		log.Info("工作协程数：",workerNum )
		log.Info("当前托管任务数：", taskPool.currentTaskStop.Count())
		log.Info("当前实际任务数：", *taskPool.workerCount)
		log.Info("当前协程状态：")
		for _, workId := range taskPool.workerStatus.Keys() {
			status, _ := taskPool.workerStatus.Get(workId)
			log.Info(workId, ":", status)
		}
		time.Sleep(time.Second * 2)
	}
}
