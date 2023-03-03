package com.sibebek.projecthunter.projecthunter.repository.impl;

import com.sibebek.projecthunter.projecthunter.dto.ProjectDetail;
import com.sibebek.projecthunter.projecthunter.repository.ProjectInquiryRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Repository
@Slf4j
public class ProjectInquiryRepositoryImpl implements ProjectInquiryRepository {

//	private final Logger logger = LoggerFactory.getLogger(ProjectInquiryRepositoryImpl.class);

	private static Connection connection;

	@Value("${projecthunter.url-inquiry}")
	private String inquiryUrl;

	public String getLink(final Element element) {
		return element.getElementsByTag("a").get(0).attr("href");
	}

	public BigDecimal getTopupYangDiijinkan(final BigDecimal danaDibutuhkanBigDecimal, final BigDecimal prosentaseTerpenuhiBigDecimal) {
		final BigDecimal topupYangDiijinkan = danaDibutuhkanBigDecimal.subtract(prosentaseTerpenuhiBigDecimal.divide(new BigDecimal(100)).multiply(danaDibutuhkanBigDecimal));
		return topupYangDiijinkan;
	}

	public BigDecimal getProsentaseTerpenuhi(final Element element) {
		final String prosentaseTerpenuhi = element.getElementsByClass("title").get(0).text().replace("%", "").strip();
		final BigDecimal prosentaseTerpenuhiBigDecimal = new BigDecimal(prosentaseTerpenuhi);
		return prosentaseTerpenuhiBigDecimal;
	}

	public String getNamaProject(final Element element) {
		return element.getElementsByClass("bold").get(0).text();
	}

	public Document getTextResponseFromURL(final String url) {
		log.info("get text response from url: {}", url);
		final Document doc;
		try {
			if (connection == null) {
				connection = Jsoup.connect(url);
			}
			doc = connection.url(url).get();
			return doc;
		} catch (final IOException e) {
			log.error("error when get text response from url: {}", url, e);
		}
		return null;
	}

	private Elements getProjectList(final Document document) {
		return document.getElementsByAttributeValue("class", "detail");
	}

	private Elements getElementsByClass(final Element parentElement, final String className) {
		return parentElement.getElementsByClass(className);
	}

	private String findByRegex(final String regex, final String text, final int group) {
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group(group);
		}
		return null;
	}

	private BigDecimal getDanaDibutuhkan(final Element element) {
		final String regexDanaDibutuhkan = "Rp ([\\d+.]+)";
		final String danaDibutuhkan = this.getElementsByClass(element, "p-text-bold").get(0).text().trim();
		return new BigDecimal(this.findByRegex(regexDanaDibutuhkan, danaDibutuhkan, 1).replace(".", ""));
	}

	private int getDurasiProject(final Element element) {
		final String regexDurasiProject = "\\d+";
		final String durasiProject = this.getElementsByClass(element, "p-text-bold").get(1).text().trim();
		return Integer.parseInt(this.findByRegex(regexDurasiProject, durasiProject, 0));
	}

	@Override
	public List<ProjectDetail> getAllProject() throws IOException {
		return this.projectInquiry(0, 0);
	}

	@Override
	public List<ProjectDetail> projectInquiry(final int notifProcParam, final int durasiProjectParam) throws IOException {
		int pageCounter = 1;
		List<ProjectDetail> projectDetailList = new ArrayList<ProjectDetail>();
		while (true) {
			final Document doc = this.getTextResponseFromURL(this.inquiryUrl + pageCounter);
			final Elements projects = this.getProjectList(doc);
			final String regexProsentaseImbalHasil = "\\(((.+?)\\%)";

			if (!projects.isEmpty()) {
				pageCounter++;

				for (final Element project : projects) {
					final Elements loc = this.getElementsByClass(project, "location");
					final String imbalHasil = loc.get(2).getElementsByClass("p-text-bold").get(0).text();
					final String persentase = this.findByRegex(regexProsentaseImbalHasil, imbalHasil, 2);
					final int durasiProject = this.getDurasiProject(loc.get(1));
					if (Double.parseDouble(persentase) >= notifProcParam && durasiProject >= durasiProjectParam) {
						ProjectDetail projectDetail = new ProjectDetail();
						final BigDecimal danaDibutuhkanBigDecimal = this.getDanaDibutuhkan(loc.get(1));


						final String namaProject = this.getNamaProject(project);
						final BigDecimal prosentaseTerpenuhiBigDecimal = this.getProsentaseTerpenuhi(project);
						final BigDecimal topupUpYangDiijinkan = this.getTopupYangDiijinkan(danaDibutuhkanBigDecimal, prosentaseTerpenuhiBigDecimal);//danaDibutuhkanBigDecimal.subtract(prosentaseTerpenuhiBigDecimal.divide(new BigDecimal(100)).multiply(danaDibutuhkanBigDecimal));
						final String link = this.getLink(project);

						projectDetail.setDurasiProject(durasiProject);
						projectDetail.setNamaProject(namaProject);
						projectDetail.setMaximumPendanaan(topupUpYangDiijinkan);
						projectDetail.setLink(link);
						projectDetail.setImbalHasil(persentase.concat(" %"));

						projectDetailList.add(projectDetail);
					}
				}
			} else {
				break;
			}

		}
		return projectDetailList;
	}
}
