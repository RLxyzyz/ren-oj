package com.rl.renoj.model.vo;

import com.rl.renoj.model.entity.QuestionSubmit;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 任磊
 * @version 1.0
 * @project renoj-backend
 * @description 获取题目提交记录的VO
 * @date 2024/2/6 13:27:24
 */
@Data
public class QuestionSubmitListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 提交记录id
     * **/
    private Long id;

    /**
     * 题目ID*/
    private Long questionId;

    /**
     * 题目名称*/
    private String questionName;


    /**
     * 用户名称*/
    private String userName;

    /**
     * 题目提交结果*/
    private String result;

    /**
     * 内存信息*/
    private Long memory;

    /**
     * 时间信息*/
    private Long time;

    /** 提交时间*/
    private Date submitTime;

    /**
     * 编程语言*/
    private String language;


}
