package com.sibebek.projecthunter.projecthunter.repository;

import com.sibebek.projecthunter.projecthunter.dto.ProjectDetail;

import java.io.IOException;
import java.util.List;

public interface ProjectInquiryRepository {
	public List<ProjectDetail> getAllProject() throws IOException;

	public List<ProjectDetail> projectInquiry(int notifProcParam, int durasiProjectParam) throws IOException;
}
