package com.rl.renoj.model.dto.questionsubmit;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 创建请求
 * @date 2023/11/29 15:22:53
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {
    /**
     * 题目 id
     */
    private Long questionId;


    /**
     * 编程语言
     */
    private String language;

    /**
     * 代码
     */
    private String code;

    private static final long serialVersionUID = 1L;
}
