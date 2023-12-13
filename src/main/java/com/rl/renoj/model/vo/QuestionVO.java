package com.rl.renoj.model.vo;
import cn.hutool.json.JSONUtil;
import com.google.gson.reflect.TypeToken;
import com.rl.renoj.model.dto.question.JudgeCase;
import com.rl.renoj.model.dto.question.JudgeConfig;
import com.rl.renoj.model.entity.Post;
import com.rl.renoj.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 题目
 * 返回给前端的类
 */
@Data
public class QuestionVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;


    /**
     * 提交数
     */
    private Integer submitNum;

    /**
     * 通过数
     */
    private Integer acceptNum;


    /**
     * 判题配置(json 对象)
     */
    private JudgeConfig judgeConfig;
    /**
     * 示例(json 对象)
     */
    private String judgeCase;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;
    /**
     * 创建用户 id
     */
    private String answer;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    private UserVO userVO;


    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTags();
        if (tagList != null) {
            question.setTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        String judgeCase = questionVO.getJudgeCase();
        if (judgeConfig!=null)
        {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        if (judgeCase!=null)
        {
            question.setJudgeCase(JSONUtil.toJsonStr(judgeCase));
        }
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        List<String> tagList = JSONUtil.toList(question.getTags(), String.class);
        String judgeConfigStr = question.getJudgeConfig();
        questionVO.setJudgeConfig(JSONUtil.toBean(judgeConfigStr, JudgeConfig.class));
        String judgeCase1 = question.getJudgeCase();
        questionVO.setJudgeCase(judgeCase1);
        questionVO.setTags(tagList);
        return questionVO;
    }
}