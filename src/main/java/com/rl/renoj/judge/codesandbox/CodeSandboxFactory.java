package com.rl.renoj.judge.codesandbox;

import com.rl.renoj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.rl.renoj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.rl.renoj.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 静态代码沙箱工厂，传入字符串，构造相应的代码沙箱
 * @date 2023/12/12 21:50:39
 */
public class CodeSandboxFactory {
    public static CodeSandbox newInstance(String type)
    {
        switch (type)
        {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}
