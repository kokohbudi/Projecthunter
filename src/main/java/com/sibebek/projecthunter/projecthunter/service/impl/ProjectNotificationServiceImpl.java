package com.sibebek.projecthunter.projecthunter.service.impl;

import com.sibebek.projecthunter.projecthunter.dto.ProjectDetail;
import com.sibebek.projecthunter.projecthunter.repository.ProjectInquiryRepository;
import com.sibebek.projecthunter.projecthunter.service.NotificationService;
import com.sibebek.projecthunter.projecthunter.service.ProjectNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ProjectNotificationServiceImpl implements ProjectNotificationService {
	@Autowired
	ProjectInquiryRepository projectInquiryRepository;

	@Autowired
	NotificationService notificationService;

	@Value("${projecthunter.notification.wirepusher.token}")
	private String sendTo;

	@Override
	public List<ProjectDetail> sendNotificationTotalProjects() throws IOException {
		List<ProjectDetail> allProject = this.projectInquiryRepository.getAllProject();
		int projectsSize = allProject.size();
		String subject = "Dana Syariah Project Hunter";
		String body = String.format("Server Running and found %d project(s)", projectsSize);
		String type = "serverup";
		String link = "";
		this.notificationService.sendNotification(subject, null, this.sendTo, body, link, type);
		return allProject;
	}

	@Override
	public List<ProjectDetail> sendNotificationForProjectCriteria(int prosentase, int durasi) throws IOException {
		List<ProjectDetail> projects = this.projectInquiryRepository.projectInquiry(prosentase, durasi);
		for (ProjectDetail project : projects) {
			final String subject = "Dana Syariah Project Hunter\n" + project.getNamaProject();
			final String imbal_hasil = project.getImbalHasil();
			final String sisa_project = String.valueOf(project.getMaximumPendanaan());
			final String durasi_project = String.valueOf(project.getDurasiProject());
			final String link = project.getLink();
			final String type = "foundproject";
			final String body = String.format("Imbal Hasil: %s%%\nSisa project: %,.2f\nDurasi: %d", imbal_hasil, sisa_project, durasi_project);
			final int result = (int) this.notificationService.sendNotification(this.sendTo, null, subject, body, link, type);
		}
		return projects;
	}
}
