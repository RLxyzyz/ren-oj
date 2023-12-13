package com.rl.renoj.judge.impl;

import com.rl.renoj.common.ErrorCode;
import com.rl.renoj.exception.BusinessException;
import com.rl.renoj.judge.JudgeService;
import com.rl.renoj.model.entity.Question;
import com.rl.renoj.model.entity.QuestionSubmit;
import com.rl.renoj.model.enums.QuestionSubmitStatusEnum;
import com.rl.renoj.model.vo.QuestionSubmitVO;
import com.rl.renoj.service.QuestionService;
import com.rl.renoj.service.QuestionSubmitService;

import javax.annotation.Resource;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 判题实现类
 * @date 2023/12/12 22:45:45
 */
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Override
    public QuestionSubmitVO doSubmit(long questionSubmitId) {
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目提交信息为空");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目信息为空");
        }
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue()))
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"题目状态错误");
        }
        questionSubmit.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        return null;
    }
}
