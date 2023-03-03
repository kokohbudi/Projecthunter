package com.sibebek.projecthunter.service;

import com.sibebek.projecthunter.dto.ProjectDetail;

import java.io.IOException;
import java.util.List;

public interface ProjectNotificationService {

	List<ProjectDetail> sendNotificationTotalProjects() throws IOException;

	List<ProjectDetail> sendNotificationForProjectCriteria(int prosentase, int durasi) throws IOException;
}
