package com.sibebek.projecthunter.service.impl;

import com.sibebek.projecthunter.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class WirePusherNotificationService implements NotificationService {

	@Value("${projecthunter.notification.wirepusher.url}")
	private String wirePusherUrl;

	private void postToWirePusher(String subject, String sendTo, String message, String link, String type) throws IOException {
		Connection connection = Jsoup.connect(this.wirePusherUrl)
				.header("Content-Type", "application/x-www-form-urlencoded")
				.data("id", sendTo)
				.data("title", subject)
				.data("message", message)
				.data("type", type)
				.data("action", link)
				.data("message_id", String.valueOf(Math.abs(link.hashCode() % (int) Math.pow(10, 8))))
				.ignoreHttpErrors(true)
				.ignoreContentType(true)
				.method(Connection.Method.POST);

		try {
			Response response = connection.execute();
			log.info("status code: {}", response.statusCode());
			log.info("response: {}", response.body());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @param subject    = title
	 * @param sendFrom   = nothing
	 * @param sendTo     = wirepusher id
	 * @param message    = message
	 * @param additonals = {link, type}
	 * @return 1 if success, 0 if failed
	 */
	@Override
	public Object sendNotification(String subject, String sendFrom, String sendTo, String message, Object... additonals) {
		try {
			this.postToWirePusher(subject, sendTo, message, (String) additonals[0], (String) additonals[1]);
			return 1;
		} catch (IOException e) {
			log.error("error when send notification", e);
			return 0;
		}
	}
}
