package com.telus.commerce.myapp.service;

import com.telus.commerce.myapp.domain.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

  private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

  public Resource getResource() {
    return new Resource("hello", "world");
  }

  public void processResource(Resource payload) {
      logger.info(payload.toString());
  }
}
