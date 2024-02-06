package com.rl.renoj.judge.strategy;

import com.rl.renoj.model.dto.question.JudgeCase;
import com.rl.renoj.judge.codesandbox.model.JudgeInfo;
import com.rl.renoj.model.entity.Question;
import com.rl.renoj.model.entity.QuestionSubmit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 判题策略上下文
 * @date 2023/12/13 18:36:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeContext {
    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private Question question;

    private List<JudgeCase> judgeCaseList;
    private QuestionSubmit questionSubmit;
}
