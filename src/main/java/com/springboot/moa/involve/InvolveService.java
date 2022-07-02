package com.springboot.moa.involve;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class InvolveService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final InvolveDao involveDao;
    private final InvolveProvider involveProvider;


    @Autowired
    public InvolveService(InvolveDao involveDao, InvolveProvider involveProvider) {
        this.involveDao = involveDao;
        this.involveProvider = involveProvider;
    }


}
