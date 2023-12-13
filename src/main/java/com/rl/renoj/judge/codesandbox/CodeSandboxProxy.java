package com.rl.renoj.judge.codesandbox;

import com.rl.renoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.rl.renoj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 代理模式，代码沙箱代理，实现日志功能
 * @date 2023/12/12 22:23:06
 */
@Slf4j
@AllArgsConstructor
public class CodeSandboxProxy implements CodeSandbox {
    private CodeSandbox codeSandbox;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("请求信息:"+executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("响应信息:"+executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
