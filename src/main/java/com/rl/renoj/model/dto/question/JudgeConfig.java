package com.rl.renoj.model.dto.question;

import lombok.Data;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 题目配置
 * @date 2023/11/28 21:17:07
 */
@Data
public class JudgeConfig {
    /*
    * 时间限制(ms)
    * */
    private Long timeLimit;

    /*
     * 内存限制(kb)
     * */
    private Long memoryLimit;

    /*
     * 堆栈限制(kb)
     * */
    private Long stackLimit;
}
