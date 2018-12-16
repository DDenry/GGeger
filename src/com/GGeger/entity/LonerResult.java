package com.GGeger.entity;

import java.util.List;

public class LonerResult {

	/**
	 * 学生总人数，平均吃饭次数
	 */

	private StudentsCountInfo studentsCountInfo;
	private float averageMealCount;

	private List<LonerInfo> lonerInfos;

	public StudentsCountInfo getStudentsCountInfo() {
		return studentsCountInfo;
	}

	public float getAverageMealCount() {
		return averageMealCount;
	}

	public LonerResult(StudentsCountInfo studentsCountInfo, float averagemealCount) {
		this.studentsCountInfo = studentsCountInfo;
		this.averageMealCount = averagemealCount;
	}

	public List<LonerInfo> getLonerInfos() {
		return lonerInfos;
	}

	public void setLonerInfos(List<LonerInfo> lonerInfos) {
		this.lonerInfos = lonerInfos;
	}
}
