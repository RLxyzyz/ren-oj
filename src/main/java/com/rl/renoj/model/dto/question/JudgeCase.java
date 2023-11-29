package com.rl.renoj.model.dto.question;

import lombok.Data;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 题目用例
 * @date 2023/11/28 21:17:07
 */
@Data
public class JudgeCase {
    /**输入用例*/
    private String input;
    /*
    * 输出用例
    * */
    private String output;
}
