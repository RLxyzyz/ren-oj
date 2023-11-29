package com.rl.renoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rl.renoj.common.ErrorCode;
import com.rl.renoj.exception.BusinessException;
import com.rl.renoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.rl.renoj.model.entity.*;
import com.rl.renoj.model.enums.QuestionSubmitLanguageEnum;
import com.rl.renoj.model.enums.QuestionSubmitStatusEnum;
import com.rl.renoj.service.QuestionService;
import com.rl.renoj.service.QuestionSubmitService;
import com.rl.renoj.mapper.QuestionSubmitMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author 任磊
* @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
* @createDate 2023-11-28 15:52:05
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{
    @Resource
    private QuestionService questionService;

    /**
     * 点赞
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum enumByValue = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if(enumByValue==null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言错误");
        }
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        long userId = loginUser.getId();
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmit.getCode());
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        boolean save = this.save(questionSubmit);
        if (!save)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交失败");
        }
        return questionSubmit.getId();
    }


}




