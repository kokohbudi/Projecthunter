package com.sibebek.projecthunter.projecthunter.controller;

import com.sibebek.projecthunter.projecthunter.dto.ProjectDetail;
import com.sibebek.projecthunter.projecthunter.service.ProjectNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class ProjectHunterController {
	@Autowired
	private ProjectNotificationService projectNotificationService;

	@PostMapping(value = "notifyproject")
	public ResponseEntity<Map<String, List<ProjectDetail>>> sendProjectNotification(@RequestParam int prosentase, @RequestParam int durasi) throws IOException {
		final List<ProjectDetail> projectDetailList = this.projectNotificationService.sendNotificationForProjectCriteria(prosentase, durasi);
		Map responseMap = Map.of("projects", projectDetailList);
		if (projectDetailList.isEmpty()) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok().body(responseMap);
		}
	}
}
