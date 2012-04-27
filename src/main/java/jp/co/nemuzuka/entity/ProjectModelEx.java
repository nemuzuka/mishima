/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.co.nemuzuka.entity;

import jp.co.nemuzuka.model.ProjectModel;

/**
 * プロジェクト画面表示用Entity.
 * @author kazumune
 */
public class ProjectModelEx {
	/** ProjectModel. */
	private ProjectModel model;

	/** プロジェクト概要表示用文字列. */
	private String projectSummaryView;
	
	/**
	 * @return projectSummaryView
	 */
	public String getProjectSummaryView() {
		return projectSummaryView;
	}

	/**
	 * @param projectSummaryView セットする projectSummaryView
	 */
	public void setProjectSummaryView(String projectSummaryView) {
		this.projectSummaryView = projectSummaryView;
	}

	/**
	 * @return model
	 */
	public ProjectModel getModel() {
		return model;
	}

	/**
	 * @param model セットする model
	 */
	public void setModel(ProjectModel model) {
		this.model = model;
	}
}
