package com.rl.renoj.service;

import com.rl.renoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.rl.renoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rl.renoj.model.entity.User;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 任磊
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2023-11-28 15:52:05
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);





}
