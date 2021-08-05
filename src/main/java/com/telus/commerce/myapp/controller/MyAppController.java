package com.telus.commerce.myapp.controller;

import com.telus.commerce.myapp.domain.Resource;
import com.telus.commerce.myapp.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api //For swagger document
public class MyAppController {

  private final ResourceService service;

  public MyAppController(ResourceService service) {
    this.service = service;
  }

  @ApiOperation(value = "GET a resource")
  @GetMapping(value = "/resource", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Resource getResource() {
    return service.getResource();
  }

  @ApiOperation(value = "POST a resource")
  @PostMapping(value = "/resource")
  public void postResource(@RequestBody @Valid Resource payload) {
    service.processResource(payload);
  }
}
