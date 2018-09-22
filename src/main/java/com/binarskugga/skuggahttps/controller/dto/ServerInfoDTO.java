package com.binarskugga.skuggahttps.controller.dto;

import com.binarskugga.skuggahttps.*;
import com.binarskugga.skuggahttps.utils.*;
import lombok.*;

public class ServerInfoDTO {

	@Getter @Setter private String name;
	@Getter @Setter private int thread_count;

	public ServerInfoDTO() {
		PropertiesConfiguration config = HttpConfigProvider.get();
		this.name = config.getString("headers.server").orElse("SkuggaHttps");
		this.thread_count = config.getInt("server.threads").orElse(1);
	}

}
