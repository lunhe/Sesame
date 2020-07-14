package com.helun.menu.performance;

public class TestTaskBOBuilder implements IMergeSETaskBOBuilder{

	private String name ;
	private Long classId ;
	private Long id ;
	private Integer age ;
	
	@Override
	public MergeSETaskBO build() {
		MergeSETaskBO mergeSETaskBO = new MergeSETaskBO() ;
		mergeSETaskBO.addProperty("name", name) ;
		mergeSETaskBO.addProperty("classId", classId) ;
		mergeSETaskBO.addProperty("id", id) ;
		mergeSETaskBO.addProperty("age", age) ;
		return mergeSETaskBO ;
	}

	@Override
	public String key() {
		return classId.toString();
	}

	@Override
	public String serviceName() {
		return "test";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public Integer proxyType() {
		return IMergeServiceProxy.FIRST_NONE_THEN_TRANSACTION;
	}
}
