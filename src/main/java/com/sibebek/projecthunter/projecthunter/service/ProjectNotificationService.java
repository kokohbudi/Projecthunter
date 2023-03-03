package com.sibebek.projecthunter.projecthunter.service;

import com.sibebek.projecthunter.projecthunter.dto.ProjectDetail;

import java.io.IOException;
import java.util.List;

public interface ProjectNotificationService {

	List<ProjectDetail> sendNotificationTotalProjects() throws IOException;

	List<ProjectDetail> sendNotificationForProjectCriteria(int prosentase, int durasi) throws IOException;
}
