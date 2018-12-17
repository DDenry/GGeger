package com.GGeger.entity;

import java.util.List;

public class LonerResult {

	/**
	 * 学生总人数（男生人数，女生人数） 学生平均吃饭次数 孤僻学子信息（）
	 */

	private String billStartTime;
	private String billStopTime;
	private StudentsCountInfo studentsCountInfo;
	private float averageMealCount;

	private List<LonerInfo> lonerInfos;

	public LonerResult(String billStartTime, String billStopTime, StudentsCountInfo studentsCountInfo,
			float averagemealCount) {
		this.billStartTime = billStartTime;
		this.billStopTime = billStopTime;
		this.studentsCountInfo = studentsCountInfo;
		this.averageMealCount = averagemealCount;
	}

	public String getBillStartTime() {
		return billStartTime;
	}

	public String getBillStopTime() {
		return billStopTime;
	}

	public StudentsCountInfo getStudentsCountInfo() {
		return studentsCountInfo;
	}

	public float getAverageMealCount() {
		return averageMealCount;
	}

	public List<LonerInfo> getLonerInfos() {
		return lonerInfos;
	}

	public void setLonerInfos(List<LonerInfo> lonerInfos) {
		this.lonerInfos = lonerInfos;
	}
}
