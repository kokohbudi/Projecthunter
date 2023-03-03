package com.sibebek.projecthunter.projecthunter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunTrigger implements ApplicationRunner {
	@Autowired
	ProjectNotificationService projectNotificationService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.projectNotificationService.sendNotificationTotalProjects();
	}
}