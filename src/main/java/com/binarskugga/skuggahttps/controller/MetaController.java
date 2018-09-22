package com.binarskugga.skuggahttps.controller;

import com.binarskugga.skuggahttps.annotation.*;
import com.binarskugga.skuggahttps.auth.role.*;
import com.binarskugga.skuggahttps.controller.dto.*;
import com.binarskugga.skuggahttps.http.*;
import com.binarskugga.skuggahttps.http.api.*;
import com.binarskugga.skuggahttps.utils.security.*;
import org.shredzone.acme4j.util.*;

import java.security.*;

@Controller("meta")
public class MetaController extends AbstractController {

	@Get
	@Access({AllAccess.class})
	public Response ping() {
		return Response.ok();
	}

	@Get
	@Access({AllAccess.class})
	public ServerInfoDTO info() {
		return new ServerInfoDTO();
	}

}
