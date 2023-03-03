package com.sibebek.projecthunter.projecthunter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProjectDetail {
	
	@JsonProperty("nama_project")
	private String namaProject;

	@JsonProperty("link_project")
	private String link;

	@JsonProperty("maximun_pendanaan")
	private BigDecimal maximumPendanaan;

	@JsonProperty("durasi_project")
	private int durasiProject;

	@JsonProperty("imbal_hasil_tahunan")
	private String imbalHasil;
}
