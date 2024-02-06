package com.rl.renoj.judge;

import com.rl.renoj.model.entity.QuestionSubmit;
import com.rl.renoj.model.vo.QuestionSubmitVO;
import com.rl.renoj.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 判题服务
 * @date 2023/12/12 22:40:08
 */
public interface JudgeService {
    QuestionSubmit doSubmit(long questionSubmitId);
}
