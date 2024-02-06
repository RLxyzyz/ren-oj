package com.rl.renoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.rl.renoj.model.dto.question.JudgeCase;
import com.rl.renoj.model.dto.question.JudgeConfig;
import com.rl.renoj.judge.codesandbox.model.JudgeInfo;
import com.rl.renoj.model.entity.Question;
import com.rl.renoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description默认判题策略
 * @date 2023/12/13 18:37:29
 */
public class DefaultJudgeStrategy implements JudgeStrategy{


    @Override
    public JudgeInfo dpJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        String message = judgeInfo.getMessage();
        if (message.equals("Compile Error"))
        {
            return judgeInfo;
        }
        List<String> inputList = judgeContext.getInputList();
        Question question = judgeContext.getQuestion();
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        JudgeInfoMessageEnum judgeInfoMessageEnum=JudgeInfoMessageEnum.WRONG_ANSWER;
        JudgeInfo judgeInfoResponse=new JudgeInfo();
        judgeInfoResponse.setTime(time);
        judgeInfoResponse.setMemory(memory);
        //先判断沙箱执行的结果输出数量是否和预期的输出数量相等
        if (outputList.size()!=inputList.size())
        {
            judgeInfoMessageEnum=JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        //依次判断输出项和预期是否相等
        for (int i=0;i<judgeCaseList.size();i++)
        {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i)))
            {
                judgeInfoMessageEnum=JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        //判题限制

        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long memoryLimit = judgeConfig.getMemoryLimit();
        Long timeLimit = judgeConfig.getTimeLimit();
        if (memory>memoryLimit)
        {
            judgeInfoMessageEnum=JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (time>timeLimit)
        {
            judgeInfoMessageEnum=JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoMessageEnum=JudgeInfoMessageEnum.ACCEPTED;
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}
