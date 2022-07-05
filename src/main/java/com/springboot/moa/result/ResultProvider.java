package com.springboot.moa.result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResultProvider {
    private final ResultDao resultDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    public ResultProvider(ResultDao resultDao) {
        this.resultDao = resultDao;
    }
}
