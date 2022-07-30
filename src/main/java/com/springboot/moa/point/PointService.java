package com.springboot.moa.point;

import com.springboot.moa.config.BaseException;
import com.springboot.moa.user.UserDao;
import com.springboot.moa.user.UserProvider;
import com.springboot.moa.user.model.PostPointsReq;
import com.springboot.moa.user.model.PostPointsRes;
import com.springboot.moa.user.model.PostUserReq;
import com.springboot.moa.user.model.PostUserRes;
import com.springboot.moa.utils.JwtService;
import com.springboot.moa.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.springboot.moa.config.BaseResponseStatus.DATABASE_ERROR;
import static com.springboot.moa.config.BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR;

@Service
public class PointService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PointDao pointDao;
    private final PointProvider pointProvider;
    private final JwtService jwtService;


    @Autowired
    public PointService(PointDao pointDao, PointProvider pointProvider, JwtService jwtService) {
        this.pointDao = pointDao;
        this.pointProvider = pointProvider;
        this.jwtService = jwtService;
    }


    public PostPointsRes addPointHistory(PostPointsReq postPointsReq) throws BaseException {
        try {
            System.out.println(postPointsReq.getUserId());
            int pointId = pointDao.addPointHistory(postPointsReq.getUserId(),postPointsReq.getAddAmount(),postPointsReq.getSubAmount());
            pointDao.updateUserPoint(postPointsReq);
            return new PostPointsRes(pointId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
