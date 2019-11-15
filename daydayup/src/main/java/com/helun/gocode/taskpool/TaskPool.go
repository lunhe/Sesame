package taskpool

import(
	"../syncmap"
)

const mIN_CACHE_SIZE  = 1
const mIN_WORKER_NUM  = 1

type TaskPool struct {
	EntryChannel chan *Task
	workerNum int
	jobsChannel chan *Task
	workerStatus syncmap.ConcurrentMap
	currentTaskStop syncmap.ConcurrentMap
	isRunning bool
}

func NewTaskPool(workerNum int,cacheSize int) *TaskPool{
	defer finally()
	if workerNum < mIN_WORKER_NUM{
		workerNum = mIN_WORKER_NUM
	}
	if cacheSize < mIN_CACHE_SIZE {
		cacheSize = mIN_CACHE_SIZE
	}
	taskPool := TaskPool{
		EntryChannel: make(chan *Task,cacheSize),
		workerNum:    workerNum,
		jobsChannel:   make(chan *Task,2*workerNum),
		workerStatus : syncmap.New(),
		currentTaskStop:syncmap.New(),
		isRunning : true,
	}
	go taskPool.goManager()
	return &taskPool
}

func ( taskPool *TaskPool) AddTask(task *Task){
	defer finally()
	if taskPool.isRunning {
		if 	taskPool.getWorkerNum() == 0{
			taskPool.workerFactory(1)
		}
		taskPool.EntryChannel <- task
	}else {
		panic("任务池已关闭")
	}
}

func (taskPool *TaskPool) Close(isSync bool){
	defer finally()
	if isSync {
		taskPool.closePool()
	}else {
		go taskPool.closePool()
	}
}

func (taskPool *TaskPool) Start(){
	defer finally()
	taskPool.dealTask()
}

func (taskPool *TaskPool) ReStart(){

}

func (taskPool *TaskPool) Signal(){

}

func (taskPool *TaskPool) Await(){

}

func (taskPool *TaskPool) Show(){
	go taskPool.show()
}
