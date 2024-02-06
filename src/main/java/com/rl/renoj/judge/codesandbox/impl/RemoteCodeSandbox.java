package com.rl.renoj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.rl.renoj.common.ErrorCode;
import com.rl.renoj.exception.BusinessException;
import com.rl.renoj.judge.codesandbox.CodeSandbox;
import com.rl.renoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.rl.renoj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 远程代码沙箱
 * @date 2023/12/12 21:42:16
 */
@Slf4j
public class RemoteCodeSandbox implements CodeSandbox {
    private static final String AUTH_HEADER="auth";
    private static final String AUTH_REQUEST_HEADER="secretKey";
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url="http://localhost:8801/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .body(json)
                .header(AUTH_HEADER,AUTH_REQUEST_HEADER)
                .execute()
                .body();
        log.info("responseStr={}",responseStr);
        if (StringUtils.isBlank(responseStr))
        {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR,"executeCode remoteSandBox error,message = "+responseStr);
        }
        return JSONUtil.toBean(responseStr,ExecuteCodeResponse.class);
    }
}
