package com.binarskugga.skuggahttps.controller.dto;

import com.binarskugga.skuggahttps.*;
import com.binarskugga.skuggahttps.utils.*;
import lombok.*;

public class ServerInfoDTO {

	@Getter @Setter private String name;
	@Getter @Setter private String description;
	@Getter @Setter private String version;
	@Getter @Setter private String root;
	@Getter @Setter private int thread_count;


	public ServerInfoDTO() {
		PropertiesConfiguration config = HttpConfigProvider.get();
		this.name = config.getString("server.name").orElse("SkuggaHttps");
		this.description = config.getString("server.description").orElse("");
		this.version = config.getString("server.version").orElse("");
		this.root = config.getString("server.root").orElse("");
		this.thread_count = config.getInt("server.threads").orElse(1);

	}

}
