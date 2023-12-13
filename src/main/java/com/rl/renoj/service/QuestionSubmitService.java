package com.rl.renoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rl.renoj.model.dto.question.QuestionQueryRequest;
import com.rl.renoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.rl.renoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.rl.renoj.model.entity.Question;
import com.rl.renoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rl.renoj.model.entity.User;
import com.rl.renoj.model.vo.QuestionSubmitVO;
import com.rl.renoj.model.vo.QuestionVO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
* @author 任磊
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2023-11-28 15:52:05
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);


    /**
     * 获取查询条件
     * @param questionSubmitQueryRequest
     * @return QueryWrapper<QuestionSubmit>
     * */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);


    /**
     * @param questionSubmit
     * @param request
     * @return QuestionVO
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);
    /**
     * 分页查询题目封装
     * @param questionSubmitPage
     * @param request
     * @return Page<QuestionVO
     * */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage,User loginUser);
}
