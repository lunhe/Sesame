package log

import (
	"fmt"
	"io"
	"log"
	"os"
	"strings"
	"time"
)

// Debug function
var baseFileName = "qlHSocket-"
var info *log.Logger // 重要的信息
var debug *log.Logger
var errInfo *log.Logger
var warn *log.Logger
var track *log.Logger

func init() {
	BuildNewFile()
}

func Track(v ...interface{}) {
	if strings.Contains("track", "track") {//FIXME
		track.Output(3, fmt.Sprintln(v...))
	}
}


func Debug(v ...interface{}) {
	if strings.Contains("debug", "debug") {//FIXME
		debug.Output(3, fmt.Sprintln(v...))
	}
}

func Warn(v ...interface{}) {
	if strings.Contains("warn", "warn") {//FIXME
		warn.Output(3, fmt.Sprintln(v...))
	}
}

func Info(v ...interface{}) {
	if strings.Contains("info", "info") {//FIXME
		info.Output(3, fmt.Sprintln(v...))
	}
}

func ErrInfo(v ...interface{}) {
	errInfo.Output(3, fmt.Sprintln(v...))
}

func BuildNewFile() {
	logFile, err := os.OpenFile(buildLogFileName(), os.O_CREATE|os.O_RDWR|os.O_APPEND, 0666) //打开文件，若果文件不存在就创建一个同名文件并打开
	if err != nil {
		log.Fatalln("Failed to open error log file:", err)
	}
	/*info = log.New(logFile, "[info]", log.Lshortfile)
	debug = log.New(logFile, "[debug]", log.Lshortfile)
	errInfo = log.New(logFile, "[errInfo]", log.Lshortfile)*/
	info = log.New(io.MultiWriter(logFile, os.Stderr), "[info]", log.Lshortfile|log.Ldate|log.Ltime) //|log.Ldate|log.Ltime
	errInfo = log.New(io.MultiWriter(logFile, os.Stderr), "[errInfo]", log.Lshortfile|log.Ldate|log.Ltime) //|log.Ldate|log.Ltime
	debug = log.New(io.MultiWriter(logFile, os.Stderr), "[debug]", log.Lshortfile|log.Ldate|log.Ltime) //|log.Ldate|log.Ltime
}

func buildLogFileName() string {
	dateStr := time.Now().Format("2006-01")
	fileName := baseFileName + dateStr + ".log"
	return fileName
}
