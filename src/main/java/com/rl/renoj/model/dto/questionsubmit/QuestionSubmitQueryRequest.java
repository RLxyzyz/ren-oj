package com.rl.renoj.model.dto.questionsubmit;

import com.rl.renoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 查询请求
 * @date 2023/11/29 15:22:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {
    /**
     * 题目 id
     */
    private Long questionId;


    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 用户ID
     */
    private Integer userId;

    private static final long serialVersionUID = 1L;
}
