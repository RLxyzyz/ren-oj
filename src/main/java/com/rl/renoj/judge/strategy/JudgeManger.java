package com.rl.renoj.judge.strategy;

import com.rl.renoj.judge.codesandbox.model.JudgeInfo;
import com.rl.renoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 根据不同的语言选择不停的判题策略
 * @date 2024/1/31 20:40:09
 */
@Service
public class JudgeManger {
    public JudgeInfo dpJudge(JudgeContext judgeContext){
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        //可以根据不同的语言选择不同的策略
        JudgeStrategy judgeStrategy=new DefaultJudgeStrategy();
        /*if ("java".equals(language))
        {
            judgeStrategy=new JavaJudgeStrategy();
        }*/
        return judgeStrategy.dpJudge(judgeContext);
    }
}
