package com.rl.renoj.judge.impl;

import cn.hutool.json.JSONUtil;
import com.rl.renoj.common.ErrorCode;
import com.rl.renoj.exception.BusinessException;
import com.rl.renoj.judge.JudgeService;
import com.rl.renoj.judge.codesandbox.CodeSandbox;
import com.rl.renoj.judge.codesandbox.CodeSandboxFactory;
import com.rl.renoj.judge.codesandbox.CodeSandboxProxy;
import com.rl.renoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.rl.renoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.rl.renoj.judge.strategy.JudgeContext;
import com.rl.renoj.judge.strategy.JudgeManger;
import com.rl.renoj.model.dto.question.JudgeCase;
import com.rl.renoj.judge.codesandbox.model.JudgeInfo;
import com.rl.renoj.model.entity.Question;
import com.rl.renoj.model.entity.QuestionSubmit;
import com.rl.renoj.model.enums.QuestionSubmitStatusEnum;
import com.rl.renoj.service.QuestionService;
import com.rl.renoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 判题实现类
 * @date 2023/12/12 22:45:45
 */
@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Value("${codesandbox.type:example}")
    private String type;
    @Resource
    private JudgeManger judgeManger;
    @Override
    public QuestionSubmit doSubmit(long questionSubmitId) {
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
        QuestionSubmit questionSubmitUpdate=new QuestionSubmit();
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        questionSubmitUpdate.setId(questionSubmitId);
        boolean updateResult = questionSubmitService.updateById(questionSubmitUpdate);
        if (!updateResult)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新失败");
        }
        String code = questionSubmit.getCode();
        String language = questionSubmit.getLanguage();
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCases = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> inputList = judgeCases.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        List<String> outputList = judgeCases.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
        //调用代码沙箱，获取执行结果
        CodeSandbox codeSandbox= CodeSandboxFactory.newInstance(type);
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandbox);
        ExecuteCodeRequest executeCodeRequest=ExecuteCodeRequest
                .builder()
                .code(code)
                .language(language)
                .inputList(inputList).build();
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode(executeCodeRequest);
        JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();
        List<String> responseOutputList = executeCodeResponse.getOutputList();
        JudgeContext context=new JudgeContext();
        context.setJudgeInfo(judgeInfo);
        context.setJudgeCaseList(judgeCases);
        context.setInputList(inputList);
        context.setQuestion(question);
        context.setOutputList(outputList);
        context.setQuestionSubmit(questionSubmit);

        JudgeInfo judgeInfo1 = judgeManger.dpJudge(context);
        questionSubmitUpdate=new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo1));
        updateResult=questionSubmitService.updateById(questionSubmitUpdate);
        if (!updateResult)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionSubmitId);
        return questionSubmitResult;
    }
}
