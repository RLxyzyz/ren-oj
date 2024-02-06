package com.rl.renoj.judge.strategy;

import com.rl.renoj.judge.codesandbox.model.JudgeInfo;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 判题策略
 * @date 2023/12/13 18:35:30
 */
public interface JudgeStrategy {
    JudgeInfo dpJudge(JudgeContext judgeContext);
}
