package com.rl.renoj.judge.codesandbox.impl;

import com.rl.renoj.judge.codesandbox.CodeSandbox;
import com.rl.renoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.rl.renoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.rl.renoj.judge.codesandbox.model.JudgeInfo;
import com.rl.renoj.model.enums.JudgeInfoMessageEnum;
import com.rl.renoj.model.enums.QuestionSubmitStatusEnum;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 示例输入输出代码沙箱
 * @date 2023/12/12 21:42:16
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setMessage("测试执行成功");
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        executeCodeResponse.setOutputList(executeCodeRequest.getInputList());
        return executeCodeResponse;
    }
}
