package com.springboot.moa.post;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class PostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostDao postDao;
    private final PostProvider postProvider;

    @Autowired
    public PostService(PostDao postDao, PostProvider postProvider) {
        this.postDao = postDao;
        this.postProvider = postProvider;
    }
}
