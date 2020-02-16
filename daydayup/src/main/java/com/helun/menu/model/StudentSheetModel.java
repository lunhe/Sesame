package com.helun.menu.model;

import java.util.List;

import com.google.common.collect.Lists;

public class StudentSheetModel extends BaseEntity{
	private String name ;
	private String number ;
	private String submitTime ;
	private String totalSocre ;
	private String totalCorrectRate ;
	private List<AnswerResult> answerResults;
	public String getName() {
		return name;
	}
	public StudentSheetModel setName(String name) {
		this.name = name;
		return this ;
	}
	public String getNumber() {
		return number;
	}
	public StudentSheetModel setNumber(String number) {
		this.number = number;
		return this ;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public StudentSheetModel setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
		return this ;
	}
	public String getTotalSocre() {
		return totalSocre;
	}
	public StudentSheetModel setTotalSocre(String totalSocre) {
		this.totalSocre = totalSocre;
		return this ;
	}
	public String getTotalCorrectRate() {
		return totalCorrectRate;
	}
	public StudentSheetModel setTotalCorrectRate(String totalCorrectRate) {
		this.totalCorrectRate = totalCorrectRate;
		return this ;
	}

	public List<AnswerResult> getAnswerResults() {
		return answerResults;
	}
	public StudentSheetModel setAnswerResults(List<AnswerResult> answerResults) {
		this.answerResults = answerResults;
		return this ;
	}
	
	public StudentSheetModel addAnswerResult(String requestNum , String score , String correctRate){
		if(answerResults == null){
			answerResults = Lists.newArrayList() ;
		}
		AnswerResult answerResult = new AnswerResult().setCorrectRate(correctRate).setRequestNum(requestNum).setScore(score);
		answerResults.add(answerResult);
		return this;
	}
	
	public class AnswerResult{
		private String requestNum ;
		private String score;
		private String correctRate ;
		public String getRequestNum() {
			return requestNum;
		}
		public AnswerResult setRequestNum(String requestNum) {
			this.requestNum = requestNum;
			return this;
		}
		public String getScore() {
			return score;
		}
		public AnswerResult setScore(String score) {
			this.score = score;
			return this;
		}
		public String getCorrectRate() {
			return correctRate;
		}
		public AnswerResult setCorrectRate(String correctRate) {
			this.correctRate = correctRate;
			return this;
		}
	}
	
	
	
}
