package com.helun.menu.performance;
/**
 * 提交接口，控制层参数对象
 * @author Administrator
 *
 */
public class TestVO extends MergeSETaskBO{
	/**
	 * 答案数据
	 */
	String date = null ;
	/**
	 * 文件MultipartFile
	 */
	Object[] files = null ;
	/**
	 * 本次测验耗时
	 */
	private Long testUseTime;
	/**
	 * 本次测验的笔记
	 */
	private String noteContent;
	/**
	 * 本次提交关联的courseid
	 */
	private Long courseId;
	/**
	 * (重做，非重做)WKTRedoApplyAnswer
	 */
	private Integer type ;
	
	private float id ;
	
	
	
	public float getId() {
		return id;
	}
	public void setId(float id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Object[] getFiles() {
		return files;
	}
	public void setFiles(Object[] files) {
		this.files = files;
	}
	public Long getTestUseTime() {
		return testUseTime;
	}
	public void setTestUseTime(Long testUseTime) {
		this.testUseTime = testUseTime;
	}
	public String getNoteContent() {
		return noteContent;
	}
	public void setNoteContent(String noteContent) {
		this.noteContent = noteContent;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
}
