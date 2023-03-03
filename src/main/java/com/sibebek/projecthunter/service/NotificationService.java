package com.sibebek.projecthunter.service;

public interface NotificationService {
	public Object sendNotification(String subject, String sendFrom, String sendTo, String message, Object... additonals);
}
