package taskpool

type taskClose func()

type Task struct {
	function func(args ...interface{}) error
	stop    taskClose
}

func NewTask(function func(args ...interface{}) error, close func()) *Task {
	if function == nil  {
		panic("方法不能为空")
	}
	task := Task{
		function: function,
		stop: taskClose(close),
	}
	return &task
}

func (task *Task) Stop() {
	task.stop()
}

func (task *Task) Execute(args ...interface{}) error {
	return task.function(args)
}
