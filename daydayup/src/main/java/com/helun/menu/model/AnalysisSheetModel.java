package com.helun.menu.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AnalysisSheetModel extends BaseEntity{
	private String title ;
	private String time ;
	private Map<String,String> requestsToCorrectRate ;
	private String avgCorrectRate ;
	private Map<String,String> pointsToCorrectRate ;
	private List<StudentResult> studentResults ;
	
	
	
	public String getTitle() {
		return title;
	}



	public AnalysisSheetModel setTitle(String title) {
		this.title = title;
		return this;
	}



	public String getTime() {
		return time;
	}



	public AnalysisSheetModel setTime(String time) {
		this.time = time;
		return this;
	}



	public Map<String, String> getRequestsToCorrectRate() {
		return requestsToCorrectRate;
	}



	public AnalysisSheetModel setRequestsToCorrectRate(Map<String, String> requestsToCorrectRate) {
		this.requestsToCorrectRate = requestsToCorrectRate;
		return this;
	}

	public AnalysisSheetModel addRequestsToCorrectRate(String requestNum,String correctRate) {
		if(requestsToCorrectRate==null){
			requestsToCorrectRate = Maps.newLinkedHashMap();
		}
		requestsToCorrectRate.put(requestNum, correctRate) ;
		return this;
	}


	public String getAvgCorrectRate() {
		return avgCorrectRate;
	}



	public AnalysisSheetModel setAvgCorrectRate(String avgCorrectRate) {
		this.avgCorrectRate = avgCorrectRate;
		return this;
	}



	public Map<String, String> getPointsToCorrectRate() {
		return pointsToCorrectRate;
	}



	public AnalysisSheetModel setPointsToCorrectRate(Map<String, String> pointsToCorrectRate) {
		this.pointsToCorrectRate = pointsToCorrectRate;
		return this;
	}
	
	
	public AnalysisSheetModel addPointsToCorrectRate(String point , String correctRate) {
		if(pointsToCorrectRate==null){
			pointsToCorrectRate = Maps.newLinkedHashMap();
		}
		pointsToCorrectRate.put(point, correctRate);
		return this;
	}



	public List<StudentResult> getStudentResults() {
		return studentResults;
	}

	public AnalysisSheetModel addStudentResult(String name ,String score ,String correctRate , String submitTime , String submitStatus) {
		if(studentResults == null){
			studentResults = Lists.newArrayList();
		}
		StudentResult studentResult = new StudentResult().setCorrectRate(correctRate)
				.setName(name).setScore(score).setSubmitStatus(submitStatus).setSubmitTime(submitTime);
		studentResults.add(studentResult);
		return this;
	}
	

	public AnalysisSheetModel setStudentResults(List<StudentResult> studentResults) {
		this.studentResults = studentResults;
		return this;
	}



	public class StudentResult{
		private String name ;
		private String score ;
		private String correctRate ;
		private String submitTime ;
		private String submitStatus ;
		
		public String getName() {
			return name;
		}
		public StudentResult setName(String name) {
			this.name = name;
			return this;
		}
		public String getScore() {
			return score;
		}
		public StudentResult setScore(String score) {
			this.score = score;
			return this;
		}
		public String getCorrectRate() {
			return correctRate;
		}
		public StudentResult setCorrectRate(String correctRate) {
			this.correctRate = correctRate;
			return this;
		}
		public String getSubmitTime() {
			return submitTime;
		}
		public StudentResult setSubmitTime(String submitTime) {
			this.submitTime = submitTime;
			return this;
		}
		public String getSubmitStatus() {
			return submitStatus;
		}
		public StudentResult setSubmitStatus(String submitStatus) {
			this.submitStatus = submitStatus;
			return this;
		}
		
		
	}
}
