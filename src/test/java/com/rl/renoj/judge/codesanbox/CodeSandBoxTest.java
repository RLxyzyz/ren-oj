package com.rl.renoj.judge.codesanbox;

import com.rl.renoj.judge.codesandbox.CodeSandbox;
import com.rl.renoj.judge.codesandbox.CodeSandboxFactory;
import com.rl.renoj.judge.codesandbox.CodeSandboxProxy;
import com.rl.renoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.rl.renoj.judge.codesandbox.model.ExecuteCodeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 代码沙箱测试
 * @date 2024/2/4 21:45:01
 */
@SpringBootTest
public class CodeSandBoxTest {
    @Value("${codesandbox.type:example}")
    private String type;
    @Test
    void executeCodeByProxy()
    {
        CodeSandbox codeSandbox= CodeSandboxFactory.newInstance(type);
        codeSandbox=new CodeSandboxProxy(codeSandbox);
        String code="public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int a=Integer.parseInt(args[0]);\n" +
                "        int b=Integer.parseInt(args[1]);\n" +
                "        System.out.println(\"结果:\"+(a+b));\n" +
                "    }\n" +
                "}";
        ExecuteCodeRequest executeCodeRequest=new ExecuteCodeRequest();
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");
        executeCodeRequest.setInputList(Arrays.asList("1 2","3 4"));
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }
}
